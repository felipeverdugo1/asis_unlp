




-- Tabla: usuarios
INSERT INTO usuarios  (id, email, habilitado, nombre_usuario, password, rol) VALUES
                                                                                 (1, 'admin@example.com', 1, 'admin', 'pass123', 'ADMINISTRADOR'),
                                                                                 (2, 'salud1@example.com', 1, 'salud1', 'pass123', 'PERSONAL_DE_SALUD'),
                                                                                 (3, 'referente1@example.com', 1, 'referente1', 'pass123', 'REFERENTE_ORGANIZACION_SOCIAL'),
                                                                                 (4, 'visitador1@example.com', 1, 'visitador1', 'pass123', 'VISITANTE'),
                                                                                 (5, 'admin2@example.com', 1, 'admin2', 'pass123', 'ADMINISTRADOR'),
                                                                                 (6, 'salud2@example.com', 1, 'salud2', 'pass123', 'PERSONAL_DE_SALUD'),
                                                                                 (7, 'referente2@example.com', 1, 'referente2', 'pass123', 'REFERENTE_ORGANIZACION_SOCIAL'),
                                                                                 (8, 'visitador2@example.com', 1, 'visitador2', 'pass123', 'VISITANTE'),
                                                                                 (9, 'test@example.com', 1, 'testuser', 'pass123', 'VISITANTE'),
                                                                                 (10, 'demo@example.com', 1, 'demouser', 'pass123', 'ADMINISTRADOR');

-- Tabla: barrios
INSERT INTO barrios (id, geolocalizacion, informacion, nombre) VALUES
                                                                   (1, 'loc1', 'info1', 'Barrio A'),
                                                                   (2, 'loc2', 'info2', 'Barrio B'),
                                                                   (3, 'loc3', 'info3', 'Barrio C'),
                                                                   (4, 'loc4', 'info4', 'Barrio D'),
                                                                   (5, 'loc5', 'info5', 'Barrio E'),
                                                                   (6, 'loc6', 'info6', 'Barrio F'),
                                                                   (7, 'loc7', 'info7', 'Barrio G'),
                                                                   (8, 'loc8', 'info8', 'Barrio H'),
                                                                   (9, 'loc9', 'info9', 'Barrio I'),
                                                                   (10, 'loc10', 'info10', 'Barrio J');

-- Tabla: organizaciones_sociales
INSERT INTO organizaciones_sociales (id, actividad_principal, domicilio, nombre, barrio_id) VALUES
                                                                                                (1, 'Educación', 'Calle 1', 'Org A', 1),
                                                                                                (2, 'Salud', 'Calle 2', 'Org B', 2),
                                                                                                (3, 'Deporte', 'Calle 3', 'Org C', 3),
                                                                                                (4, 'Arte', 'Calle 4', 'Org D', 4),
                                                                                                (5, 'Cultura', 'Calle 5', 'Org E', 5),
                                                                                                (6, 'Asistencia', 'Calle 6', 'Org F', 6),
                                                                                                (7, 'Empleo', 'Calle 7', 'Org G', 7),
                                                                                                (8, 'Comida', 'Calle 8', 'Org H', 8),
                                                                                                (9, 'Salud', 'Calle 9', 'Org I', 9),
                                                                                                (10, 'Educación', 'Calle 10', 'Org J', 10);

-- Tabla: campanas
INSERT INTO campanias  (id, fecha_inicio, fecha_fin, nombre, barrio_id) VALUES
                                                                            (1, '2025-01-01', '2025-01-10', 'Campaña A', 1),
                                                                            (2, '2025-01-11', '2025-01-20', 'Campaña B', 2),
                                                                            (3, '2025-01-21', '2025-01-30', 'Campaña C', 3),
                                                                            (4, '2025-02-01', '2025-02-10', 'Campaña D', 4),
                                                                            (5, '2025-02-11', '2025-02-20', 'Campaña E', 5),
                                                                            (6, '2025-02-21', '2025-02-28', 'Campaña F', 6),
                                                                            (7, '2025-03-01', '2025-03-10', 'Campaña G', 7),
                                                                            (8, '2025-03-11', '2025-03-20', 'Campaña H', 8),
                                                                            (9, '2025-03-21', '2025-03-30', 'Campaña I', 9),
                                                                            (10, '2025-04-01', '2025-04-10', 'Campaña J', 10);

-- Tabla: jornadas
INSERT INTO jornadas (id, fecha_inicio, fecha_fin, campana_id) VALUES
                                                                   (1, '2025-01-01', '2025-01-03', 1),
                                                                   (2, '2025-01-04', '2025-01-06', 2),
                                                                   (3, '2025-01-07', '2025-01-09', 3),
                                                                   (4, '2025-01-10', '2025-01-12', 4),
                                                                   (5, '2025-01-13', '2025-01-15', 5),
                                                                   (6, '2025-01-16', '2025-01-18', 6),
                                                                   (7, '2025-01-19', '2025-01-21', 7),
                                                                   (8, '2025-01-22', '2025-01-24', 8),
                                                                   (9, '2025-01-25', '2025-01-27', 9),
                                                                   (10, '2025-01-28', '2025-01-30', 10);

-- Tabla: zonas
INSERT INTO zonas (id, geolocalizacion, nombre, barrio_id) VALUES
                                                               (1, 'geo1', 'Zona A', 1),
                                                               (2, 'geo2', 'Zona B', 2),
                                                               (3, 'geo3', 'Zona C', 3),
                                                               (4, 'geo4', 'Zona D', 4),
                                                               (5, 'geo5', 'Zona E', 5),
                                                               (6, 'geo6', 'Zona F', 6),
                                                               (7, 'geo7', 'Zona G', 7),
                                                               (8, 'geo8', 'Zona H', 8),
                                                               (9, 'geo9', 'Zona I', 9),
                                                               (10, 'geo10', 'Zona J', 10);

-- Tabla: encuestador
INSERT INTO encuestadores (id, dni, edad, genero, nombre, ocupacion) VALUES
                                                                         (1, '1001', 30, 'M', 'Carlos', 'Doctor'),
                                                                         (2, '1002', 25, 'F', 'Ana', 'Enfermera'),
                                                                         (3, '1003', 28, 'M', 'Luis', 'Promotor'),
                                                                         (4, '1004', 35, 'F', 'Laura', 'Trabajadora Social'),
                                                                         (5, '1005', 22, 'M', 'Pedro', 'Estudiante'),
                                                                         (6, '1006', 31, 'F', 'Julia', 'Psicóloga'),
                                                                         (7, '1007', 29, 'M', 'Juan', 'Paramédico'),
                                                                         (8, '1008', 26, 'F', 'María', 'Nutricionista'),
                                                                         (9, '1009', 27, 'M', 'Diego', 'Docente'),
                                                                         (10, '1010', 24, 'F', 'Lucía', 'Técnica');

-- Tabla: encuesta
INSERT INTO encuestas (id, fecha, encuestador_id, jornada_id, zona_id) VALUES
                                                                           (1, '2025-01-01', 1, 1, 1),
                                                                           (2, '2025-01-02', 2, 2, 2),
                                                                           (3, '2025-01-03', 3, 3, 3),
                                                                           (4, '2025-01-04', 4, 4, 4),
                                                                           (5, '2025-01-05', 5, 5, 5),
                                                                           (6, '2025-01-06', 6, 6, 6),
                                                                           (7, '2025-01-07', 7, 7, 7),
                                                                           (8, '2025-01-08', 8, 8, 8),
                                                                           (9, '2025-01-09', 9, 9, 9),
                                                                           (10, '2025-01-10', 10, 10, 10);

-- Tabla: pregunta
INSERT INTO preguntas (id, pregunta, respuesta, tipo, encuesta_id) VALUES
                                                                       (1, '¿Tiene fiebre?', 'Sí', 'booleano', 1),
                                                                       (2, '¿Tiene tos?', 'No', 'booleano', 2),
                                                                       (3, '¿Edad del paciente?', '35', 'número', 3),
                                                                       (4, '¿Está vacunado?', 'Sí', 'booleano', 4),
                                                                       (5, '¿Tiene síntomas?', 'Sí', 'texto', 5),
                                                                       (6, '¿Dónde vive?', 'Barrio A', 'texto', 6),
                                                                       (7, '¿Tiene contacto con enfermos?', 'No', 'booleano', 7),
                                                                       (8, '¿Qué síntomas presenta?', 'Dolor de cabeza', 'texto', 8),
                                                                       (9, '¿Cuántos días con síntomas?', '3', 'número', 9),
                                                                       (10, '¿Requiere atención médica?', 'Sí', 'booleano', 10);

-- Tabla: reportes
INSERT INTO reportes (id, descripcion, fecha_creacion, nombre, compartido_con, creado_por, campaña_id) VALUES
                                                                                                           (1, 'Reporte A', '2025-01-01', 'Reporte 1', 2, 1, 1),
                                                                                                           (2, 'Reporte B', '2025-01-02', 'Reporte 2', 3, 2, 2),
                                                                                                           (3, 'Reporte C', '2025-01-03', 'Reporte 3', 4, 3, 3),
                                                                                                           (4, 'Reporte D', '2025-01-04', 'Reporte 4', 5, 4, 4),
                                                                                                           (5, 'Reporte E', '2025-01-05', 'Reporte 5', 6, 5, 5),
                                                                                                           (6, 'Reporte F', '2025-01-06', 'Reporte 6', 7, 6, 6),
                                                                                                           (7, 'Reporte G', '2025-01-07', 'Reporte 7', 8, 7, 7),
                                                                                                           (8, 'Reporte H', '2025-01-08', 'Reporte 8', 9, 8, 8),
                                                                                                           (9, 'Reporte I', '2025-01-09', 'Reporte 9', 10, 9, 9),
                                                                                                           (10, 'Reporte J', '2025-01-10', 'Reporte 10', 1, 10, 10);

-- Tabla: filtros_personalizados
INSERT INTO filtros_personalizados (id, criterios, nombre, usuario_id) VALUES
                                                                           (1, 'edad > 30', 'Filtro A', 1),
                                                                           (2, 'genero = F', 'Filtro B', 2),
                                                                           (3, 'ocupacion = Doctor', 'Filtro C', 3),
                                                                           (4, 'zona_id = 1', 'Filtro D', 4),
                                                                           (5, 'respuesta = Sí', 'Filtro E', 5),
                                                                           (6, 'pregunta LIKE "%fiebre%"', 'Filtro F', 6),
                                                                           (7, 'jornada_id BETWEEN 1 AND 5', 'Filtro G', 7),
                                                                           (8, 'campana_id = 3', 'Filtro H', 8),
                                                                           (9, 'dni LIKE "100%"', 'Filtro I', 9),
                                                                           (10, 'nombre_usuario LIKE "%admin%"', 'Filtro J', 10);
