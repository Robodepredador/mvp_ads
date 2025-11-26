package com.example.farmaceutica.services;

import com.example.farmaceutica.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ProgramacionService {
    private final RequerimientoRepository reqRepo;
    private final DetalleRequerimientoRepository detReqRepo;
    private final ProductoRepository productoRepo;
    private final InventarioRepository inventarioRepo;
    private final LoteProductoRepository loteRepo;
    private final SolicitudCompraRepository solCompraRepo;
    private final DetalleSolicitudCompraRepository detSolCompraRepo;
    private final OrdenDistribucionRepository ordDistRepo;
    private final DetalleOrdenDistribucionRepository detOrdDistRepo;

    public ProgramacionService(
        RequerimientoRepository reqRepo,
        DetalleRequerimientoRepository detReqRepo,
        ProductoRepository productoRepo,
        InventarioRepository inventarioRepo,
        LoteProductoRepository loteRepo,
        SolicitudCompraRepository solCompraRepo,
        DetalleSolicitudCompraRepository detSolCompraRepo,
        OrdenDistribucionRepository ordDistRepo,
        DetalleOrdenDistribucionRepository detOrdDistRepo
    ) {
        this.reqRepo = reqRepo;
        this.detReqRepo = detReqRepo;
        this.productoRepo = productoRepo;
        this.inventarioRepo = inventarioRepo;
        this.loteRepo = loteRepo;
        this.solCompraRepo = solCompraRepo;
        this.detSolCompraRepo = detSolCompraRepo;
        this.ordDistRepo = ordDistRepo;
        this.detOrdDistRepo = detOrdDistRepo;
    }

    // 1. Listar requerimientos pendientes
    public List<Requerimiento> listarRequerimientosPendientes() {
        return reqRepo.findAll(); // Puedes filtrar por estado si es necesario
    }

    // 2. Obtener detalle de un requerimiento
    public Requerimiento obtenerDetalleRequerimiento(Long requerimientoId) {
        Objects.requireNonNull(requerimientoId, "requerimientoId no puede ser nulo");
        return reqRepo.findById(requerimientoId).orElse(null);
    }

    // 3. Consultar inventario y lotes para un producto
    public List<Inventario> consultarInventarioPorProducto(Long productoId) {
        Objects.requireNonNull(productoId, "productoId no puede ser nulo");
        return inventarioRepo.findByProductoId(productoId);
    }

    public List<LoteProducto> consultarLotesPorProducto(Long productoId) {
        Objects.requireNonNull(productoId, "productoId no puede ser nulo");
        return loteRepo.findByProductoId(productoId);
    }

    // 4. Registrar decisión del usuario para un producto
    @Transactional
    public String registrarDecisionDistribucion(Long requerimientoId, Long productoId, int cantidad, Long loteId) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        Objects.requireNonNull(requerimientoId, "requerimientoId no puede ser nulo");
        Objects.requireNonNull(productoId, "productoId no puede ser nulo");
        Objects.requireNonNull(loteId, "loteId no puede ser nulo");

        Requerimiento requerimiento = reqRepo.findById(requerimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Requerimiento no encontrado"));
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        LoteProducto lote = loteRepo.findById(loteId)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado"));

        if (!lote.getProducto().getId().equals(productoId)) {
            throw new IllegalArgumentException("El lote no pertenece al producto indicado");
        }

        OrdenDistribucion orden = ensureOrdenDistribucion(requerimiento);

        DetalleOrdenDistribucion detalle = new DetalleOrdenDistribucion();
        detalle.setOrdenDistribucion(orden);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setLote(lote);
        detOrdDistRepo.save(detalle);
        return buildMensajeEstado("Distribución registrada", requerimiento);
    }

    @Transactional
    public String registrarDecisionCompra(Long requerimientoId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        Objects.requireNonNull(requerimientoId, "requerimientoId no puede ser nulo");
        Objects.requireNonNull(productoId, "productoId no puede ser nulo");

        Requerimiento requerimiento = reqRepo.findById(requerimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Requerimiento no encontrado"));
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        SolicitudCompra solicitud = ensureSolicitudCompra(requerimiento);

        DetalleSolicitudCompra detalle = new DetalleSolicitudCompra();
        detalle.setSolicitudCompra(solicitud);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioReferencial(producto.getPrecioReferencial());
        detSolCompraRepo.save(detalle);
        return buildMensajeEstado("Solicitud de compra registrada", requerimiento);
    }

    private OrdenDistribucion ensureOrdenDistribucion(Requerimiento requerimiento) {
        return ordDistRepo.findByRequerimientoId(requerimiento.getId())
                .orElseGet(() -> {
                    OrdenDistribucion nueva = new OrdenDistribucion();
                    nueva.setRequerimiento(requerimiento);
                    return ordDistRepo.save(nueva);
                });
    }

    private SolicitudCompra ensureSolicitudCompra(Requerimiento requerimiento) {
        return solCompraRepo.findByRequerimientoId(requerimiento.getId())
                .orElseGet(() -> {
                    SolicitudCompra nueva = new SolicitudCompra();
                    nueva.setRequerimiento(requerimiento);
                    return solCompraRepo.save(nueva);
                });
    }

    // 5. Finalizar la programación de un requerimiento
    @Transactional
    public String finalizarProgramacion(Long requerimientoId) {
        Objects.requireNonNull(requerimientoId, "requerimientoId no puede ser nulo");
        Requerimiento req = reqRepo.findById(requerimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Requerimiento no encontrado"));

        if (!estaCompletamenteProgramado(req.getId())) {
            throw new IllegalStateException("Aún hay productos sin decisión. Revisa el requerimiento antes de finalizar.");
        }

        req.setEstado("PROGRAMADO");
        reqRepo.save(req);
        return "Requerimiento finalizado. Estado: PROGRAMADO";
    }

    private String buildMensajeEstado(String baseMensaje, Requerimiento requerimiento) {
        String estado = actualizarEstadoRequerimiento(requerimiento);
        return baseMensaje + ". Estado del requerimiento: " + estado;
    }

    private String actualizarEstadoRequerimiento(Requerimiento requerimiento) {
        String estado = estaCompletamenteProgramado(requerimiento.getId()) ? "PROGRAMADO" : "PARCIAL";
        requerimiento.setEstado(estado);
        reqRepo.save(requerimiento);
        return estado;
    }

    private boolean estaCompletamenteProgramado(Long requerimientoId) {
        List<DetalleRequerimiento> detalles = detReqRepo.findByRequerimientoId(requerimientoId);
        if (detalles.isEmpty()) {
            return true;
        }

        Map<Long, Integer> programadas = calcularCantidadesProgramadas(requerimientoId);
        return detalles.stream()
                .allMatch(detalle -> programadas.getOrDefault(detalle.getProducto().getId(), 0) >= detalle.getCantidad());
    }

    private Map<Long, Integer> calcularCantidadesProgramadas(Long requerimientoId) {
        Map<Long, Integer> suma = new HashMap<>();

        detOrdDistRepo.findByOrdenDistribucion_Requerimiento_Id(requerimientoId)
                .forEach(det -> acumularCantidad(suma, det.getProducto(), det.getCantidad()));

        detSolCompraRepo.findBySolicitudCompra_Requerimiento_Id(requerimientoId)
                .forEach(det -> acumularCantidad(suma, det.getProducto(), det.getCantidad()));

        return suma;
    }

    private void acumularCantidad(Map<Long, Integer> suma, Producto producto, Integer cantidad) {
        if (producto == null || producto.getId() == null || cantidad == null) {
            return;
        }
        suma.merge(producto.getId(), cantidad, (a, b) -> Integer.valueOf(a + b));
    }
}
