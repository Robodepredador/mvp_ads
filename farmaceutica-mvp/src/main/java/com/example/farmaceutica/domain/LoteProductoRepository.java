package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteProductoRepository extends JpaRepository<LoteProducto, Long> {
    List<LoteProducto> findByProductoId(Long productoId);
}
