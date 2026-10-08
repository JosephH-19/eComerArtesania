package com.tallerbuendia.api.pedidos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Long> {
    List<EstadoPedido> findByPedidoIdOrderByFechaAsc(Long pedidoId);
}
