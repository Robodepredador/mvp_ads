package com.example.farmaceutica.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lotes_producto")
public class LoteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroLote;

    private LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;


    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private java.time.LocalDateTime fechaRecepcion = java.time.LocalDateTime.now();

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    public LoteProducto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public java.time.LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(java.time.LocalDateTime fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
// Getters y setters
}
