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
    private final ProductoProveedorRepository productoProveedorRepository;
    private final ProveedorRepository proveedorRepository;

    public ComprasService(SolicitudCompraRepository solRepo,
                          DetalleSolicitudCompraRepository detSolRepo,
                          OrdenCompraRepository ocRepo,
                          DetalleOrdenCompraRepository detOcRepo,
                          ProductoProveedorRepository productoProveedorRepository,
                          ProveedorRepository proveedorRepository) {
        this.solRepo = solRepo;
        this.detSolRepo = detSolRepo;
        this.ocRepo = ocRepo;
        this.detOcRepo = detOcRepo;
        this.productoProveedorRepository = productoProveedorRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<SolicitudCompra> pendientes() {
        return solRepo.findAll().stream()
                .filter(s -> s.getEstado().equals("PENDIENTE"))
                .toList();
    }

    public java.util.List<ProductoProveedor> proveedoresPorProducto(Long productoId) {
        return productoProveedorRepository.findByProductoIdAndActivoTrue(productoId);
    }

    public DetalleSolicitudCompra asignarProveedor(Long detalleId, Long proveedorId, java.math.BigDecimal precioNegociado) {
        DetalleSolicitudCompra detalle = detSolRepo.findById(detalleId).orElseThrow();
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow();
        detalle.setProveedorSeleccionado(proveedor);
        if (precioNegociado != null) {
            detalle.setPrecioNegociado(precioNegociado);
        }
        return detSolRepo.save(detalle);
    }

    public java.util.List<OrdenCompra> generarOrden(Long solicitudId) {
        SolicitudCompra sc = solRepo.findById(solicitudId).orElseThrow();
        List<DetalleSolicitudCompra> detalles = sc.getDetalles();

        java.util.Map<Proveedor, List<DetalleSolicitudCompra>> detallesPorProveedor = detalles.stream()
                .peek(det -> {
                    if (det.getProveedorSeleccionado() == null) {
                        throw new IllegalStateException("El detalle " + det.getId() + " no tiene proveedor asignado");
                    }
                })
                .collect(java.util.stream.Collectors.groupingBy(DetalleSolicitudCompra::getProveedorSeleccionado));

        java.util.List<OrdenCompra> ordenesGeneradas = new java.util.ArrayList<>();

        for (java.util.Map.Entry<Proveedor, List<DetalleSolicitudCompra>> entry : detallesPorProveedor.entrySet()) {
            Proveedor proveedor = entry.getKey();
            OrdenCompra oc = new OrdenCompra();
            oc.setSolicitudCompra(sc);
            oc.setProveedor(proveedor);
            ocRepo.save(oc);

            BigDecimal total = BigDecimal.ZERO;

            for (DetalleSolicitudCompra d : entry.getValue()) {
                DetalleOrdenCompra det = new DetalleOrdenCompra();
                det.setOrdenCompra(oc);
                det.setProducto(d.getProducto());
                det.setCantidad(d.getCantidad());

                BigDecimal precio = d.getPrecioNegociado() != null ? d.getPrecioNegociado() : d.getPrecioReferencial();
                det.setPrecioUnitario(precio);
                det.setSubtotal(precio.multiply(BigDecimal.valueOf(d.getCantidad())));
                detOcRepo.save(det);

                total = total.add(det.getSubtotal());
            }

            oc.setTotal(total);
            ocRepo.save(oc);
            ordenesGeneradas.add(oc);
        }

        sc.setEstado("PROCESADA");

        solRepo.save(sc);
        return ordenesGeneradas;
    }
}
