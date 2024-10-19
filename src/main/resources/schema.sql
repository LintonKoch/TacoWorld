create table if not exists Taco_Order (
  id identity,
  deliveryName varchar(50) not null,
  deliveryStreet varchar(50) not null,
  deliveryCity varchar(50) not null,
  deliveryState varchar(2) not null,
  deliveryZip varchar(10) not null,
  ccNumber varchar(16) not null,
  ccExpiration varchar(5) not null,
  ccCvv varchar(3) not null,
  placedAt timestamp not null
);
 
create table if not exists Taco (
  id identity,
  name varchar(50) not null,
  taco_order bigint not null,
  taco_order_key bigint not null,
  created_at timestamp not null
);
 
create table if not exists Ingredient_Ref (
  ingredient varchar(4) not null,
  taco bigint not null,
  taco_key bigint not null
);
 
 
create table if not exists Ingredient (
  id varchar(4) primary key not null,
  name varchar(25) not null,
  type varchar(10) not null
);
 
 
alter table Taco
    add foreign key (taco_order) references Taco_Order(id);

alter table Ingredient_Ref
    add foreign key (ingredient) references Ingredient(id);