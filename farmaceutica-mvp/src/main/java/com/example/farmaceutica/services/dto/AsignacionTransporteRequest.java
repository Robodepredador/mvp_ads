package com.example.farmaceutica.services.dto;

import java.util.List;

public record AsignacionTransporteRequest(
        Long ordenDistribucionId,
        List<DetalleAsignacion> detalles
) {
    public record DetalleAsignacion(
            Long detalleOrdenDistribucionId,
            Long vehiculoId,
            Integer cantidad,
            String observacion
    ) {}
}

