package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleOrdenDistribucionRepository extends JpaRepository<DetalleOrdenDistribucion, Long> {
    List<DetalleOrdenDistribucion> findByOrdenDistribucion_Requerimiento_Id(Long requerimientoId);
}

