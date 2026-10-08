INSERT INTO  estudiante 
 (id, nombre, apellido, codigo, fecha_nacimiento) 
 VALUES (1, 'Roberto Geronimo','Zarate Mendoza','C28933', '1982-01-01');

INSERT INTO  estudiante 
 (id, nombre, apellido, codigo, fecha_nacimiento) 
 VALUES (2, 'Mercedes','Mendoza','C11111','1980-06-06');
INSERT INTO  estudiante 
 (id, nombre, apellido, codigo, fecha_nacimiento) 
 VALUES (3, 'Edgar','Mendoza','C22222','1952-02-19');  

INSERT INTO  tipo_producto
 ( nombre, fechaCreacion) 
 VALUES ('Detergentes en polvo','2025-02-02');

INSERT INTO  tipo_producto
 ( nombre, fechaCreacion) 
 VALUES ('Detergentes liquidos','2025-02-04');

INSERT INTO  tipo_producto  
 ( nombre, fechaCreacion) 
 VALUES ('Detergentes enzimaticos ','2025-02-06');

INSERT INTO  producto
( nombre, fecha_creacion, id_tipo_producto) 
 values ('Detergente en polvo Ariel','2025-02-02',1);

INSERT INTO  producto 
( nombre, fecha_creacion, id_tipo_producto) 
values ('Detergente en polvo Ace','2025-02-04',1);

INSERT INTO  producto 
( nombre, fecha_creacion, id_tipo_producto) 
 values ('Detergente en polvo Omo','2025-02-06',1);

INSERT INTO  producto
( nombre, fecha_creacion, id_tipo_producto) 
 values ('Detergente liquido Ariel','2025-02-02',2);

INSERT INTO  producto
( nombre, fecha_creacion, id_tipo_producto )
values ('Detergente liquido Ace','2025-02-04',2);