package com.tallerbuendia.api.catalogo;

import java.math.BigDecimal;

import com.tallerbuendia.api.artesanos.Artesano;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(length = 1200)
    private String descripcion;

    @Column(length = 120)
    private String material;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false, length = 30)
    private String estado = "disponible";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artesano_id")
    private Artesano artesano;

    @Column(length = 500)
    private String imagen;

    protected Producto() {}

    public Producto(String nombre, String descripcion, String material, BigDecimal precio, Integer stock,
                    String estado, Categoria categoria, Artesano artesano, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.material = material;
        this.precio = precio;
        this.stock = stock == null ? 0 : stock;
        this.estado = estado == null || estado.isBlank() ? "disponible" : estado;
        this.categoria = categoria;
        this.artesano = artesano;
        this.imagen = imagen;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Artesano getArtesano() { return artesano; }
    public void setArtesano(Artesano artesano) { this.artesano = artesano; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
