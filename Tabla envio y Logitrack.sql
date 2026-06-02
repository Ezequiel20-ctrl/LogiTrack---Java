create database if not exists logitrack_db;
use logitrack_db;

create table envios (
	num_seguimiento varchar(20) primary key,
    remitente varchar (100) not null,
    destinatario varchar (100) not null,
    provincia_destino varchar (50) not null,
    localidad_destino varchar (100) not null,
    cp_destino varchar (10) not null,
    direccion_destino varchar (255) not null,
    peso_real decimal (10,2) not null,
    largo decimal (10,2) not null,
    ancho decimal (10,2) not null,
    alto decimal (10,2) not null,
    peso_volumetrico decimal (10,2) not null,
    tarifa decimal (10,2) not null,
    fecha_creacion timestamp default current_timestamp
);

create table usuario (
	id_usuario int primary key auto_increment,
    nombre varchar (100) not null,
    rol varchar (50) not null,
    legajo varchar (50) not null
);

create table estado(
	id_estado int primary key,
    nombre_estado varchar (50) not null
);

create table trazabilidad (
	id_movimiento int primary key auto_increment,
    num_seguimiento varchar (20) not null,
    id_usuario int,
    id_estado int,
    ubicacion varchar (150) not null,
    fecha_hora timestamp default current_timestamp,
    foreign key (num_seguimiento) references envios (num_seguimiento),
    foreign key (id_usuario) references usuario (id_usuario),
    foreign key (id_estado) references estado (id_estado)
);

insert into usuario (id_usuario, nombre, rol, legajo) values
(1,`Ezequiel Palma`, `Administrativo`, `AD-1509` ),
(2, `Juan Perez`, `Transportista`, `TP-1324`),
(3, `Carlos Gomez`, `Repartidor`, `RP-7825`);

insert into estado (id_estado, nombre_estado) values
(1, `Admisión`),
(2, `En Tránsito Troncal`),
(3, `Recibido en Sucursal`),
(4, `En Distribución Final`),
(5, `Entregado`);
