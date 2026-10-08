package com.example.demo.producto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoServiceImpl(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public List<Producto> ProductoReporte(Integer idTipoProducto) {
        return productoDAO.ProductoReporte(idTipoProducto);
    }

    @Override
    public List<Producto> listarTodos() {
        return productoDAO.listarTodos();
    }

    @Override
    public void guardar(Producto producto) {

        if (producto.getNombre() == null ||
                producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (producto.getDescripcion() == null ||
                producto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripción es obligatoria."
            );
        }

        if (producto.getMaterial() == null ||
                producto.getMaterial().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El material es obligatorio."
            );
        }

        if (producto.getNombre().length() > 200 ||
                producto.getDescripcion().length() > 1000 ||
                producto.getMaterial().length() > 200) {
            throw new IllegalArgumentException(
                    "El nombre, descripción o material supera el tamaño permitido."
            );
        }

        if (producto.getPrecio() == null ||
                producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0 ||
                producto.getPrecio().compareTo(
                        new BigDecimal("99999999.99")) > 0 ||
                producto.getPrecio().scale() > 2) {
            throw new IllegalArgumentException(
                    "Ingresa un precio mayor que cero, con hasta dos decimales."
            );
        }

        if (producto.getStock() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        if (producto.getTipoProducto() == null ||
                producto.getTipoProducto().getId() == null ||
                producto.getTipoProducto().getId() <= 0) {
            throw new IllegalArgumentException(
                    "Selecciona un tipo de producto."
            );
        }

        if (producto.getImagen() == null) {
            producto.setImagen("");
        }

        if (producto.getImagen().length() > 500) {
            throw new IllegalArgumentException(
                    "La ruta de imagen no puede superar 500 caracteres."
            );
        }

        producto.setNombre(producto.getNombre().trim());
        producto.setDescripcion(producto.getDescripcion().trim());
        producto.setMaterial(producto.getMaterial().trim());
        producto.setFechaCreacion(LocalDate.now());
        producto.setEstado("ACTIVO");

        productoDAO.guardar(producto);
    }
}