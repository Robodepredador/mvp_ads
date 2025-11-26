package com.example.farmaceutica.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_transporte")
public class DetalleTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_distribucion_id")
    private OrdenDistribucion ordenDistribucion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lote_id")
    private LoteProducto lote;

    @Column(nullable = false)
    private Integer cantidad;

    public DetalleTransporte() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public OrdenDistribucion getOrdenDistribucion() { return ordenDistribucion; }
    public void setOrdenDistribucion(OrdenDistribucion ordenDistribucion) { this.ordenDistribucion = ordenDistribucion; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public LoteProducto getLote() { return lote; }
    public void setLote(LoteProducto lote) { this.lote = lote; }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
