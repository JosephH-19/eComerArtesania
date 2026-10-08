package com.example.demo.producto;

import java.util.List;

public interface ProductoDAO {
    
    List<Producto> ProductoReporte(Integer idTipoProducto);

    List<Producto> listarTodos();

    void guardar(Producto producto);

    Producto buscarPorId(int id);

    void actualizar(Producto producto);

    List<Producto> filtrar(String nombre, String disponibilidad);

}
