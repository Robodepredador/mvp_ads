package com.example.farmaceutica.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_solicitud_compra")
public class DetalleSolicitudCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitud_compra_id")
    private SolicitudCompra solicitudCompra;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;


    @Column(nullable = false)
    private Integer cantidad;

    @Column(precision = 12, scale = 2)
    private java.math.BigDecimal precioReferencial;

    @Column
    private Long createdBy;
    @Column
    private Long updatedBy;
    @Column(nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
    @Column(nullable = false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    public DetalleSolicitudCompra() {
    }

    public DetalleSolicitudCompra(SolicitudCompra solicitud, Producto producto, Integer cantidad) {
        this.solicitudCompra = solicitud;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SolicitudCompra getSolicitudCompra() {
        return solicitudCompra;
    }

    public void setSolicitudCompra(SolicitudCompra solicitudCompra) {
        this.solicitudCompra = solicitudCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }


    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public java.math.BigDecimal getPrecioReferencial() { return precioReferencial; }
    public void setPrecioReferencial(java.math.BigDecimal precioReferencial) { this.precioReferencial = precioReferencial; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Getters y setters
}
