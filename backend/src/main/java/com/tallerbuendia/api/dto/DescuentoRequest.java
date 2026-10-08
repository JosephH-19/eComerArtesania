package com.tallerbuendia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DescuentoRequest(
        @NotBlank String nombre,
        String descripcion,
        @NotBlank String tipo,
        @NotNull @Positive BigDecimal valor,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String aplicableA,
        Long objetivoId,
        Boolean activo) {}
