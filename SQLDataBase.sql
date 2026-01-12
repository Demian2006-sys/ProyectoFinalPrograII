SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP SCHEMA IF EXISTS `adminus`;
CREATE SCHEMA IF NOT EXISTS `adminus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `adminus`;

CREATE TABLE IF NOT EXISTS `persona` (
  `cedula` VARCHAR(20) NOT NULL,
  `nombre_completo` VARCHAR(100) NOT NULL,
  `edad` INT NOT NULL,
  `genero` VARCHAR(10) NOT NULL,
  `direccion` VARCHAR(255) NOT NULL,
  `telefono` BIGINT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`cedula`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `rol` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `nombre_rol` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `rolusuario` (
  `id_asignacion` INT NOT NULL AUTO_INCREMENT,
  `usuario_cedula` VARCHAR(20) NOT NULL,
  `rol_id` INT NOT NULL,
  PRIMARY KEY (`id_asignacion`),
  INDEX `idx_usuario_cedula` (`usuario_cedula`),
  INDEX `idx_rol_id` (`rol_id`),
  CONSTRAINT `fk_rolusuario_persona`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `persona` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rolusuario_rol`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rol` (`id_rol`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `asignatura` (
  `id_asignatura` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `codigo` VARCHAR(20) NOT NULL,
  `horas_semanales` INT NOT NULL,
  PRIMARY KEY (`id_asignatura`),
  UNIQUE INDEX `idx_codigo` (`codigo`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `curso` (
  `id_curso` INT NOT NULL AUTO_INCREMENT,
  `nivel` VARCHAR(50) NOT NULL,
  `paralelo` VARCHAR(10) NOT NULL,
  `periodo_academico` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_curso`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `estudiante` (
  `cedula` VARCHAR(20) NOT NULL,
  `nivel_educativo` VARCHAR(50) NOT NULL,
  `paralelo` VARCHAR(10) NOT NULL,
  `representante` VARCHAR(100) NOT NULL,
  `curso_id` INT NULL,
  PRIMARY KEY (`cedula`),
  INDEX `idx_curso` (`curso_id`),
  CONSTRAINT `fk_estudiante_persona`
    FOREIGN KEY (`cedula`)
    REFERENCES `persona` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_estudiante_curso`
    FOREIGN KEY (`curso_id`)
    REFERENCES `curso` (`id_curso`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `docente` (
  `cedula` VARCHAR(20) NOT NULL,
  `especialidad` VARCHAR(50) NOT NULL,
  `titulo_academico` VARCHAR(100) NOT NULL,
  `jornada_laboral` VARCHAR(20) NOT NULL,
  `sueldo_mensual` DECIMAL(10,2) NOT NULL,
  `carga_horaria` INT NOT NULL,
  `horario_clases` VARCHAR(100) NULL,
  `asignatura_id` INT NULL,
  PRIMARY KEY (`cedula`),
  INDEX `idx_asignatura` (`asignatura_id`),
  CONSTRAINT `fk_docente_persona`
    FOREIGN KEY (`cedula`)
    REFERENCES `persona` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_docente_asignatura`
    FOREIGN KEY (`asignatura_id`)
    REFERENCES `asignatura` (`id_asignatura`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `administrativo` (
  `cedula` VARCHAR(20) NOT NULL,
  `cargo` VARCHAR(50) NOT NULL,
  `area` VARCHAR(100) NOT NULL,
  `jornada_laboral` VARCHAR(20) NOT NULL,
  `horas_trabajadas` INT NOT NULL,
  `sueldo` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`cedula`),
  CONSTRAINT `fk_administrativo_persona`
    FOREIGN KEY (`cedula`)
    REFERENCES `persona` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `estadoempleabilidad` (
  `id_estado` INT NOT NULL AUTO_INCREMENT,
  `cedula` VARCHAR(20) NOT NULL,
  `estado` VARCHAR(45) NOT NULL,
  `fecha_actualizacion` DATE NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (`id_estado`),
  INDEX `idx_cedula` (`cedula`),
  CONSTRAINT `fk_estadoempleabilidad_persona`
    FOREIGN KEY (`cedula`)
    REFERENCES `persona` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `inscripcion` (
  `id_inscripcion` INT NOT NULL AUTO_INCREMENT,
  `estudiante_cedula` VARCHAR(20) NOT NULL,
  `asignatura_id` INT NOT NULL,
  `fecha_inscripcion` DATE NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (`id_inscripcion`),
  UNIQUE INDEX `idx_estudiante_asignatura` (`estudiante_cedula`, `asignatura_id`),
  INDEX `idx_asignatura` (`asignatura_id`),
  CONSTRAINT `fk_inscripcion_estudiante`
    FOREIGN KEY (`estudiante_cedula`)
    REFERENCES `estudiante` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_inscripcion_asignatura`
    FOREIGN KEY (`asignatura_id`)
    REFERENCES `asignatura` (`id_asignatura`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `docente_materia` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `docente_cedula` VARCHAR(20) NOT NULL,
  `materia_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_docente_materia` (`docente_cedula`, `materia_id`),
  INDEX `idx_materia` (`materia_id`),
  CONSTRAINT `fk_docente_materia_docente`
    FOREIGN KEY (`docente_cedula`)
    REFERENCES `docente` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_docente_materia_asignatura`
    FOREIGN KEY (`materia_id`)
    REFERENCES `asignatura` (`id_asignatura`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `asistencia` (
  `id_asistencia` INT NOT NULL AUTO_INCREMENT,
  `estudiante_cedula` VARCHAR(20) NOT NULL,
  `asignatura_id` INT NOT NULL,
  `curso_id` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `estado` ENUM('Presente', 'Ausente', 'Tarde') NOT NULL,
  PRIMARY KEY (`id_asistencia`),
  UNIQUE INDEX `idx_asistencia_unica` (`estudiante_cedula`, `asignatura_id`, `fecha`),
  INDEX `idx_asignatura` (`asignatura_id`),
  INDEX `idx_curso` (`curso_id`),
  CONSTRAINT `fk_asistencia_estudiante`
    FOREIGN KEY (`estudiante_cedula`)
    REFERENCES `estudiante` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_asistencia_asignatura`
    FOREIGN KEY (`asignatura_id`)
    REFERENCES `asignatura` (`id_asignatura`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_asistencia_curso`
    FOREIGN KEY (`curso_id`)
    REFERENCES `curso` (`id_curso`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `notas` (
  `id_nota` INT NOT NULL AUTO_INCREMENT,
  `estudiante_cedula` VARCHAR(20) NOT NULL,
  `asignatura_id` INT NOT NULL,
  `parcial` int NOT NULL,
  `nota` DECIMAL(5,2) NOT NULL,
  `fecha_registro` DATE NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (`id_nota`),
  UNIQUE INDEX `idx_nota_unica` (`estudiante_cedula`, `asignatura_id`),
  INDEX `idx_asignatura` (`asignatura_id`),
  CONSTRAINT `fk_notas_estudiante`
    FOREIGN KEY (`estudiante_cedula`)
    REFERENCES `estudiante` (`cedula`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_notas_asignatura`
    FOREIGN KEY (`asignatura_id`)
    REFERENCES `asignatura` (`id_asignatura`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_nota_rango`
    CHECK (`nota` >= 0 AND `nota` <= 10)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Datos iniciales
INSERT INTO `rol` (`nombre_rol`) VALUES 
  ('Estudiante'),
  ('Docente'),
  ('Administrativo');

INSERT INTO `asignatura` (`nombre`, `codigo`, `horas_semanales`) VALUES
  ('Matemáticas', 'MAT001', 5),
  ('Física', 'FIS001', 4),
  ('Ciencias Naturales', 'CNA001', 4),
  ('Lengua y Literatura', 'LEN001', 5),
  ('Historia', 'HIS001', 3),
  ('Educación Física', 'EDF001', 2);

INSERT INTO `curso` (`nivel`, `paralelo`, `periodo_academico`) VALUES
  ('Bachillerato', 'A', '2025-2026'),
  ('Bachillerato', 'B', '2025-2026'),
  ('Basica', 'A', '2025-2026'),
  ('Basica', 'B', '2025-2026');

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS; 