package com.tallerbuendia.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(@NotBlank String nombre, String descripcion, Boolean activa) {}
