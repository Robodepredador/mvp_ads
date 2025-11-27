-- =====================================
--   INSERTS BASE PARA SISTEMA MVP
--   BD COMPATIBLE CON TU MODELO REAL
-- =====================================

-- =====================================
-- 1. USUARIOS
-- =====================================
INSERT INTO usuarios (id, username, password, rol, activo, nombre_completo, created_at, updated_at) VALUES
  (1, 'programacion', '$2y$12$AJL85JOTFUc0RFq6CaIUfOzfXVQfrh3Fse4HyZZ3BscFOpmz1RKsS', 'PROGRAMACION', TRUE, 'Usuario Programación', NOW(), NOW()),
  (2, 'compras',      '$2y$12$AJL85JOTFUc0RFq6CaIUfOzfXVQfrh3Fse4HyZZ3BscFOpmz1RKsS', 'COMPRAS', TRUE, 'Usuario Compras', NOW(), NOW()),
  (3, 'almacen',      '$2y$12$AJL85JOTFUc0RFq6CaIUfOzfXVQfrh3Fse4HyZZ3BscFOpmz1RKsS', 'ALMACEN', TRUE, 'Usuario Almacén', NOW(), NOW()),
  (4, 'distribucion', '$2y$12$AJL85JOTFUc0RFq6CaIUfOzfXVQfrh3Fse4HyZZ3BscFOpmz1RKsS', 'DISTRIBUCION', TRUE, 'Usuario Distribución', NOW(), NOW()),
  (5, 'admin',        '$2y$12$AJL85JOTFUc0RFq6CaIUfOzfXVQfrh3Fse4HyZZ3BscFOpmz1RKsS', 'ADMIN', TRUE, 'Administrador General', NOW(), NOW());
-- =====================================
-- 2. PRODUCTOS
-- =====================================
INSERT INTO productos (id, nombre, descripcion, unidad_medida, precio_referencial, created_at, updated_at) VALUES
  (100,'Paracetamol 500mg','Caja x20','CAJA',   1.20,NOW(),NOW()),
  (101,'Ibuprofeno 400mg','Blister x10','BLISTER',1.80,NOW(),NOW()),
  (102,'Amoxicilina 500mg','Caja x12','CAJA',   2.50,NOW(),NOW()),
  (103,'Vitamina C 1g','Caja x30','CAJA',      0.90,NOW(),NOW()),
  (104,'Omeprazol 20mg','Caja x14','CAJA',     1.10,NOW(),NOW()),
  (105,'Loratadina 10mg','Blister x10','BLISTER',0.75,NOW(),NOW()),
  (106,'Insulina NPH','Frasco 10ml','FRASCO',  8.40,NOW(),NOW()),
  (107,'Suero Fisiológico 500ml','Bolsa','UNIDAD',0.55,NOW(),NOW());

-- =====================================
-- 3. PROVEEDORES
-- =====================================
INSERT INTO proveedores (id, nombre, ruc, direccion, telefono, email, activo, created_at, updated_at) VALUES
  (200,'Laboratorios Salud','12345678901','Av. Central 123','999111222','compras@labsalud.com',TRUE, NOW(),NOW()),
  (201,'Distribuidora Pharma','10987654321','Jr. Norte 456','988777666','ventas@pharma.com',TRUE, NOW(),NOW());

-- =====================================
-- 4. PRODUCTO – PROVEEDOR
-- =====================================
INSERT INTO producto_proveedor (id, producto_id, proveedor_id, precio, activo) VALUES
  (1,100,200,1.10,TRUE),
  (2,101,200,1.70,TRUE),
  (3,102,200,2.30,TRUE),
  (4,103,201,0.80,TRUE),
  (5,104,201,1.00,TRUE),
  (6,105,200,0.70,TRUE),
  (7,106,201,8.00,TRUE),
  (8,107,201,0.50,TRUE);

-- =====================================
-- 5. REQUERIMIENTOS
-- =====================================
INSERT INTO requerimientos (id, usuario_id, fecha, estado, observaciones, created_by, updated_by, created_at, updated_at) VALUES
  (300,1,NOW(),'PENDIENTE','Reposición hospitales zona norte',1,1,NOW(),NOW()),
  (301,2,NOW(),'PENDIENTE','Campaña de vacunación regional',2,2,NOW(),NOW()),
  (302,3,NOW(),'PENDIENTE','Reposición botiquines escuelas',3,3,NOW(),NOW()),
  (303,4,NOW(),'PENDIENTE','Stock emergencia temporada',4,4,NOW(),NOW()),
  (304,5,NOW(),'PENDIENTE','Plan logístico rural',5,5,NOW(),NOW());

-- =====================================
-- 6. DETALLE REQUERIMIENTO
-- =====================================
INSERT INTO detalle_requerimiento (id,requerimiento_id,producto_id,cantidad,observaciones) VALUES
  (400,300,100,80,'Dolor/fiebre'),
  (401,300,101,50,'Dolor muscular'),
  (402,301,102,40,'Antibiótico'),
  (403,301,107,120,'Hidratación'),
  (404,302,103,90,'Suplemento'),
  (405,302,104,60,'Gástrico'),
  (406,303,105,70,'Alergias'),
  (407,303,100,45,'Dolor/fiebre'),
  (408,304,106,30,'Insulina'),
  (409,304,107,80,'Hidratación');

-- =====================================
-- 7. LOTES
-- =====================================
INSERT INTO lotes_producto (id,producto_id,numero_lote,fecha_vencimiento,cantidad,fecha_recepcion,created_by,created_at) VALUES
  (500,100,'L-PARA-01','2025-10-01',120,NOW(),1,NOW()),
  (501,100,'L-PARA-02','2026-03-15',70,NOW(),1,NOW()),
  (502,101,'L-IBU-01','2025-08-20',65,NOW(),1,NOW()),
  (503,102,'L-AMOX-01','2026-05-30',90,NOW(),1,NOW()),
  (504,103,'L-VITC-01','2025-09-10',140,NOW(),1,NOW()),
  (505,104,'L-OMEP-01','2025-11-05',80,NOW(),1,NOW()),
  (506,105,'L-LORA-01','2025-07-18',120,NOW(),1,NOW()),
  (507,106,'L-INSU-01','2024-10-25',45,NOW(),1,NOW()),
  (508,107,'L-SUERO-01','2025-04-12',200,NOW(),1,NOW());

-- =====================================
-- 8. INVENTARIO
-- =====================================
INSERT INTO inventario (id,producto_id,lote_id,cantidad,ubicacion,fecha,created_by,updated_by,created_at,updated_at) VALUES
  (600,100,500,120,'Almacén Central',NOW(),1,1,NOW(),NOW()),
  (601,100,501,70,'Almacén Sur',NOW(),1,1,NOW(),NOW()),
  (602,101,502,65,'Almacén Norte',NOW(),1,1,NOW(),NOW()),
  (603,102,503,90,'Almacén Central',NOW(),1,1,NOW(),NOW()),
  (604,103,504,140,'Almacén Central',NOW(),1,1,NOW(),NOW()),
  (605,104,505,80,'Almacén Sur',NOW(),1,1,NOW(),NOW()),
  (606,105,506,120,'Almacén Norte',NOW(),1,1,NOW(),NOW()),
  (607,106,507,45,'Almacén Central',NOW(),1,1,NOW(),NOW()),
  (608,107,508,200,'Almacén Central',NOW(),1,1,NOW(),NOW());

-- =====================================
-- 9. SOLICITUD DE COMPRA
-- =====================================
INSERT INTO solicitud_compra (id,requerimiento_id,fecha,estado,created_at,updated_at) VALUES
  (700,300,NOW(),'PENDIENTE',NOW(),NOW());

INSERT INTO detalle_solicitud_compra (id,solicitud_compra_id,producto_id,cantidad,precio_referencial) VALUES
  (701,700,101,10,1.80);

-- =====================================
-- 10. ORDEN DE DISTRIBUCIÓN
-- =====================================
INSERT INTO orden_distribucion (id,requerimiento_id,fecha,estado,created_at,updated_at) VALUES
  (800,301,NOW(),'PENDIENTE',NOW(),NOW());

INSERT INTO detalle_orden_distribucion (id,orden_distribucion_id,producto_id,cantidad,lote_id) VALUES
  (801,800,102,20,503);
