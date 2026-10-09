INSERT INTO  estudiante 
 (id, nombre, apellido, codigo, fecha_nacimiento) 
 VALUES (1, 'Roberto Geronimo','Zarate Mendoza','C28933', '1982-01-01');

INSERT INTO  estudiante 
 (id, nombre, apellido, codigo, fecha_nacimiento) 
 VALUES (2, 'Mercedes','Mendoza','C11111','1980-06-06');
INSERT INTO  estudiante 
 (id, nombre, apellido, codigo, fecha_nacimiento) 
 VALUES (3, 'Edgar','Mendoza','C22222','1952-02-19');

INSERT INTO tipo_producto (nombre, fechaCreacion)
VALUES ('Bufandas', '2026-10-08');

INSERT INTO tipo_producto (nombre, fechaCreacion)
VALUES ('Ponchos', '2026-10-08');

INSERT INTO tipo_producto (nombre, fechaCreacion)
VALUES ('Mantas', '2026-10-08');

INSERT INTO producto
(nombre, fecha_creacion, id_tipo_producto, descripcion,
 material, precio, stock, estado, imagen)
VALUES
    ('Bufanda de alpaca', '2026-10-08', 1,
     'Bufanda tejida para clima frío.',
     'Lana de alpaca', 45.00, 10, 'ACTIVO', '');

INSERT INTO producto
(nombre, fecha_creacion, id_tipo_producto, descripcion,
 material, precio, stock, estado, imagen)
VALUES
    ('Bufanda de lana', '2026-10-08', 1,
     'Bufanda de lana con diseño a rayas.',
     'Lana de oveja', 30.00, 0, 'ACTIVO', '');

INSERT INTO producto
(nombre, fecha_creacion, id_tipo_producto, descripcion,
 material, precio, stock, estado, imagen)
VALUES
    ('Poncho tradicional', '2026-10-08', 2,
     'Poncho tejido con diseño tradicional.',
     'Lana de oveja', 120.00, 5, 'ACTIVO', '');

INSERT INTO producto
(nombre, fecha_creacion, id_tipo_producto, descripcion,
 material, precio, stock, estado, imagen)
VALUES
    ('Manta de alpaca', '2026-10-08', 3,
     'Manta tejida para abrigarse en casa.',
     'Lana de alpaca', 150.00, 3, 'ACTIVO', '');

INSERT INTO producto
(nombre, fecha_creacion, id_tipo_producto, descripcion,
 material, precio, stock, estado, imagen)
VALUES
    ('Poncho de muestra', '2026-10-08', 2,
     'Producto de ejemplo para probar el estado inactivo.',
     'Lana de oveja', 100.00, 2, 'INACTIVO', '');