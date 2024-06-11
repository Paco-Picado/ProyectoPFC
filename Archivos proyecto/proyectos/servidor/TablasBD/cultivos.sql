
DROP DATABASE IF EXISTS db_cultivos;
CREATE DATABASE db_cultivos;
USE db_cultivos;

CREATE TABLE cultivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE fases_cultivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL,
    id_cultivo INT NOT NULL,
    FOREIGN KEY (id_cultivo) REFERENCES cultivos(id)
);

CREATE TABLE lista_tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_fase INT NOT NULL,
    FOREIGN KEY (id_fase) REFERENCES fases_cultivos(id)
);

CREATE TABLE textos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    texto VARCHAR(150) NOT NULL
);

CREATE TABLE tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    dia_inicial INT NOT NULL,
    dia_final INT NOT NULL,
    intervalo_dias INT NOT NULL,
    id_lista INT NOT NULL,
    FOREIGN KEY (id_lista) REFERENCES lista_tareas(id),
    id_textos INT NOT NULL,
    FOREIGN KEY (id_textos) REFERENCES textos(id)
);

CREATE TABLE problemas_cultivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL,
    id_cultivo INT NOT NULL,
    FOREIGN KEY (id_cultivo) REFERENCES cultivos(id)
);

CREATE TABLE lista_tareas_problemas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_problemas_cultivos INT NOT NULL,
    FOREIGN KEY (id_problemas_cultivos) REFERENCES problemas_cultivos(id)
);

CREATE TABLE tareas_problemas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    dia_inicial INT NOT NULL,
    dia_final INT NOT NULL,
    intervalo_dias INT NOT NULL,
    id_lista INT NOT NULL,
    FOREIGN KEY (id_lista) REFERENCES lista_tareas_problemas(id),
    id_textos INT NOT NULL,
    FOREIGN KEY (id_textos) REFERENCES textos(id)
);


INSERT INTO cultivos VALUES (1, 'tomate cherry');

INSERT INTO fases_cultivos VALUES (1, 'germinacion', 1);
INSERT INTO fases_cultivos VALUES (2, 'desarrollo', 1);
INSERT INTO fases_cultivos VALUES (3, 'floracion', 1);
INSERT INTO fases_cultivos VALUES (4, 'maduracion', 1);

INSERT INTO lista_tareas VALUES (1, 1);
INSERT INTO lista_tareas VALUES (2, 2);
INSERT INTO lista_tareas VALUES (3, 3);
INSERT INTO lista_tareas VALUES (4, 4);

INSERT INTO textos (id, texto) VALUES (1, 'Regar con 2 litros.');
INSERT INTO textos (id, texto) VALUES (2, 'Mantener la temperatura entre 20-25°C y la humedad alrededor del 70-80%.');
INSERT INTO textos (id, texto) VALUES (3, 'Proveer luz indirecta o bajo una lámpara de crecimiento por 14-16 horas.');
INSERT INTO textos (id, texto) VALUES (4, 'Revisar las plántulas para asegurarse de que no hay enfermedades o plagas.');
INSERT INTO textos (id, texto) VALUES (5, 'Aplicar fertilizante balanceado NPK (20-20-20).');
INSERT INTO textos (id, texto) VALUES (6, 'Realizar poda para eliminar hojas y ramas enfermas.');
INSERT INTO textos (id, texto) VALUES (7, 'Inspeccionar y tratar plagas (p.ej., ácaros, pulgones).');
INSERT INTO textos (id, texto) VALUES (8, 'Aplicar fertilizante rico en fósforo (P).');
INSERT INTO textos (id, texto) VALUES (9, 'Sacudir suavemente las plantas para favorecer la polinización.');
INSERT INTO textos (id, texto) VALUES (10, 'Aplicar fertilizante rico en potasio (K).');
INSERT INTO textos (id, texto) VALUES (11, 'Asegurar las plantas con tutores o jaulas.');
INSERT INTO textos (id, texto) VALUES (12, 'Revisar los frutos para detectar enfermedades y plagas.');
INSERT INTO textos (id, texto) VALUES (13, 'Cosechar los tomates cherry maduros.');
INSERT INTO textos (id, texto) VALUES (14, 'Sembrar las semillas en macetas o bandejas de germinación.');
INSERT INTO textos (id, texto) VALUES (15, 'Transplantar las plántulas a su lugar definitivo.');
INSERT into textos VALUES(16, 'Aplicar fungicida.');
INSERT into textos VALUES(17, 'Retirar partes contaminadas.');
INSERT into textos VALUES(18, 'Aislar planta del resto.');
INSERT INTO textos VALUES(19, 'Aplicar método de control de insectos.');

INSERT INTO tareas VALUES (1, 'riego', 1, 15, 2, 1, 1);
INSERT INTO tareas VALUES (2, 'temperatura-humedad', 1, 15, 1, 1, 2);
INSERT INTO tareas VALUES (3, 'luz', 1, 15, 1, 1, 3);
INSERT INTO tareas VALUES (4, 'revision-plantulas', 1, 15, 5, 1, 4);
INSERT INTO tareas VALUES (20, 'siembra', 1, 5, 10, 1, 14);
INSERT INTO tareas VALUES (21, 'trasplante', 10, 15, 10, 1, 15);

INSERT INTO tareas VALUES (5, 'riego', 1, 45, 3, 2, 1);
INSERT INTO tareas VALUES (6, 'fertilizacion', 1, 45, 15, 2, 5);
INSERT INTO tareas VALUES (7, 'luz', 1, 45, 1, 2, 3);
INSERT INTO tareas VALUES (8, 'poda', 1, 45, 15, 2, 6);
INSERT INTO tareas VALUES (9, 'revision-plagas', 1, 45, 15, 2, 7);

INSERT INTO tareas VALUES (10, 'riego', 1, 30, 3, 3, 1);
INSERT INTO tareas VALUES (11, 'fertilizacion', 1, 30, 15, 3, 8);
INSERT INTO tareas VALUES (12, 'polinización', 1, 30, 5, 3, 9);
INSERT INTO tareas VALUES (13, 'luz', 1, 30, 1, 3, 3);
INSERT INTO tareas VALUES (14, 'revision-plagas', 1, 30, 10, 3, 7);

INSERT INTO tareas VALUES (15, 'riego', 1, 30, 3, 4, 1);
INSERT INTO tareas VALUES (16, 'fertilización', 1, 30, 15, 4, 10);
INSERT INTO tareas VALUES (17, 'soporte', 1, 30, 15, 4, 11);
INSERT INTO tareas VALUES (18, 'revision-frutos', 1, 30, 10, 4, 12);
INSERT INTO tareas VALUES (19, 'cosecha', 1, 30, 5, 4, 13);

INSERT INTO problemas_cultivos VALUES (1, 'hongos', 1);
INSERT INTO lista_tareas_problemas VALUES (1, 1);
INSERT INTO tareas_problemas VALUES (1, 'fungicida', 1, 7, 2, 1, 16);
INSERT INTO tareas_problemas VALUES (2, 'poda', 1, 1, 1, 1, 17);
INSERT into tareas_problemas VALUEs (3, 'aislamiento', 1,1,1,1,18);

INSERT INTO problemas_cultivos VALUES (2, 'insectos', 1);
INSERT INTO lista_tareas_problemas VALUES (2, 2);
INSERT INTO tareas_problemas VALUES (4, 'control-insectos', 1, 7, 2, 2, 19);
