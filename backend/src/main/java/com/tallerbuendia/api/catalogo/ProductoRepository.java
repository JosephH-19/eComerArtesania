package com.tallerbuendia.api.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    long countByEstadoIgnoreCase(String estado);
}
