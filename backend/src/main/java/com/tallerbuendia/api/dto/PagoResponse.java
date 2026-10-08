package com.tallerbuendia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(Long id, Long pedidoId, String codigoPedido, String cliente, String metodo,
                           BigDecimal monto, String estado, String referencia, LocalDateTime fecha) {}
