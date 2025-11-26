// DetalleRequerimientoRepository.java
package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRequerimientoRepository extends JpaRepository<DetalleRequerimiento, Long> {
    List<DetalleRequerimiento> findByRequerimientoId(Long requerimientoId);
}

