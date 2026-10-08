package com.tallerbuendia.api.pagos;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerbuendia.api.dto.PagoRequest;
import com.tallerbuendia.api.dto.PagoResponse;
import com.tallerbuendia.api.pedidos.EstadoPedido;
import com.tallerbuendia.api.pedidos.EstadoPedidoRepository;
import com.tallerbuendia.api.pedidos.Pedido;
import com.tallerbuendia.api.pedidos.PedidoRepository;

@Service
@Transactional
public class PagoService {
    private final PagoRepository pagos;
    private final PedidoRepository pedidos;
    private final EstadoPedidoRepository estados;
    public PagoService(PagoRepository pagos, PedidoRepository pedidos, EstadoPedidoRepository estados) {
        this.pagos = pagos; this.pedidos = pedidos; this.estados = estados;
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listar() { return pagos.findAll().stream().map(this::toResponse).toList(); }

    @Transactional(readOnly = true)
    public PagoResponse obtener(Long id) { return toResponse(pagos.findById(id).orElseThrow(() -> new NoSuchElementException("Pago no encontrado: " + id))); }

    public PagoResponse registrar(PagoRequest request) {
        Pedido order = pedidos.findById(request.pedidoId()).orElseThrow(() -> new NoSuchElementException("Pedido no encontrado: " + request.pedidoId()));
        if (request.monto().compareTo(order.getTotal()) != 0) throw new IllegalArgumentException("El monto debe coincidir con el total del pedido");
        Pago payment = pagos.save(new Pago(order, request.metodo(), request.monto(), "PENDIENTE", request.referencia()));
        return toResponse(payment);
    }

    public PagoResponse confirmar(Long id) {
        Pago payment = pagos.findById(id).orElseThrow(() -> new NoSuchElementException("Pago no encontrado: " + id));
        if (!"PENDIENTE".equals(payment.getEstado())) throw new IllegalArgumentException("Solo se pueden confirmar pagos pendientes");
        payment.setEstado("CONFIRMADO");
        Pedido order = payment.getPedido();
        order.setEstado("PAGO_CONFIRMADO");
        pedidos.save(order);
        estados.save(new EstadoPedido(order, "PAGO_CONFIRMADO", "Pago confirmado: " + payment.getMetodo()));
        return toResponse(payment);
    }

    private PagoResponse toResponse(Pago payment) {
        return new PagoResponse(payment.getId(), payment.getPedido().getId(), payment.getPedido().getCodigo(),
            payment.getPedido().getCliente().getNombre(),
                payment.getMetodo(), payment.getMonto(), payment.getEstado(), payment.getReferencia(), payment.getFecha());
    }
}
