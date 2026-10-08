package com.tallerbuendia.api.metricas;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerbuendia.api.catalogo.ProductoRepository;
import com.tallerbuendia.api.dto.MetricasResponse;
import com.tallerbuendia.api.pagos.PagoRepository;
import com.tallerbuendia.api.pedidos.PedidoRepository;

@Service
public class MetricasService {
    private final PedidoRepository pedidos;
    private final PagoRepository pagos;
    private final ProductoRepository productos;
    public MetricasService(PedidoRepository pedidos, PagoRepository pagos, ProductoRepository productos) {
        this.pedidos = pedidos; this.pagos = pagos; this.productos = productos;
    }

    @Transactional(readOnly = true)
    public MetricasResponse resumen() {
        var allOrders = pedidos.findAll();
        Map<String, Long> byStatus = allOrders.stream().collect(Collectors.groupingBy(order -> order.getEstado(), Collectors.counting()));
        BigDecimal paid = pagos.findAll().stream().filter(payment -> "CONFIRMADO".equals(payment.getEstado()))
                .map(payment -> payment.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pending = pagos.findAll().stream().filter(payment -> "PENDIENTE".equals(payment.getEstado()))
            .map(payment -> payment.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        YearMonth thisMonth = YearMonth.now();
        var ordersThisMonth = allOrders.stream().filter(order -> YearMonth.from(order.getFechaCreacion()).equals(thisMonth)).toList();
        var paidPaymentsThisMonth = pagos.findAll().stream()
            .filter(payment -> "CONFIRMADO".equals(payment.getEstado()))
            .filter(payment -> YearMonth.from(payment.getFecha()).equals(thisMonth)).toList();
        BigDecimal monthSales = ordersThisMonth.stream().filter(order -> "PAGO_CONFIRMADO".equals(order.getEstado())
                || "EN_PREPARACION".equals(order.getEstado()) || "LISTO_PARA_ENTREGA".equals(order.getEstado())
                || "ENTREGADO".equals(order.getEstado()))
            .map(order -> order.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
        long unitsSoldThisMonth = ordersThisMonth.stream()
            .filter(order -> !"CANCELADO".equals(order.getEstado()))
            .flatMap(order -> order.getDetalles().stream())
            .mapToLong(item -> item.getCantidad()).sum();
        BigDecimal incomeThisMonth = paidPaymentsThisMonth.stream().map(payment -> payment.getMonto())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new MetricasResponse(allOrders.size(), byStatus.getOrDefault("PENDIENTE_PAGO", 0L),
            byStatus.getOrDefault("EN_PREPARACION", 0L), byStatus.getOrDefault("ENTREGADO", 0L),
            byStatus.getOrDefault("PAGO_CONFIRMADO", 0L), productos.countByEstadoIgnoreCase("disponible"),
            paid, pending, monthSales, ordersThisMonth.size(), incomeThisMonth, unitsSoldThisMonth,
            byStatus.getOrDefault("CANCELADO", 0L), byStatus);
    }
}
