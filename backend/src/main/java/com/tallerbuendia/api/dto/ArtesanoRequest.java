package com.tallerbuendia.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtesanoRequest(
        @NotBlank String nombre,
        String especialidad,
        Integer aniosExperiencia,
        String correo,
        String telefono,
        Boolean activo) {}
