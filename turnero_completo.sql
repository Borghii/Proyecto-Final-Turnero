-- Script limpio para crear la base de datos del sistema Turnero.
-- Preparado para ejecutar en MySQL / phpMyAdmin.

DROP DATABASE IF EXISTS turnero;
CREATE DATABASE turnero
	CHARACTER SET utf8mb4
	COLLATE utf8mb4_unicode_ci;

USE turnero;

CREATE TABLE roles (
	id_rol INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios (
	id_usuario INT PRIMARY KEY AUTO_INCREMENT,
	mail VARCHAR(150) NOT NULL UNIQUE,
	pass VARCHAR(100) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	id_rol INT NOT NULL,
	INDEX idx_usuarios_rol (id_rol),
	CONSTRAINT fk_usuarios_roles
		FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE negocios (
	id_negocio INT PRIMARY KEY AUTO_INCREMENT,
	ciudad VARCHAR(100) NOT NULL,
	nombre VARCHAR(150) NOT NULL,
	id_usuario INT NOT NULL UNIQUE,
	CONSTRAINT fk_negocios_usuarios
		FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE horarios (
	id_horario INT PRIMARY KEY AUTO_INCREMENT,
	id_negocio INT NOT NULL,
	dia TINYINT NOT NULL,
	manana_desde TIME NULL,
	manana_hasta TIME NULL,
	tarde_desde TIME NULL,
	tarde_hasta TIME NULL,
	activo BOOLEAN NOT NULL DEFAULT TRUE,
	UNIQUE KEY uk_horarios_negocio_dia (id_negocio, dia),
	INDEX idx_horarios_negocio_activo (id_negocio, activo),
	CONSTRAINT chk_horarios_dia_valido CHECK (dia BETWEEN 1 AND 7),
	CONSTRAINT chk_horarios_manana_completa CHECK (
		(manana_desde IS NULL AND manana_hasta IS NULL)
		OR (manana_desde IS NOT NULL AND manana_hasta IS NOT NULL AND manana_hasta > manana_desde)
	),
	CONSTRAINT chk_horarios_tarde_completa CHECK (
		(tarde_desde IS NULL AND tarde_hasta IS NULL)
		OR (tarde_desde IS NOT NULL AND tarde_hasta IS NOT NULL AND tarde_hasta > tarde_desde)
	),
	CONSTRAINT chk_horarios_bloques_ordenados CHECK (
		manana_hasta IS NULL
		OR tarde_desde IS NULL
		OR tarde_desde >= manana_hasta
	),
	CONSTRAINT fk_horarios_negocios
		FOREIGN KEY (id_negocio) REFERENCES negocios(id_negocio)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE servicios (
	id_servicio INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(150) NOT NULL,
	precio DECIMAL(10, 2) NOT NULL,
	duracion INT NOT NULL,
	id_negocio INT NOT NULL,
	activo BOOLEAN NOT NULL DEFAULT TRUE,
	INDEX idx_servicios_negocio (id_negocio),
	INDEX idx_servicios_activo (activo),
	INDEX idx_servicios_negocio_nombre_activo (id_negocio, nombre, activo),
	CONSTRAINT chk_servicios_precio_no_negativo CHECK (precio >= 0),
	CONSTRAINT chk_servicios_duracion_positiva CHECK (duracion > 0),
	CONSTRAINT chk_servicios_duracion_media_hora CHECK (MOD(duracion, 30) = 0),
	CONSTRAINT fk_servicios_negocios
		FOREIGN KEY (id_negocio) REFERENCES negocios(id_negocio)
		ON UPDATE CASCADE
		ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE turnos (
	id_turno INT PRIMARY KEY AUTO_INCREMENT,
	id_servicio INT NOT NULL,
	id_usuario INT NOT NULL,
	fecha DATE NOT NULL,
	hora_inicio TIME NOT NULL,
	hora_fin TIME NOT NULL,
	estado VARCHAR(30) NOT NULL DEFAULT 'CONFIRMADO',
	INDEX idx_turnos_servicio_fecha_inicio (id_servicio, fecha, hora_inicio),
	INDEX idx_turnos_usuario_fecha (id_usuario, fecha),
	INDEX idx_turnos_fecha (fecha),
	INDEX idx_turnos_estado (estado),
	CONSTRAINT chk_turnos_hora_fin_posterior CHECK (hora_fin > hora_inicio),
	CONSTRAINT chk_turnos_estado_valido CHECK (estado IN ('CONFIRMADO', 'CANCELADO')),
	CONSTRAINT fk_turnos_servicios
		FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio)
		ON UPDATE CASCADE
		ON DELETE RESTRICT,
	CONSTRAINT fk_turnos_usuarios
		FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (id_rol, nombre) VALUES
	(1, 'CLIENTE'),
	(2, 'ADMIN');

INSERT INTO usuarios (id_usuario, mail, pass, nombre, id_rol) VALUES
	(1, 'admin@barberiaurbana.com', 'admin123', 'Martin Suarez', 2),
	(2, 'admin@estilocentro.com', 'admin123', 'Carla Benitez', 2),
	(3, 'admin@glamstudio.com', 'admin123', 'Rocio Medina', 2),
	(4, 'admin@cortedeluxe.com', 'admin123', 'Diego Alvarez', 2),
	(5, 'admin@barbados.com', 'admin123', 'Nicolas Romero', 2),
	(6, 'admin@bellezanorte.com', 'admin123', 'Valeria Torres', 2),
	(101, 'juan.perez@cliente.com', 'cliente123', 'Juan Perez', 1),
	(102, 'maria.gomez@cliente.com', 'cliente123', 'Maria Gomez', 1),
	(103, 'lucas.fernandez@cliente.com', 'cliente123', 'Lucas Fernandez', 1),
	(104, 'sofia.martinez@cliente.com', 'cliente123', 'Sofia Martinez', 1),
	(105, 'camila.rodriguez@cliente.com', 'cliente123', 'Camila Rodriguez', 1),
	(106, 'tomas.garcia@cliente.com', 'cliente123', 'Tomas Garcia', 1),
	(107, 'valentina.lopez@cliente.com', 'cliente123', 'Valentina Lopez', 1),
	(108, 'agustin.fernandez@cliente.com', 'cliente123', 'Agustin Fernandez', 1),
	(109, 'julieta.diaz@cliente.com', 'cliente123', 'Julieta Diaz', 1),
	(110, 'bruno.sosa@cliente.com', 'cliente123', 'Bruno Sosa', 1),
	(111, 'martina.acosta@cliente.com', 'cliente123', 'Martina Acosta', 1),
	(112, 'facundo.arias@cliente.com', 'cliente123', 'Facundo Arias', 1);

INSERT INTO negocios (id_negocio, ciudad, nombre, id_usuario) VALUES
	(1, 'Buenos Aires', 'Barberia Urbana', 1),
	(2, 'Rosario', 'Estilo Centro', 2),
	(3, 'Cordoba', 'Glam Studio', 3),
	(4, 'La Plata', 'Corte Deluxe', 4),
	(5, 'Mendoza', 'Barba Dos', 5),
	(6, 'San Miguel de Tucuman', 'Belleza Norte', 6);

INSERT INTO horarios (id_negocio, dia, manana_desde, manana_hasta, tarde_desde, tarde_hasta, activo)
SELECT n.id_negocio, d.dia, '09:00:00', '12:00:00', '15:00:00', '20:00:00', TRUE
FROM negocios n
JOIN (
	SELECT 1 AS dia UNION ALL
	SELECT 2 UNION ALL
	SELECT 3 UNION ALL
	SELECT 4 UNION ALL
	SELECT 5 UNION ALL
	SELECT 6 UNION ALL
	SELECT 7
) d;

INSERT INTO servicios (id_servicio, nombre, precio, duracion, id_negocio, activo) VALUES
	(1, 'Corte clasico', 8000.00, 30, 1, TRUE),
	(2, 'Corte y barba', 13000.00, 60, 1, TRUE),
	(3, 'Perfilado de barba', 6000.00, 30, 1, TRUE),
	(4, 'Color hombre', 18000.00, 90, 1, TRUE),
	(5, 'Corte dama', 12000.00, 60, 2, TRUE),
	(6, 'Brushing', 9000.00, 30, 2, TRUE),
	(7, 'Tintura de raiz', 22000.00, 90, 2, TRUE),
	(8, 'Peinado para evento', 18000.00, 60, 2, TRUE),
	(9, 'Manicuria express', 7000.00, 30, 3, TRUE),
	(10, 'Belleza de manos', 10000.00, 60, 3, TRUE),
	(11, 'Limpieza facial', 15000.00, 60, 3, TRUE),
	(12, 'Perfilado de cejas', 5000.00, 30, 3, TRUE),
	(13, 'Corte premium', 11000.00, 30, 4, TRUE),
	(14, 'Barba premium', 8000.00, 30, 4, TRUE),
	(15, 'Corte de nino', 7000.00, 30, 4, TRUE),
	(16, 'Alisado corto', 28000.00, 120, 4, TRUE),
	(17, 'Afeitado tradicional', 9000.00, 30, 5, TRUE),
	(18, 'Corte fade', 10000.00, 30, 5, TRUE),
	(19, 'Combo ejecutivo', 16000.00, 60, 5, TRUE),
	(20, 'Tratamiento capilar', 14000.00, 60, 5, TRUE),
	(21, 'Corte mujer', 13000.00, 60, 6, TRUE),
	(22, 'Lavado y brushing', 10000.00, 30, 6, TRUE),
	(23, 'Nutricion capilar', 20000.00, 90, 6, TRUE),
	(24, 'Depilacion facial', 7000.00, 30, 6, TRUE);

SET @hoy := CURRENT_DATE();
SET @ayer := DATE_SUB(@hoy, INTERVAL 1 DAY);
SET @dos_dias_atras := DATE_SUB(@hoy, INTERVAL 2 DAY);
SET @tres_dias_atras := DATE_SUB(@hoy, INTERVAL 3 DAY);
SET @cuatro_dias_atras := DATE_SUB(@hoy, INTERVAL 4 DAY);
SET @manana := DATE_ADD(@hoy, INTERVAL 1 DAY);
SET @pasado_manana := DATE_ADD(@hoy, INTERVAL 2 DAY);
SET @tres_dias := DATE_ADD(@hoy, INTERVAL 3 DAY);
SET @cuatro_dias := DATE_ADD(@hoy, INTERVAL 4 DAY);

INSERT INTO turnos (id_servicio, id_usuario, fecha, hora_inicio, hora_fin, estado) VALUES
	(1, 101, @cuatro_dias_atras, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(2, 102, @cuatro_dias_atras, '10:00:00', '11:00:00', 'CONFIRMADO'),
	(3, 103, @cuatro_dias_atras, '15:00:00', '15:30:00', 'CANCELADO'),
	(4, 104, @cuatro_dias_atras, '16:00:00', '17:30:00', 'CONFIRMADO'),
	(5, 105, @cuatro_dias_atras, '09:00:00', '10:00:00', 'CONFIRMADO'),
	(6, 106, @cuatro_dias_atras, '10:30:00', '11:00:00', 'CONFIRMADO'),
	(7, 107, @cuatro_dias_atras, '15:00:00', '16:30:00', 'CONFIRMADO'),
	(8, 108, @cuatro_dias_atras, '17:00:00', '18:00:00', 'CANCELADO'),
	(9, 109, @cuatro_dias_atras, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(10, 110, @cuatro_dias_atras, '10:00:00', '11:00:00', 'CONFIRMADO'),
	(11, 111, @cuatro_dias_atras, '15:00:00', '16:00:00', 'CONFIRMADO'),
	(12, 112, @cuatro_dias_atras, '17:00:00', '17:30:00', 'CONFIRMADO'),
	(13, 101, @tres_dias_atras, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(14, 102, @tres_dias_atras, '09:30:00', '10:00:00', 'CONFIRMADO'),
	(15, 103, @tres_dias_atras, '10:00:00', '10:30:00', 'CONFIRMADO'),
	(16, 104, @tres_dias_atras, '15:00:00', '17:00:00', 'CANCELADO'),
	(17, 105, @tres_dias_atras, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(18, 106, @tres_dias_atras, '10:00:00', '10:30:00', 'CONFIRMADO'),
	(19, 107, @tres_dias_atras, '15:00:00', '16:00:00', 'CONFIRMADO'),
	(20, 108, @tres_dias_atras, '16:30:00', '17:30:00', 'CONFIRMADO'),
	(21, 109, @tres_dias_atras, '09:00:00', '10:00:00', 'CONFIRMADO'),
	(22, 110, @tres_dias_atras, '10:30:00', '11:00:00', 'CONFIRMADO'),
	(23, 111, @tres_dias_atras, '15:00:00', '16:30:00', 'CONFIRMADO'),
	(24, 112, @tres_dias_atras, '17:00:00', '17:30:00', 'CONFIRMADO'),
	(1, 105, @dos_dias_atras, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(2, 106, @dos_dias_atras, '10:00:00', '11:00:00', 'CONFIRMADO'),
	(3, 107, @dos_dias_atras, '15:00:00', '15:30:00', 'CONFIRMADO'),
	(4, 108, @dos_dias_atras, '16:00:00', '17:30:00', 'CONFIRMADO'),
	(5, 109, @dos_dias_atras, '09:00:00', '10:00:00', 'CONFIRMADO'),
	(6, 110, @dos_dias_atras, '10:30:00', '11:00:00', 'CONFIRMADO'),
	(7, 111, @dos_dias_atras, '15:00:00', '16:30:00', 'CANCELADO'),
	(8, 112, @dos_dias_atras, '17:00:00', '18:00:00', 'CONFIRMADO'),
	(9, 101, @ayer, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(10, 102, @ayer, '10:00:00', '11:00:00', 'CONFIRMADO'),
	(11, 103, @ayer, '15:00:00', '16:00:00', 'CONFIRMADO'),
	(12, 104, @ayer, '17:00:00', '17:30:00', 'CANCELADO'),
	(1, 101, @manana, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(2, 102, @manana, '10:00:00', '11:00:00', 'CONFIRMADO'),
	(3, 103, @manana, '11:00:00', '11:30:00', 'CONFIRMADO'),
	(4, 104, @manana, '16:00:00', '17:30:00', 'CONFIRMADO'),
	(5, 105, @manana, '09:00:00', '10:00:00', 'CONFIRMADO'),
	(6, 106, @manana, '10:30:00', '11:00:00', 'CONFIRMADO'),
	(7, 107, @manana, '15:00:00', '16:30:00', 'CONFIRMADO'),
	(8, 108, @manana, '17:00:00', '18:00:00', 'CONFIRMADO'),
	(9, 109, @manana, '09:30:00', '10:00:00', 'CONFIRMADO'),
	(10, 110, @manana, '10:30:00', '11:30:00', 'CONFIRMADO'),
	(11, 111, @manana, '15:00:00', '16:00:00', 'CONFIRMADO'),
	(12, 112, @manana, '17:00:00', '17:30:00', 'CONFIRMADO'),
	(13, 101, @pasado_manana, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(14, 102, @pasado_manana, '09:30:00', '10:00:00', 'CONFIRMADO'),
	(15, 103, @pasado_manana, '10:00:00', '10:30:00', 'CONFIRMADO'),
	(16, 104, @pasado_manana, '15:00:00', '17:00:00', 'CONFIRMADO'),
	(17, 105, @pasado_manana, '09:00:00', '09:30:00', 'CONFIRMADO'),
	(18, 106, @pasado_manana, '10:00:00', '10:30:00', 'CONFIRMADO'),
	(19, 107, @pasado_manana, '15:00:00', '16:00:00', 'CONFIRMADO'),
	(20, 108, @pasado_manana, '16:30:00', '17:30:00', 'CONFIRMADO'),
	(21, 109, @tres_dias, '09:00:00', '10:00:00', 'CONFIRMADO'),
	(22, 110, @tres_dias, '10:30:00', '11:00:00', 'CONFIRMADO'),
	(23, 111, @tres_dias, '15:00:00', '16:30:00', 'CONFIRMADO'),
	(24, 112, @tres_dias, '17:00:00', '17:30:00', 'CONFIRMADO'),
	(1, 110, @cuatro_dias, '09:00:00', '09:30:00', 'CANCELADO'),
	(7, 102, @cuatro_dias, '15:00:00', '16:30:00', 'CANCELADO');
