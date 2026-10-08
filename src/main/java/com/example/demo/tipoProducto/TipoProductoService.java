package com.example.demo.tipoProducto;

import java.util.List;

public interface TipoProductoService  {
    
    public List<TipoProducto> listaTipoProducto();

    public TipoProducto obtenerTipoProductoPorId(int id);

    public void crearTipoProducto(TipoProducto tipoProducto);
    
}
