package com.example.farmaceutica.services;

import com.example.farmaceutica.domain.*;
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

    public AlmacenService(OrdenCompraRepository ocRepo,
                          DetalleOrdenCompraRepository detOcRepo,
                          LoteProductoRepository loteRepo,
                          InventarioRepository invRepo,
                          MovimientoInventarioRepository movRepo) {
        this.ocRepo = ocRepo;
        this.detOcRepo = detOcRepo;
        this.loteRepo = loteRepo;
        this.invRepo = invRepo;
        this.movRepo = movRepo;
    }

    public List<OrdenCompra> pendientes() {
        return ocRepo.findAll().stream()
                .filter(o -> o.getEstado().equals("GENERADA"))
                .toList();
    }

    @Transactional
    public String recibir(Long ocId) {
        OrdenCompra oc = ocRepo.findById(ocId).orElseThrow();
        List<DetalleOrdenCompra> detalles = oc.getDetalles();

        for (DetalleOrdenCompra d : detalles) {
            LoteProducto lote = new LoteProducto();
            lote.setProducto(d.getProducto());
            lote.setCantidad(d.getCantidad());
            lote.setNumeroLote("L" + System.currentTimeMillis());
            loteRepo.save(lote);

            Inventario inv = invRepo.findAll().stream()
                    .filter(i -> i.getProducto().getId().equals(d.getProducto().getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        Inventario x = new Inventario();
                        x.setProducto(d.getProducto());
                        x.setCantidad(0);
                        return x;
                    });

            inv.setCantidad(inv.getCantidad() + d.getCantidad());
            invRepo.save(inv);

            MovimientoInventario mov = new MovimientoInventario();
            mov.setInventario(inv);
            mov.setTipo("ENTRADA");
            mov.setCantidad(d.getCantidad());
            movRepo.save(mov);
        }

        oc.setEstado("RECIBIDA");
        ocRepo.save(oc);

        return "Recepción completada";
    }
}
