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
    fechaCreacion date not null
);

CREATE TABLE IF NOT EXISTS producto (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    fecha_creacion DATE NOT NULL,
    id_tipo_producto INT NOT NULL,
    descripcion VARCHAR(1000) NOT NULL DEFAULT '',
    material VARCHAR(200) NOT NULL DEFAULT '',
    precio DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (precio >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    imagen VARCHAR(500) NOT NULL DEFAULT '',
    CONSTRAINT fk_tipo_producto FOREIGN KEY (id_tipo_producto)
        REFERENCES tipo_producto(id)
    );


