package com.example.farmaceutica.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencia_transporte")
public class IncidenciaTransporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "detalle_transporte_id")
    private DetalleTransporte detalleTransporte;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private com.example.farmaceutica.security.Usuario usuario;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DetalleTransporte getDetalleTransporte() { return detalleTransporte; }
    public void setDetalleTransporte(DetalleTransporte detalleTransporte) { this.detalleTransporte = detalleTransporte; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public com.example.farmaceutica.security.Usuario getUsuario() { return usuario; }
    public void setUsuario(com.example.farmaceutica.security.Usuario usuario) { this.usuario = usuario; }
}
