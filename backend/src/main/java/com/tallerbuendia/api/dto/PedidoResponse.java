package com.tallerbuendia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(Long id, String codigo, String estado, BigDecimal total,
                             String metodoEntrega, String nombreCliente, String dni,
                             LocalDateTime fechaCreacion, List<Item> items, List<Evento> seguimiento) {
    public record Item(Long productoId, String producto, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {}
    public record Evento(String estado, String observacion, LocalDateTime fecha) {}
}
