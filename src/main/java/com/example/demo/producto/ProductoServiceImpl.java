package com.example.demo.producto;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO ProductoDAO;

    public ProductoServiceImpl(ProductoDAO ProductoDAO) {
        this.ProductoDAO = ProductoDAO;
    }
    
    @Override
    public List<Producto> ProductoReporte(Integer idTipoProducto) {
        return ProductoDAO.ProductoReporte(idTipoProducto);
    }

    public List<Producto> listarTodos() {
        return ProductoDAO.listarTodos();
    }

}