package com.example.farmaceutica.controllers;

import com.example.farmaceutica.domain.OrdenDistribucion;
import com.example.farmaceutica.domain.SeguimientoDistribucion;
import com.example.farmaceutica.services.DistribucionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distribucion")
@CrossOrigin
public class DistribucionController {

    private final DistribucionService distribucionService;

    public DistribucionController(DistribucionService distribucionService) {
        this.distribucionService = distribucionService;
    }

    @GetMapping("/pendientes")
    public List<OrdenDistribucion> pendientes() {
        return distribucionService.pendientes();
    }

    @PostMapping("/asignar")
    public SeguimientoDistribucion asignar(
            @RequestParam Long ordenId,
            @RequestParam Long vehiculoId
    ) {
        return distribucionService.asignar(ordenId, vehiculoId);
    }

    @PostMapping("/entregar/{id}")
    public String entregar(@PathVariable Long id) {
        return distribucionService.entregar(id);
    }
}
