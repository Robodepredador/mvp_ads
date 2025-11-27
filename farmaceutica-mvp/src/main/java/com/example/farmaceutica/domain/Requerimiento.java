package com.example.farmaceutica.domain;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requerimientos")
public class Requerimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private com.example.farmaceutica.security.Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE";

    @Column(length = 255)
    private String observaciones;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleRequerimiento> detalles;

    public Requerimiento() {
        this.fecha = LocalDateTime.now();
        this.estado = "NUEVO";
    }

    // Getters y setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }


    public com.example.farmaceutica.security.Usuario getUsuario() { return usuario; }
    public void setUsuario(com.example.farmaceutica.security.Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public List<DetalleRequerimiento> getDetalles() { return detalles; }

    public void setDetalles(List<DetalleRequerimiento> detalles) { this.detalles = detalles; }
}
