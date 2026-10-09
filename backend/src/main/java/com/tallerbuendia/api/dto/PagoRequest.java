package com.tallerbuendia.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PagoRequest(
        @NotNull Long pedidoId,
        @NotBlank String metodo,
        @NotNull @Positive BigDecimal monto,
        String referencia) {}
