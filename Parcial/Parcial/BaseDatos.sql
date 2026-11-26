CREATE DATABASE IF NOT EXISTS LabLibre;
USE LabLibre;
CREATE TABLE materiales (
    id_material INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,

    tipo ENUM('LIBRO', 'PORTATIL', 'KIT') NOT NULL,

    valor_comercial DECIMAL(12,2) NULL,
    numero_componentes INT NULL,

    -- Validación de atributos según el tipo
    CONSTRAINT chk_datos_material CHECK (
        (tipo = 'LIBRO'
            AND valor_comercial IS NULL
            AND numero_componentes IS NULL)

        OR

        (tipo = 'PORTATIL'
            AND valor_comercial > 0
            AND numero_componentes IS NULL)

        OR

        (tipo = 'KIT'
            AND valor_comercial IS NULL
            AND numero_componentes BETWEEN 1 AND 50)
    )
);

CREATE TABLE prestamos (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,

    id_material INT NOT NULL,

    documento VARCHAR(15) NOT NULL,

    fecha_prestamo DATE NOT NULL,
    fecha_limite DATE NOT NULL,
    fecha_devolucion DATE NULL,

    componentes_faltantes INT NULL,

    politica_multa ENUM(
        'NORMAL',
        'DOCENTE',
        'SEMANA_EXAMENES'
    ) NOT NULL DEFAULT 'NORMAL',

    seguro DECIMAL(12,2) NOT NULL DEFAULT 0,
    multa_base DECIMAL(12,2) NOT NULL DEFAULT 0,
    multa_final DECIMAL(12,2) NOT NULL DEFAULT 0,

    -- Relación con materiales
    CONSTRAINT fk_prestamo_material
        FOREIGN KEY (id_material)
        REFERENCES materiales(id_material),

    CONSTRAINT chk_fecha_limite
        CHECK (fecha_limite > fecha_prestamo),

    CONSTRAINT chk_fecha_devolucion
        CHECK (
            fecha_devolucion IS NULL
            OR fecha_devolucion >= fecha_prestamo
        ),

    CONSTRAINT chk_componentes_faltantes
        CHECK (
            componentes_faltantes IS NULL
            OR componentes_faltantes >= 0
        ),

    CONSTRAINT chk_valores_prestamo
        CHECK (
            seguro >= 0
            AND multa_base >= 0
            AND multa_final >= 0
        )
);

CREATE INDEX idx_prestamo_material
ON prestamos(id_material);

CREATE INDEX idx_prestamo_fechas
ON prestamos(fecha_limite, fecha_devolucion);

CREATE INDEX idx_prestamo_documento
ON prestamos(documento);
