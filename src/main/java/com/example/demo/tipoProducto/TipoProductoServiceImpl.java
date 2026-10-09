package com.example.demo.tipoProducto;

import java.util.List;
import java.time.LocalDate;
import com.example.demo.comun.Validacion;

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
        tipoProducto.setNombre(Validacion.texto(tipoProducto.getNombre(), "el nombre", 200, true));
        tipoProducto.setDescripcion(Validacion.texto(tipoProducto.getDescripcion(), "la descripción", 255, false));
        tipoProducto.setFechaCreacion(LocalDate.now());
        tipoProductoDAO.crearTipoProducto(tipoProducto);
    }

    public List<TipoProducto> listarActivos() {
        return tipoProductoDAO.listarActivos();
    }

    public void desactivar(int id) {
        tipoProductoDAO.desactivar(id);
    }
    
}
