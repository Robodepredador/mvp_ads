package com.example.farmaceutica.services;

import com.example.farmaceutica.domain.*;
import com.example.farmaceutica.services.dto.RecepcionOrdenRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlmacenService {

    private final OrdenCompraRepository ocRepo;
    private final DetalleOrdenCompraRepository detOcRepo;
    private final LoteProductoRepository loteRepo;
    private final InventarioRepository invRepo;
    private final MovimientoInventarioRepository movRepo;
    private final IncidenciaLoteRepository incidenciaLoteRepository;

    public AlmacenService(OrdenCompraRepository ocRepo,
                          DetalleOrdenCompraRepository detOcRepo,
                          LoteProductoRepository loteRepo,
                          InventarioRepository invRepo,
                          MovimientoInventarioRepository movRepo,
                          IncidenciaLoteRepository incidenciaLoteRepository) {
        this.ocRepo = ocRepo;
        this.detOcRepo = detOcRepo;
        this.loteRepo = loteRepo;
        this.invRepo = invRepo;
        this.movRepo = movRepo;
        this.incidenciaLoteRepository = incidenciaLoteRepository;
    }

    public List<OrdenCompra> pendientes() {
        return ocRepo.findAll().stream()
                .filter(o -> o.getEstado().equals("GENERADA"))
                .toList();
    }

    @Transactional
    public String recibirBasico(Long ocId) {
        OrdenCompra oc = ocRepo.findById(ocId).orElseThrow();
        List<RecepcionOrdenRequest.RecepcionDetalleRequest> detalles = oc.getDetalles().stream()
                .map(det -> new RecepcionOrdenRequest.RecepcionDetalleRequest(
                        det.getId(),
                        "L-" + det.getId() + "-" + System.currentTimeMillis(),
                        java.time.LocalDate.now().plusYears(1),
                        det.getCantidad(),
                        "ALMACEN-PRINCIPAL",
                        null
                ))
                .toList();
        return recibirConDetalle(new RecepcionOrdenRequest(ocId, detalles));
    }

    @Transactional
    public String recibirConDetalle(RecepcionOrdenRequest request) {
        OrdenCompra oc = ocRepo.findById(request.ordenCompraId()).orElseThrow();

        for (RecepcionOrdenRequest.RecepcionDetalleRequest detalleRequest : request.detalles()) {
            DetalleOrdenCompra detalle = detOcRepo.findById(detalleRequest.detalleOrdenCompraId()).orElseThrow();
            if (!detalle.getOrdenCompra().getId().equals(oc.getId())) {
                throw new IllegalArgumentException("El detalle no pertenece a la orden indicada");
            }

            LoteProducto lote = new LoteProducto();
            lote.setProducto(detalle.getProducto());
            lote.setCantidad(detalleRequest.cantidadRecibida());
            lote.setNumeroLote(detalleRequest.numeroLote());
            lote.setFechaVencimiento(detalleRequest.fechaVencimiento());
            loteRepo.save(lote);

            Inventario inventario = new Inventario();
            inventario.setProducto(detalle.getProducto());
            inventario.setLote(lote);
            inventario.setCantidad(detalleRequest.cantidadRecibida());
            inventario.setUbicacion(detalleRequest.ubicacion());
            invRepo.save(inventario);

            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setInventario(inventario);
            movimiento.setTipo("ENTRADA");
            movimiento.setCantidad(detalleRequest.cantidadRecibida());
            movimiento.setObservaciones("Recepción OC #" + oc.getId());
            movRepo.save(movimiento);

            if (detalleRequest.incidencia() != null && !detalleRequest.incidencia().isBlank()) {
                IncidenciaLote incidencia = new IncidenciaLote();
                incidencia.setLote(lote);
                incidencia.setDescripcion(detalleRequest.incidencia());
                incidenciaLoteRepository.save(incidencia);
            }
        }

        oc.setEstado("RECIBIDA");
        ocRepo.save(oc);
        return "Recepción registrada";
    }
}
