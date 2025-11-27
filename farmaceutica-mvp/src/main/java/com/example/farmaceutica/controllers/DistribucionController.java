package com.example.farmaceutica.controllers;

import com.example.farmaceutica.domain.OrdenDistribucion;
import com.example.farmaceutica.domain.SeguimientoDistribucion;
import com.example.farmaceutica.services.DistribucionService;
import com.example.farmaceutica.services.dto.ActualizarSeguimientoRequest;
import com.example.farmaceutica.services.dto.AsignacionTransporteRequest;
import com.example.farmaceutica.services.dto.IncidenciaTransporteRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distribucion")
@CrossOrigin
@PreAuthorize("hasRole('DISTRIBUCION')")
public class DistribucionController {

    private final DistribucionService distribucionService;

    public DistribucionController(DistribucionService distribucionService) {
        this.distribucionService = distribucionService;
    }

    @GetMapping("/vehiculos")
    public List<com.example.farmaceutica.domain.Vehiculo> getVehiculos() {
        return distribucionService.listarVehiculos();
    }

    @GetMapping("/pendientes")
    public List<OrdenDistribucion> pendientes() {
        return distribucionService.pendientes();
    }

    @PostMapping("/asignar")
    public SeguimientoDistribucion asignar(
            @RequestParam Long ordenId,
            @RequestParam Long vehiculoId) {
        return distribucionService.asignar(ordenId, vehiculoId);
    }

    @PostMapping("/asignaciones")
    public List<SeguimientoDistribucion> asignarTransporte(@RequestBody AsignacionTransporteRequest request) {
        return distribucionService.asignarTransporte(request);
    }

    @PostMapping("/entregar/{id}")
    public String entregar(@PathVariable Long id) {
        return distribucionService.entregar(id);
    }

    @PostMapping("/seguimiento/{id}/estado")
    public SeguimientoDistribucion actualizarEstado(@PathVariable Long id,
            @RequestBody ActualizarSeguimientoRequest request) {
        return distribucionService.actualizarEstado(id, request.estado(), request.observacion());
    }

    @PostMapping("/seguimiento/{id}/incidencias")
    public String registrarIncidencia(@PathVariable Long id,
            @RequestBody IncidenciaTransporteRequest request) {
        return distribucionService.registrarIncidencia(id, request);
    }
}
