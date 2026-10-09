package com.example.demo.tipoProducto;

import java.util.List;

public interface TipoProductoDAO {
    List<TipoProducto> listarActivos();
    void desactivar(int id);
    
    public List<TipoProducto> listaTipoProducto();

    public TipoProducto obtenerTipoProductoPorId(int id);
    
    public void crearTipoProducto(TipoProducto tipoProducto);

}
