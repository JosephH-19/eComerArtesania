package com.tallerbuendia.api.clientes;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 160)
    private String correo;

    @Column(nullable = false, length = 30)
    private String telefono;

    @Column(length = 250)
    private String direccion;

    @Column(length = 100)
    private String distrito;

    private LocalDate fechaNacimiento;

    @Column(length = 40)
    private String genero;

    @Column(nullable = false, length = 20)
    private String estado = "activo";

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    protected Cliente() {}

    public Cliente(String nombre, String dni, String correo, String telefono, String direccion,
                   String distrito, LocalDate fechaNacimiento, String genero, String estado) {
        this.nombre = nombre;
        this.dni = dni;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.distrito = distrito;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.estado = estado == null || estado.isBlank() ? "activo" : estado;
    }

    @PrePersist
    void onCreate() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
