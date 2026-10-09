package com.example.demo.Artesanos;

import java.time.LocalDate;

public class Artesano {
    private int id;
    private String nombre;
    private String especialidad;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correoElectronico;
    private int edad;
    private int anosExp;
    private int productosElaborados;
    private boolean estado;
    private String imagen;

    public Artesano(int id, String nombre, String especialidad, LocalDate fechaNacimiento,
                    String telefono, String correoElectronico,
                    int edad, int anosExp, int productosElaborados, boolean estado,  String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.edad = edad;
        this.anosExp = anosExp;
        this.productosElaborados = productosElaborados;
        this.estado = estado;
        this.imagen = imagen;
    }


    public Artesano() {}



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public int getAnosExp() { return anosExp; }
    public void setAnosExp(int anosExp) { this.anosExp = anosExp; }

    public int getProductosElaborados() { return productosElaborados; }
    public void setProductosElaborados(int productosElaborados) { this.productosElaborados = productosElaborados; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

}
