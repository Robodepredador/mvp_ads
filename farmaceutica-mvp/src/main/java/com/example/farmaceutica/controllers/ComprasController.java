package com.example.farmaceutica.controllers;

import com.example.farmaceutica.domain.OrdenCompra;
import com.example.farmaceutica.domain.SolicitudCompra;
import com.example.farmaceutica.services.ComprasService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin
public class ComprasController {

    private final ComprasService comprasService;

    public ComprasController(ComprasService comprasService) {
        this.comprasService = comprasService;
    }

    @GetMapping("/pendientes")
    public List<SolicitudCompra> pendientes() {
        return comprasService.pendientes();
    }

    @PostMapping("/generar-orden/{id}")
    public OrdenCompra generar(@PathVariable Long id) {
        return comprasService.generarOrden(id);
    }
}
