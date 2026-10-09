CREATE TABLE IF NOT EXISTS estudiante (
    id INT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    apellido VARCHAR(200) NOT NULL,
    codigo VARCHAR(10) UNIQUE,
    fecha_nacimiento DATE NOT NULL
);

create table if not exists tipo_producto(
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre varchar(200) not null,
    fechaCreacion date not null,
    descripcion VARCHAR(255) NOT NULL DEFAULT '',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS artesano (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    anos_experiencia INT NOT NULL CHECK (anos_experiencia >= 0),
    correo VARCHAR(150) NOT NULL DEFAULT '',
    telefono VARCHAR(20) NOT NULL DEFAULT '',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS producto (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    fecha_creacion DATE NOT NULL,
    id_tipo_producto INT NOT NULL,
    id_artesano INT NOT NULL,
    descripcion VARCHAR(1000) NOT NULL DEFAULT '',
    material VARCHAR(200) NOT NULL DEFAULT '',
    precio DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (precio >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    imagen VARCHAR(500) NOT NULL DEFAULT '',
    CONSTRAINT fk_tipo_producto FOREIGN KEY (id_tipo_producto)
        REFERENCES tipo_producto(id),
    CONSTRAINT fk_artesano FOREIGN KEY (id_artesano) REFERENCES artesano(id)
    );

CREATE TABLE IF NOT EXISTS cliente (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE CHECK (REGEXP_LIKE(dni, '^[0-9]{8}$')),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL DEFAULT '',
    telefono VARCHAR(20) NOT NULL,
    fecha_registro DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS usuario_admin (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    hash_clave VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS descuento (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_producto INT NOT NULL REFERENCES producto(id),
    nombre VARCHAR(100) NOT NULL,
    porcentaje DECIMAL(5,2) NOT NULL CHECK (porcentaje > 0 AND porcentaje < 100),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL CHECK (fecha_fin >= fecha_inicio),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_cliente INT NOT NULL REFERENCES cliente(id),
    fecha TIMESTAMP NOT NULL,
    estado VARCHAR(30) NOT NULL CHECK (estado IN ('PENDIENTE_PAGO', 'CONFIRMADO', 'EN_PREPARACION', 'LISTO_ENTREGA', 'ENTREGADO')),
    total DECIMAL(10,2) NOT NULL CHECK (total > 0),
    metodo_pago VARCHAR(30) NOT NULL CHECK (metodo_pago IN ('YAPE', 'TRANSFERENCIA', 'EFECTIVO')),
    metodo_entrega VARCHAR(20) NOT NULL CHECK (metodo_entrega IN ('RECOJO', 'ENVIO')),
    provincia_entrega VARCHAR(100) NOT NULL DEFAULT '',
    direccion_entrega VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pedido INT NOT NULL REFERENCES pedido(id),
    id_producto INT NOT NULL REFERENCES producto(id),
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_base DECIMAL(10,2) NOT NULL CHECK (precio_base > 0),
    porcentaje_descuento DECIMAL(5,2) NOT NULL CHECK (porcentaje_descuento >= 0 AND porcentaje_descuento < 100),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario > 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal > 0),
    UNIQUE (id_pedido, id_producto)
);

CREATE TABLE IF NOT EXISTS pago (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pedido INT NOT NULL UNIQUE REFERENCES pedido(id),
    metodo VARCHAR(30) NOT NULL CHECK (metodo IN ('YAPE', 'TRANSFERENCIA', 'EFECTIVO')),
    monto DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    fecha_pago TIMESTAMP NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado = 'CONFIRMADO'),
    referencia VARCHAR(100) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS seguimiento_pedido (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pedido INT NOT NULL REFERENCES pedido(id),
    id_usuario INT REFERENCES usuario_admin(id),
    estado VARCHAR(30) NOT NULL CHECK (estado IN ('PENDIENTE_PAGO', 'CONFIRMADO', 'EN_PREPARACION', 'LISTO_ENTREGA', 'ENTREGADO')),
    fecha TIMESTAMP NOT NULL,
    observacion VARCHAR(500) NOT NULL DEFAULT ''
);


