package com.example.farmaceutica.services;

import com.example.farmaceutica.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistribucionService {

    private final OrdenDistribucionRepository odRepo;
    private final VehiculoRepository vehRepo;
    private final SeguimientoDistribucionRepository segRepo;

    public DistribucionService(OrdenDistribucionRepository odRepo,
                               VehiculoRepository vehRepo,
                               SeguimientoDistribucionRepository segRepo) {
        this.odRepo = odRepo;
        this.vehRepo = vehRepo;
        this.segRepo = segRepo;
    }

    public List<OrdenDistribucion> pendientes() {
        return odRepo.findAll();
    }

    @Transactional
    public SeguimientoDistribucion asignar(Long ordenId, Long vehId) {
        OrdenDistribucion od = odRepo.findById(ordenId).orElseThrow();
        Vehiculo v = vehRepo.findById(vehId).orElseThrow();

        SeguimientoDistribucion seg = new SeguimientoDistribucion();
        seg.setOrdenDistribucion(od);
        seg.setVehiculo(v);
        segRepo.save(seg);

        return seg;
    }

    public String entregar(Long segId) {
        SeguimientoDistribucion seg = segRepo.findById(segId).orElseThrow();
        seg.setEstado("ENTREGADO");
        segRepo.save(seg);

        return "Entregado";
    }
}
