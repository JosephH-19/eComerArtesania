package com.tallerbuendia.api.dto;

import java.math.BigDecimal;
import java.util.Map;

public record MetricasResponse(long totalPedidos, long pedidosPendientesPago,
                               long pedidosEnPreparacion, long pedidosEntregados,
                               long pedidosConfirmados, long productosActivos,
                               BigDecimal totalPagado, BigDecimal saldoPendiente,
                               BigDecimal ventasMes, long pedidosMes,
                               BigDecimal ingresosMes, long productosVendidosMes,
                               long pedidosCancelados,
                               Map<String, Long> pedidosPorEstado) {}
