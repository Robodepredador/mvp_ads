package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleSolicitudCompraRepository extends JpaRepository<DetalleSolicitudCompra, Long> {
    List<DetalleSolicitudCompra> findBySolicitudCompra_Requerimiento_Id(Long requerimientoId);
}

