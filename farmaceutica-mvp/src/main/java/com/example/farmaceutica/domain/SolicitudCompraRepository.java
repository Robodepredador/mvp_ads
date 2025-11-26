package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitudCompraRepository extends JpaRepository<SolicitudCompra, Long> {
    Optional<SolicitudCompra> findByRequerimientoId(Long requerimientoId);
}
