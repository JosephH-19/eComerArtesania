package com.tallerbuendia.api.descuentos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "descuentos")
public class Descuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 140)
    private String nombre;
    @Column(length = 800)
    private String descripcion;
    @Column(nullable = false, length = 20)
    private String tipo;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @Column(nullable = false, length = 20)
    private String aplicableA = "todos";
    private Long objetivoId;
    @Column(nullable = false)
    private boolean activo = true;

    protected Descuento() {}

    public Descuento(String nombre, String descripcion, String tipo, BigDecimal valor, LocalDate fechaInicio,
                     LocalDate fechaFin, String aplicableA, Long objetivoId, boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.valor = valor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.aplicableA = aplicableA == null ? "todos" : aplicableA;
        this.objetivoId = objetivoId;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getAplicableA() { return aplicableA; }
    public void setAplicableA(String aplicableA) { this.aplicableA = aplicableA; }
    public Long getObjetivoId() { return objetivoId; }
    public void setObjetivoId(Long objetivoId) { this.objetivoId = objetivoId; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
