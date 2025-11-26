package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdenDistribucionRepository extends JpaRepository<OrdenDistribucion, Long> {
    Optional<OrdenDistribucion> findByRequerimientoId(Long requerimientoId);
}
