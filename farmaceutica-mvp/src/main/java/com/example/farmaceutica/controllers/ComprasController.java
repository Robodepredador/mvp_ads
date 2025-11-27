package com.example.farmaceutica.controllers;

import com.example.farmaceutica.domain.OrdenCompra;
import com.example.farmaceutica.domain.ProductoProveedor;
import com.example.farmaceutica.domain.SolicitudCompra;
import com.example.farmaceutica.domain.DetalleSolicitudCompra;
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

    @GetMapping("/proveedores/{productoId}")
    public List<ProductoProveedor> proveedores(@PathVariable Long productoId) {
        return comprasService.proveedoresPorProducto(productoId);
    }

    @PostMapping("/detalles/{detalleId}/proveedor")
    public DetalleSolicitudCompra seleccionarProveedor(@PathVariable Long detalleId,
                                                       @RequestBody SeleccionProveedorRequest request) {
        return comprasService.asignarProveedor(detalleId, request.proveedorId(), request.precioNegociado());
    }

    @PostMapping("/generar-orden/{id}")
    public List<OrdenCompra> generar(@PathVariable Long id) {
        return comprasService.generarOrden(id);
    }

    public record SeleccionProveedorRequest(Long proveedorId, java.math.BigDecimal precioNegociado) {}
}
