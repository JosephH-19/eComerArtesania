package com.example.demo.tipoProducto;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TipoProductoServiceImpl implements TipoProductoService {

    private final TipoProductoDAO tipoProductoDAO;

    public TipoProductoServiceImpl(TipoProductoDAO tipoProductoDAO) {
        this.tipoProductoDAO = tipoProductoDAO;
    }

    public List<TipoProducto> listaTipoProducto() {
        return tipoProductoDAO.listaTipoProducto();
    }

    public TipoProducto obtenerTipoProductoPorId(int id) {
        return tipoProductoDAO.obtenerTipoProductoPorId(id);
    }

    public void crearTipoProducto(TipoProducto tipoProducto) {
        tipoProductoDAO.crearTipoProducto(tipoProducto);
    }
    
}