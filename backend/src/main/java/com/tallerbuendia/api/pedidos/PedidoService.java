package com.tallerbuendia.api.pedidos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerbuendia.api.catalogo.Producto;
import com.tallerbuendia.api.catalogo.ProductoRepository;
import com.tallerbuendia.api.clientes.Cliente;
import com.tallerbuendia.api.clientes.ClienteRepository;
import com.tallerbuendia.api.dto.PedidoRequest;
import com.tallerbuendia.api.dto.PedidoResponse;

@Service
@Transactional
public class PedidoService {
    private static final List<String> ESTADOS = List.of("PENDIENTE_PAGO", "PAGO_CONFIRMADO", "EN_PREPARACION", "LISTO_PARA_ENTREGA", "ENTREGADO", "CANCELADO");
    private final PedidoRepository pedidos;
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    private final EstadoPedidoRepository estados;
    private final com.tallerbuendia.api.descuentos.DescuentoService descuentos;

    public PedidoService(PedidoRepository pedidos, ProductoRepository productos, ClienteRepository clientes,
                         EstadoPedidoRepository estados, com.tallerbuendia.api.descuentos.DescuentoService descuentos) {
        this.pedidos = pedidos; this.productos = productos; this.clientes = clientes; this.estados = estados; this.descuentos = descuentos;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() { return pedidos.findAll().stream().map(this::toResponse).toList(); }

    @Transactional(readOnly = true)
    public PedidoResponse obtener(Long id) { return toResponse(pedido(id)); }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorDni(String dni) {
        return pedidos.findByClienteDniOrderByFechaCreacionDesc(dni).stream()
                .filter(order -> !"CANCELADO".equals(order.getEstado()))
                .map(this::toResponse).toList();
    }

    public PedidoResponse crear(PedidoRequest request) {
        Cliente client = clientes.findByDni(request.dni()).orElseGet(() -> clientes.save(new Cliente(
                request.nombre(), request.dni(), request.correo(), request.telefono(), request.direccion(),
                request.distrito(), null, null, "activo")));
        String code = "ORD-" + java.time.Year.now().getValue() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        Pedido order = new Pedido(code, client, request.metodoEntrega(), request.direccion());
        BigDecimal total = BigDecimal.ZERO;
        for (PedidoRequest.ItemRequest requested : request.items()) {
            Producto product = productos.findById(requested.productoId())
                    .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + requested.productoId()));
            if (product.getStock() < requested.cantidad()) throw new IllegalArgumentException("Stock insuficiente para " + product.getNombre());
            BigDecimal unitPrice = descuentos.precioFinal(product);
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(requested.cantidad()));
            product.setStock(product.getStock() - requested.cantidad());
            order.addDetalle(new DetallePedido(product, requested.cantidad(), unitPrice, subtotal));
            total = total.add(subtotal);
        }
        order.setTotal(total);
        Pedido saved = pedidos.save(order);
        estados.save(new EstadoPedido(saved, saved.getEstado(), "Pedido creado"));
        return toResponse(saved);
    }

    public PedidoResponse cambiarEstado(Long id, String rawStatus, String note) {
        Pedido order = pedido(id);
        String status = rawStatus.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        if (!ESTADOS.contains(status)) throw new IllegalArgumentException("Estado de pedido no válido: " + rawStatus);
        if (!"CANCELADO".equals(order.getEstado()) && "CANCELADO".equals(status)) {
            order.getDetalles().forEach(item -> item.getProducto().setStock(item.getProducto().getStock() + item.getCantidad()));
        }
        order.setEstado(status);
        Pedido saved = pedidos.save(order);
        estados.save(new EstadoPedido(saved, status, note));
        return toResponse(saved);
    }

    Pedido pedido(Long id) { return pedidos.findById(id).orElseThrow(() -> new NoSuchElementException("Pedido no encontrado: " + id)); }
    Pedido pedidoPorCodigo(String code) { return pedidos.findByCodigo(code).orElseThrow(() -> new NoSuchElementException("Pedido no encontrado: " + code)); }

    private PedidoResponse toResponse(Pedido order) {
        List<PedidoResponse.Item> items = order.getDetalles().stream().map(item -> new PedidoResponse.Item(
                item.getProducto().getId(), item.getProducto().getNombre(), item.getCantidad(),
                item.getPrecioUnitario(), item.getSubtotal())).toList();
        List<PedidoResponse.Evento> history = estados.findByPedidoIdOrderByFechaAsc(order.getId()).stream()
                .map(event -> new PedidoResponse.Evento(event.getEstado(), event.getObservacion(), event.getFecha())).toList();
        return new PedidoResponse(order.getId(), order.getCodigo(), order.getEstado(), order.getTotal(),
                order.getMetodoEntrega(), order.getCliente().getNombre(), order.getCliente().getDni(),
                order.getFechaCreacion(), items, history);
    }
}
