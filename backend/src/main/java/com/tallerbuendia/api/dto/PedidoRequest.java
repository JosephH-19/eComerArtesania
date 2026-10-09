package com.tallerbuendia.api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record PedidoRequest(
        @NotBlank String nombre,
        @NotBlank @Pattern(regexp = "[0-9]{8}") String dni,
        @NotBlank String correo,
        @NotBlank @Pattern(regexp = "[0-9]{9}") String telefono,
        String direccion,
        String distrito,
        String metodoEntrega,
        @NotEmpty List<@Valid ItemRequest> items) {
    public record ItemRequest(Long productoId, @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive Integer cantidad) {}
}
