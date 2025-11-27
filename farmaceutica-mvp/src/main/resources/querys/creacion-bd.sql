-- ENUMS para estados
CREATE TYPE estado_requerimiento AS ENUM ('PENDIENTE', 'ATENDIDO', 'CANCELADO');
CREATE TYPE estado_solicitud_compra AS ENUM ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA');
CREATE TYPE estado_orden_compra AS ENUM ('GENERADA', 'ENVIADA', 'RECIBIDA', 'CANCELADA');
CREATE TYPE estado_orden_distribucion AS ENUM ('PENDIENTE', 'EN_RUTA', 'ENTREGADA', 'CANCELADA');
CREATE TYPE estado_vehiculo AS ENUM ('DISPONIBLE', 'EN_USO', 'MANTENIMIENTO', 'INACTIVO');

-- Usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    nombre_completo VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Productos
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    unidad_medida VARCHAR(20),
    precio_referencial NUMERIC(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Proveedores
CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruc VARCHAR(20) UNIQUE,
    direccion TEXT,
    telefono VARCHAR(20),
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Relación producto-proveedor
CREATE TABLE producto_proveedor (
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    proveedor_id INT NOT NULL REFERENCES proveedores(id) ON DELETE CASCADE,
    precio NUMERIC(12,2) NOT NULL CHECK (precio >= 0),
    activo BOOLEAN DEFAULT TRUE,
    UNIQUE (producto_id, proveedor_id)
);

-- Requerimientos
CREATE TABLE requerimientos (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuarios(id),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado estado_requerimiento DEFAULT 'PENDIENTE',
    observaciones TEXT,
    created_by INT REFERENCES usuarios(id),
    updated_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalle_requerimiento (
    id SERIAL PRIMARY KEY,
    requerimiento_id INT NOT NULL REFERENCES requerimientos(id) ON DELETE CASCADE,
    producto_id INT NOT NULL REFERENCES productos(id),
    cantidad INT NOT NULL CHECK (cantidad > 0),
    observaciones TEXT,
    created_by INT REFERENCES usuarios(id),
    updated_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Solicitud de compra
CREATE TABLE solicitud_compra (
    id SERIAL PRIMARY KEY,
    requerimiento_id INT NOT NULL REFERENCES requerimientos(id),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado estado_solicitud_compra DEFAULT 'PENDIENTE',
    created_by INT REFERENCES usuarios(id),
    updated_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalle_solicitud_compra (
    id SERIAL PRIMARY KEY,
    solicitud_compra_id INT NOT NULL REFERENCES solicitud_compra(id) ON DELETE CASCADE,
    producto_id INT NOT NULL REFERENCES productos(id),
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_referencial NUMERIC(12,2) CHECK (precio_referencial >= 0),
    precio_negociado NUMERIC(12,2) CHECK (precio_negociado >= 0),
    proveedor_id INT REFERENCES proveedores(id),
    motivo_decision VARCHAR(255)
);

-- Orden de compra
CREATE TABLE orden_compra (
    id BIGSERIAL PRIMARY KEY,
    solicitud_compra_id BIGINT NOT NULL REFERENCES solicitud_compra(id),
    proveedor_id BIGINT NOT NULL REFERENCES proveedores(id),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(14,2) CHECK (total >= 0),
    estado estado_orden_compra DEFAULT 'GENERADA',
    created_by INT REFERENCES usuarios(id),
    updated_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalle_orden_compra (
    id BIGSERIAL PRIMARY KEY,
    orden_compra_id BIGINT NOT NULL REFERENCES orden_compra(id) ON DELETE CASCADE,
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) CHECK (precio_unitario >= 0),
    subtotal NUMERIC(14,2) CHECK (subtotal >= 0)
);

-- Lotes de productos
CREATE TABLE lotes_producto (
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL REFERENCES productos(id),
    numero_lote VARCHAR(50) NOT NULL,
    fecha_vencimiento DATE,
    cantidad INT NOT NULL CHECK (cantidad >= 0),
    fecha_recepcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inventario y movimientos
CREATE TABLE inventario (
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL REFERENCES productos(id),
    lote_id INT NOT NULL REFERENCES lotes_producto(id),
    cantidad INT NOT NULL CHECK (cantidad >= 0),
    ubicacion VARCHAR(100),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT REFERENCES usuarios(id),
    updated_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movimiento_inventario (
    id SERIAL PRIMARY KEY,
    inventario_id INT NOT NULL REFERENCES inventario(id) ON DELETE CASCADE,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA', 'SALIDA', 'AJUSTE')),
    cantidad INT NOT NULL CHECK (cantidad > 0),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT REFERENCES usuarios(id),
    observaciones TEXT
);

-- Incidencias en lotes
CREATE TABLE incidencia_lote (
    id SERIAL PRIMARY KEY,
    lote_id INT NOT NULL REFERENCES lotes_producto(id) ON DELETE CASCADE,
    descripcion TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT REFERENCES usuarios(id)
);

-- Orden de distribución
CREATE TABLE orden_distribucion (
    id SERIAL PRIMARY KEY,
    requerimiento_id INT NOT NULL REFERENCES requerimientos(id),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado estado_orden_distribucion DEFAULT 'PENDIENTE',
    created_by INT REFERENCES usuarios(id),
    updated_by INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalle_orden_distribucion (
    id SERIAL PRIMARY KEY,
    orden_distribucion_id INT NOT NULL REFERENCES orden_distribucion(id) ON DELETE CASCADE,
    producto_id INT NOT NULL REFERENCES productos(id),
    cantidad INT NOT NULL CHECK (cantidad > 0),
    lote_id INT REFERENCES lotes_producto(id),
    motivo_decision VARCHAR(255)
);

-- Vehículos
CREATE TABLE vehiculo (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(20) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    capacidad INT CHECK (capacidad >= 0),
    estado estado_vehiculo DEFAULT 'DISPONIBLE',
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seguimiento de vehículos y transporte
CREATE TABLE seguimiento_vehiculo (
    id SERIAL PRIMARY KEY,
    vehiculo_id INT NOT NULL REFERENCES vehiculo(id),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ubicacion VARCHAR(100),
    estado VARCHAR(30),
    observaciones TEXT
);

CREATE TABLE detalle_transporte (
    id SERIAL PRIMARY KEY,
    orden_distribucion_id INT NOT NULL REFERENCES orden_distribucion(id),
    vehiculo_id INT NOT NULL REFERENCES vehiculo(id),
    lote_id INT NOT NULL REFERENCES lotes_producto(id),
    cantidad INT NOT NULL CHECK (cantidad > 0)
);

CREATE TABLE incidencia_transporte (
    id SERIAL PRIMARY KEY,
    detalle_transporte_id INT NOT NULL REFERENCES detalle_transporte(id) ON DELETE CASCADE,
    descripcion TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT REFERENCES usuarios(id)
);

-- Triggers para updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ language 'plpgsql';

DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename IN (
        'usuarios','productos','proveedores','requerimientos','solicitud_compra','orden_compra','inventario','orden_distribucion','vehiculo'
    )
    LOOP
        EXECUTE format('CREATE TRIGGER trg_%I_updated_at BEFORE UPDATE ON %I FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();', r.tablename, r.tablename);
    END LOOP;
END $$;