create table cultivos(
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 nombre varchar(30) not null,
 fase varchar(30) not null,
 alias varchar(30) not null unique
);
create table dias(
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 fecha TEXT not null,
 id_cultivo int not null,
 foreign key(id_cultivo) references cultivos(id)
);
create table tareas(
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 nombre text not null,
 texto text not null,
 id_dia int not null,
 foreign key(id_dia) references dias(id)
);