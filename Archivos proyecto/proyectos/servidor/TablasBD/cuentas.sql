drop database db_usuarios;
CREATE DATABASE db_usuarios;
use db_usuarios;


create table usuarios(
	id int PRIMARY key AUTO_INCREMENT,
	usuario VARCHAR(30) unique not null,
	mail VARCHAR(255),
	passwd VARCHAR(44) NOT NULL
);




drop database db_sesiones;
create database db_sesiones;
use db_sesiones;
create table sesiones(
	id int PRIMARY key auto_increment,
	uuid VARCHAR(36) UNIQUE NOT NULL,
	usuario VARCHAR(30) not null
);
