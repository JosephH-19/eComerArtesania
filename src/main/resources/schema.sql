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
    CONSTRAINT fk_tipo_producto FOREIGN KEY (id_tipo_producto) 
    REFERENCES tipo_producto(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS artesano (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    telefono VARCHAR(9),
    correo_electronico VARCHAR(150),
    edad INT,
    anos_exp INT,
    productos_elaborados INT DEFAULT 0,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    imagen VARCHAR(500),

    CONSTRAINT chk_artesano_edad
    CHECK (edad IS NULL OR edad >= 0),

    CONSTRAINT chk_artesano_experiencia
    CHECK (anos_exp IS NULL OR anos_exp >= 0),

    CONSTRAINT chk_artesano_productos
    CHECK (productos_elaborados >= 0)
    );


