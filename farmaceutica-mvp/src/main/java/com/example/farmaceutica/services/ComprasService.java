package com.example.farmaceutica.services;

import com.example.farmaceutica.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ComprasService {

    private final SolicitudCompraRepository solRepo;
    private final DetalleSolicitudCompraRepository detSolRepo;
    private final OrdenCompraRepository ocRepo;
    private final DetalleOrdenCompraRepository detOcRepo;

    public ComprasService(SolicitudCompraRepository solRepo,
                          DetalleSolicitudCompraRepository detSolRepo,
                          OrdenCompraRepository ocRepo,
                          DetalleOrdenCompraRepository detOcRepo) {
        this.solRepo = solRepo;
        this.detSolRepo = detSolRepo;
        this.ocRepo = ocRepo;
        this.detOcRepo = detOcRepo;
    }

    public List<SolicitudCompra> pendientes() {
        return solRepo.findAll().stream()
                .filter(s -> s.getEstado().equals("PENDIENTE"))
                .toList();
    }

    public OrdenCompra generarOrden(Long solicitudId) {
        SolicitudCompra sc = solRepo.findById(solicitudId).orElseThrow();
        List<DetalleSolicitudCompra> detalles = sc.getDetalles();

        OrdenCompra oc = new OrdenCompra();
        ocRepo.save(oc);

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleSolicitudCompra d : detalles) {
            DetalleOrdenCompra det = new DetalleOrdenCompra();
            det.setOrdenCompra(oc);
            det.setProducto(d.getProducto());
            det.setCantidad(d.getCantidad());
            det.setPrecioUnitario(d.getProducto().getPrecioReferencial());
            detOcRepo.save(det);

            total = total.add(d.getProducto().getPrecioReferencial()
                    .multiply(BigDecimal.valueOf(d.getCantidad())));
        }

        oc.setTotal(total);
        sc.setEstado("PROCESADA");

        solRepo.save(sc);
        ocRepo.save(oc);

        return oc;
    }
}
