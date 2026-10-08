package com.tallerbuendia.api.pagos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbuendia.api.pedidos.Pedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;
    @Column(nullable = false, length = 40)
    private String metodo;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;
    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE";
    @Column(length = 100)
    private String referencia;
    @Column(nullable = false)
    private LocalDateTime fecha;

    protected Pago() {}
    public Pago(Pedido pedido, String metodo, BigDecimal monto, String estado, String referencia) {
        this.pedido = pedido;
        this.metodo = metodo;
        this.monto = monto;
        this.estado = estado == null ? "PENDIENTE" : estado;
        this.referencia = referencia;
    }
    @PrePersist
    void onCreate() { if (fecha == null) fecha = LocalDateTime.now(); }
    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public String getMetodo() { return metodo; }
    public BigDecimal getMonto() { return monto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getReferencia() { return referencia; }
    public LocalDateTime getFecha() { return fecha; }
}
