package com.tallerbuendia.api.pedidos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tallerbuendia.api.catalogo.Producto;
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
@Table(name = "detalles_pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    private Pedido pedido;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    @Column(nullable = false)
    private Integer cantidad;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    protected DetallePedido() {}
    public DetallePedido(Producto producto, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
    void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
