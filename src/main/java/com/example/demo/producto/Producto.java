package com.example.demo.producto;

import java.time.LocalDate;

import com.example.demo.tipoProducto.TipoProducto;

public class Producto {

    private int id;
    private String nombre;
    private LocalDate fechaCreacion;
    private TipoProducto tipoProducto;
    
    public Producto(int id, String nombre, LocalDate fechaCreacion, TipoProducto tipoProducto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.tipoProducto = tipoProducto;
    }

    public Producto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
