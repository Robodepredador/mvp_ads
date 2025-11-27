package com.example.farmaceutica.services;

import com.example.farmaceutica.domain.*;
import com.example.farmaceutica.services.dto.AsignacionTransporteRequest;
import com.example.farmaceutica.services.dto.IncidenciaTransporteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistribucionService {

    private final OrdenDistribucionRepository odRepo;
    private final VehiculoRepository vehRepo;
    private final SeguimientoDistribucionRepository segRepo;
    private final DetalleOrdenDistribucionRepository detalleOrdenDistribucionRepository;
    private final DetalleTransporteRepository detalleTransporteRepository;
    private final IncidenciaTransporteRepository incidenciaTransporteRepository;

    public DistribucionService(OrdenDistribucionRepository odRepo,
                               VehiculoRepository vehRepo,
                               SeguimientoDistribucionRepository segRepo,
                               DetalleOrdenDistribucionRepository detalleOrdenDistribucionRepository,
                               DetalleTransporteRepository detalleTransporteRepository,
                               IncidenciaTransporteRepository incidenciaTransporteRepository) {
        this.odRepo = odRepo;
        this.vehRepo = vehRepo;
        this.segRepo = segRepo;
        this.detalleOrdenDistribucionRepository = detalleOrdenDistribucionRepository;
        this.detalleTransporteRepository = detalleTransporteRepository;
        this.incidenciaTransporteRepository = incidenciaTransporteRepository;
    }

    public List<OrdenDistribucion> pendientes() {
        return odRepo.findAll();
    }

    @Transactional
    public SeguimientoDistribucion asignar(Long ordenId, Long vehId) {
        OrdenDistribucion od = odRepo.findById(ordenId).orElseThrow();
        Vehiculo v = vehRepo.findById(vehId).orElseThrow();

        SeguimientoDistribucion seg = new SeguimientoDistribucion();
        seg.setOrdenDistribucion(od);
        seg.setVehiculo(v);
        segRepo.save(seg);

        return seg;
    }

    @Transactional
    public List<SeguimientoDistribucion> asignarTransporte(AsignacionTransporteRequest request) {
        OrdenDistribucion orden = odRepo.findById(request.ordenDistribucionId()).orElseThrow();
        java.util.List<SeguimientoDistribucion> seguimientos = new java.util.ArrayList<>();

        for (AsignacionTransporteRequest.DetalleAsignacion detalle : request.detalles()) {
            DetalleOrdenDistribucion detOrden = detalleOrdenDistribucionRepository.findById(detalle.detalleOrdenDistribucionId()).orElseThrow();
            if (!detOrden.getOrdenDistribucion().getId().equals(orden.getId())) {
                throw new IllegalArgumentException("El detalle no pertenece a la orden");
            }
            Vehiculo vehiculo = vehRepo.findById(detalle.vehiculoId()).orElseThrow();

            SeguimientoDistribucion seguimiento = new SeguimientoDistribucion();
            seguimiento.setOrdenDistribucion(orden);
            seguimiento.setVehiculo(vehiculo);
            seguimiento.setObservacion(detalle.observacion());
            segRepo.save(seguimiento);
            seguimientos.add(seguimiento);

            DetalleTransporte transporte = new DetalleTransporte();
            transporte.setOrdenDistribucion(orden);
            transporte.setVehiculo(vehiculo);
            transporte.setLote(detOrden.getLote());
            transporte.setCantidad(detalle.cantidad());
            detalleTransporteRepository.save(transporte);
        }

        orden.setEstado("EN_RUTA");
        odRepo.save(orden);
        return seguimientos;
    }

    public String entregar(Long segId) {
        SeguimientoDistribucion seg = segRepo.findById(segId).orElseThrow();
        seg.setEstado("ENTREGADO");
        segRepo.save(seg);

        return "Entregado";
    }

    @Transactional
    public SeguimientoDistribucion actualizarEstado(Long seguimientoId, String estado, String observacion) {
        SeguimientoDistribucion seguimiento = segRepo.findById(seguimientoId).orElseThrow();
        if (estado != null && !estado.isBlank()) {
            seguimiento.setEstado(estado);
        }
        if (observacion != null) {
            seguimiento.setObservacion(observacion);
        }
        return segRepo.save(seguimiento);
    }

    @Transactional
    public String registrarIncidencia(Long seguimientoId, IncidenciaTransporteRequest request) {
        SeguimientoDistribucion seguimiento = segRepo.findById(seguimientoId).orElseThrow();
        DetalleTransporte detalleTransporte = detalleTransporteRepository.findById(request.detalleTransporteId())
                .orElseThrow();
        if (!detalleTransporte.getOrdenDistribucion().getId().equals(seguimiento.getOrdenDistribucion().getId())) {
            throw new IllegalArgumentException("El detalle de transporte no pertenece al seguimiento indicado");
        }

        IncidenciaTransporte incidencia = new IncidenciaTransporte();
        incidencia.setDetalleTransporte(detalleTransporte);
        incidencia.setDescripcion(request.descripcion());
        incidenciaTransporteRepository.save(incidencia);

        seguimiento.setEstado("INCIDENCIA");
        segRepo.save(seguimiento);
        return "Incidencia registrada";
    }
}
