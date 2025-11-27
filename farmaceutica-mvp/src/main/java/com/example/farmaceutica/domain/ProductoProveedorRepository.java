package com.example.farmaceutica.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, Long> {
    List<ProductoProveedor> findByProductoIdAndActivoTrue(Long productoId);
}

