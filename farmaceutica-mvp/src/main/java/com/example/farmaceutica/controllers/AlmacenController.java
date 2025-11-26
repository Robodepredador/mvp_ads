package com.example.farmaceutica.controllers;

import com.example.farmaceutica.domain.OrdenCompra;
import com.example.farmaceutica.services.AlmacenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/almacen")
@CrossOrigin
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping("/pendientes")
    public List<OrdenCompra> pendientes() {
        return almacenService.pendientes();
    }

    @PostMapping("/recibir/{id}")
    public String recibir(@PathVariable Long id) {
        return almacenService.recibir(id);
    }
}
