package com.tallerbuendia.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CambioEstadoRequest(@NotBlank String estado, String observacion) {}
