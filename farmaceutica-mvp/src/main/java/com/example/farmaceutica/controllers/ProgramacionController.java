package com.example.farmaceutica.controllers;

import com.example.farmaceutica.domain.*;
import com.example.farmaceutica.services.ProgramacionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programacion")
@CrossOrigin
@PreAuthorize("hasRole('PROGRAMACION')")
public class ProgramacionController {

    private final ProgramacionService programacionService;

    public ProgramacionController(ProgramacionService programacionService) {
        this.programacionService = programacionService;
    }

    // 1. Listar requerimientos pendientes
    @GetMapping("/requerimientos")
    public java.util.List<Requerimiento> listarRequerimientosPendientes() {
        return programacionService.listarRequerimientosPendientes();
    }

    // 2. Obtener detalle de un requerimiento
    @GetMapping("/requerimientos/{id}")
    public Requerimiento obtenerDetalleRequerimiento(@PathVariable Long id) {
        return programacionService.obtenerDetalleRequerimiento(id);
    }

    // 3. Consultar inventario para un producto
    @GetMapping("/inventario")
    public java.util.List<Inventario> consultarInventarioPorProducto(@RequestParam Long productoId) {
        return programacionService.consultarInventarioPorProducto(productoId);
    }

    // 4. Consultar lotes para un producto
    @GetMapping("/lotes")
    public java.util.List<LoteProducto> consultarLotesPorProducto(@RequestParam Long productoId) {
        return programacionService.consultarLotesPorProducto(productoId);
    }

    @GetMapping("/resumen")
    public ProgramacionService.ResumenProgramacion resumen() {
        return programacionService.obtenerResumen();
    }

    @GetMapping("/requerimientos/{id}/historial")
    public List<ProgramacionService.DecisionRegistro> historial(@PathVariable Long id) {
        return programacionService.obtenerHistorial(id);
    }

    // 5. Registrar decisión de distribución
    @PostMapping("/decidir/distribucion")
    public String registrarDecisionDistribucion(@RequestParam Long requerimientoId,
            @RequestParam Long productoId,
            @RequestParam int cantidad,
            @RequestParam Long loteId,
            @RequestParam(required = false) String motivo) {
        return programacionService.registrarDecisionDistribucion(requerimientoId, productoId, cantidad, loteId, motivo);
    }

    // 6. Registrar decisión de compra
    @PostMapping("/decidir/compra")
    public String registrarDecisionCompra(@RequestParam Long requerimientoId,
            @RequestParam Long productoId,
            @RequestParam int cantidad,
            @RequestParam(required = false) String motivo) {
        return programacionService.registrarDecisionCompra(requerimientoId, productoId, cantidad, motivo);
    }

    // 7. Finalizar programación
    @PostMapping("/requerimientos/{id}/finalizar")
    public String finalizarProgramacion(@PathVariable Long id) {
        return programacionService.finalizarProgramacion(id);
    }
}
