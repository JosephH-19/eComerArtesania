package com.example.demo.bean;

import com.example.demo.producto.Producto;
import com.example.demo.producto.ProductoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.List;

@Component
@ViewScoped
public class ProductoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ProductoService productoService;

    private List<Producto> productos;

    @PostConstruct
    public void init() {
        cargarProductos();
    }

    public void cargarProductos() {
        productos = productoService.listarTodos();
    }

    // Getters y Setters
    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}