package com.example.demo.producto;

import java.util.List;
import java.math.BigDecimal;

public interface ProductoService  {
    
    List<Producto> ProductoReporte(Integer idTipoProducto);

    List<Producto> listarTodos();

    void guardar(Producto producto);

    Producto buscarPorId(int id);

    void actualizar(Producto producto);

    List<Producto> filtrar(String nombre, String disponibilidad);

    List<Producto> catalogo(
            String nombre,
            String disponibilidad,
            BigDecimal precioMinimo,
            BigDecimal precioMaximo);

    void cambiarEstado(int id, String estado);
    List<Producto> catalogo(String nombre, String disponibilidad, BigDecimal minimo, BigDecimal maximo, Integer categoria);

}
