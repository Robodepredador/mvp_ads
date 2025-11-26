package com.example.farmaceutica.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguimiento_distribucion")
public class SeguimientoDistribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_distribucion_id")
    private OrdenDistribucion ordenDistribucion;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    private String estado; // ASIGNADO, EN_RUTA, ENTREGADO

    private String observacion;

    private LocalDateTime fecha;

    public SeguimientoDistribucion() {
        this.fecha = LocalDateTime.now();
        this.estado = "ASIGNADO";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public OrdenDistribucion getOrdenDistribucion() {
        return ordenDistribucion;
    }

    public void setOrdenDistribucion(OrdenDistribucion ordenDistribucion) {
        this.ordenDistribucion = ordenDistribucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}



