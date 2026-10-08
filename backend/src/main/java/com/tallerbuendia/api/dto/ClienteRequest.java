package com.tallerbuendia.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequest(
        @NotBlank String nombre,
        @NotBlank @Pattern(regexp = "[0-9]{8}") String dni,
        @NotBlank @Email String correo,
        @NotBlank @Pattern(regexp = "[0-9]{9}") String telefono,
        String direccion,
        String distrito,
        LocalDate fechaNacimiento,
        String genero,
        String estado) {}
