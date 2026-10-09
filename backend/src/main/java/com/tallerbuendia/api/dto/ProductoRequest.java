package com.tallerbuendia.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequest(
        @NotBlank String nombre,
        String descripcion,
        String material,
        @NotNull @PositiveOrZero BigDecimal precio,
        @NotNull @PositiveOrZero Integer stock,
        String estado,
        Long categoriaId,
        Long artesanoId,
        String imagen) {}
