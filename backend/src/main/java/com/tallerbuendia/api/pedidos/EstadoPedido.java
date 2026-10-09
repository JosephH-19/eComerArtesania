package com.tallerbuendia.api.pedidos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "seguimientos_pedido")
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;
    @Column(nullable = false, length = 30)
    private String estado;
    @Column(length = 600)
    private String observacion;
    @Column(nullable = false)
    private LocalDateTime fecha;

    protected EstadoPedido() {}
    public EstadoPedido(Pedido pedido, String estado, String observacion) {
        this.pedido = pedido;
        this.estado = estado;
        this.observacion = observacion;
    }
    @PrePersist
    void onCreate() { if (fecha == null) fecha = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getEstado() { return estado; }
    public String getObservacion() { return observacion; }
    public LocalDateTime getFecha() { return fecha; }
    public Pedido getPedido() { return pedido; }
}
