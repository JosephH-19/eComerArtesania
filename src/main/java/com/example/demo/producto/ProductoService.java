package com.example.demo.producto;

import java.util.List;

public interface ProductoService  {
    
    List<Producto> ProductoReporte(Integer idTipoProducto);

    List<Producto> listarTodos();

    void guardar(Producto producto);

}
