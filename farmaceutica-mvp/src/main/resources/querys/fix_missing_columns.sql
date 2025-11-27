-- Script para agregar columnas de auditoría faltantes

-- Tabla: detalle_solicitud_compra
ALTER TABLE detalle_solicitud_compra ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_solicitud_compra ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_solicitud_compra ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE detalle_solicitud_compra ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: detalle_orden_distribucion
ALTER TABLE detalle_orden_distribucion ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_orden_distribucion ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_orden_distribucion ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE detalle_orden_distribucion ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: solicitudes_compra
ALTER TABLE solicitudes_compra ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE solicitudes_compra ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE solicitudes_compra ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE solicitudes_compra ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: requerimientos
ALTER TABLE requerimientos ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE requerimientos ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE requerimientos ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE requerimientos ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: ordenes_distribucion
ALTER TABLE ordenes_distribucion ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE ordenes_distribucion ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE ordenes_distribucion ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE ordenes_distribucion ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: ordenes_compra
ALTER TABLE ordenes_compra ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE ordenes_compra ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE ordenes_compra ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE ordenes_compra ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: detalle_requerimiento
ALTER TABLE detalle_requerimiento ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_requerimiento ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_requerimiento ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE detalle_requerimiento ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: detalle_orden_compra
ALTER TABLE detalle_orden_compra ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_orden_compra ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE detalle_orden_compra ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE detalle_orden_compra ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: movimientos_inventario
ALTER TABLE movimientos_inventario ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE movimientos_inventario ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE movimientos_inventario ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE movimientos_inventario ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Tabla: lotes_producto
ALTER TABLE lotes_producto ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE lotes_producto ADD COLUMN IF NOT EXISTS created_by BIGINT;
-- Nota: LoteProducto no tenía updated_at/updated_by en la entidad

-- Tabla: inventario
ALTER TABLE inventario ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE inventario ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE inventario ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE inventario ADD COLUMN IF NOT EXISTS updated_by BIGINT;
