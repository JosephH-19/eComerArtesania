package com.example.demo.tipoProducto;

import java.time.LocalDate;

public class TipoProducto {
     
    private Integer id;
    private String nombre;
    private LocalDate fechaCreacion;
    

    public TipoProducto() {
        // Constructor vacío
    }
    
    public TipoProducto(int id, String nombre, LocalDate fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}
