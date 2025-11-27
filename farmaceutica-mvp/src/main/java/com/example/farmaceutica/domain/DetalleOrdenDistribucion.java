package com.example.farmaceutica.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_orden_distribucion")
public class DetalleOrdenDistribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_distribucion_id")
    private OrdenDistribucion ordenDistribucion;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;


    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private LoteProducto lote;

    @Column(length = 255)
    private String motivoDecision;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;
    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    public DetalleOrdenDistribucion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdenDistribucion getOrdenDistribucion() {
        return ordenDistribucion;
    }

    public void setOrdenDistribucion(OrdenDistribucion ordenDistribucion) {
        this.ordenDistribucion = ordenDistribucion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }


    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public LoteProducto getLote() { return lote; }
    public void setLote(LoteProducto lote) { this.lote = lote; }
    public String getMotivoDecision() { return motivoDecision; }
    public void setMotivoDecision(String motivoDecision) { this.motivoDecision = motivoDecision; }
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
