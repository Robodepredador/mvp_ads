package com.example.farmaceutica.services.dto;

import java.time.LocalDate;
import java.util.List;

public record RecepcionOrdenRequest(
        Long ordenCompraId,
        List<RecepcionDetalleRequest> detalles
) {
    public record RecepcionDetalleRequest(
            Long detalleOrdenCompraId,
            String numeroLote,
            LocalDate fechaVencimiento,
            Integer cantidadRecibida,
            String ubicacion,
            String incidencia
    ) {}
}

