package com.tallerbuendia.api.pagos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByPedidoIdOrderByFechaDesc(Long pedidoId);
}
