package com.tallerbuendia.api.pedidos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tallerbuendia.api.clientes.Cliente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 32)
    private String codigo;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE_PAGO";
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
    @Column(nullable = false, length = 60)
    private String metodoEntrega = "RECOJO_TALLER";
    @Column(length = 250)
    private String direccionEntrega;
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();

    protected Pedido() {}

    public Pedido(String codigo, Cliente cliente, String metodoEntrega, String direccionEntrega) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.metodoEntrega = metodoEntrega == null ? "RECOJO_TALLER" : metodoEntrega;
        this.direccionEntrega = direccionEntrega;
    }

    @PrePersist
    void onCreate() { if (fechaCreacion == null) fechaCreacion = LocalDateTime.now(); }

    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getMetodoEntrega() { return metodoEntrega; }
    public void setMetodoEntrega(String metodoEntrega) { this.metodoEntrega = metodoEntrega; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<DetallePedido> getDetalles() { return detalles; }
}
