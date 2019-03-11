insert into clientes(id,nombre,apellido,email,create_at,foto) values(1,'Samuel','Gavidia','sam@gmail.com','2018-12-23','');
insert into clientes(id,nombre,apellido,email,create_at,foto) values(2,'Jose','Suca','suca@gmail.com','2018-12-23','');

insert into productos(nombre, precio, create_at) values('Equipo Panasonic', 4000, Now());
insert into productos(nombre, precio, create_at) values('Sony Full HD', 4500, Now());


insert into facturas(descripcion,observacion,cliente_id,create_at) values('Factura equipos de oficina',null,1,NOW());
insert into facturas_items(cantidad,factura_id,producto_id) values(1,1,1);
insert into facturas_items(cantidad,factura_id,producto_id) values(2,1,2);

insert into facturas(descripcion,observacion,cliente_id,create_at) values('Factura Biblioteca','Alguna nota importante!',1,NOW());
insert into facturas_items(cantidad,factura_id,producto_id) values(3,2,1);
insert into facturas_items(cantidad,factura_id,producto_id) values(2,2,2);