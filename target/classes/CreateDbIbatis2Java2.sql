drop table FieldsOnly if exists;
drop table PKOnly if exists;
drop table PKFields if exists;
drop table PKBlobs if exists;
drop table PKFieldsBlobs if exists;
drop table FieldsBlobs if exists;
drop table "awful table" if exists;
drop table BlobsOnly if exists;

create table FieldsOnly (
  IntegerField int,
  DoubleField double,
  FloatField float
);

create table PKOnly (
  id int not null,
  seq_num int not null,
  primary key(id, seq_num)
);

create table PKFields (
  id1 int not null,
  id2 int not null,
  firstName varchar(20),
  lastName varchar(20),
  dateField date,
  timeField time,
  timestampField timestamp,
  decimal30Field decimal(3, 0),
  decimal60Field decimal(6, 0),
  decimal100Field decimal(10, 0),
  decimal155Field decimal(15, 5),
  wierd$Field int,
  "birth date" date,
  primary key (id1, id2)
);

create table PKBlobs (
  id int not null,
  blob1 longvarbinary,
  blob2 longvarbinary,
  primary key (id)
);

create table PKFieldsBlobs (
  id1 int not null,
  id2 int not null,
  firstName varchar(20),
  lastName varchar(20),
  blob1 longvarbinary,
  primary key (id1, id2)
);

create table FieldsBlobs (
  firstName varchar(20),
  lastName varchar(20),
  blob1 longvarbinary,
  blob2 longvarbinary
);

create table "awful table" (
  "CuStOmEr iD" int generated by default as identity (start with 57) not null,
  "customer id" int,
  "first name" varchar(20) default 'Mabel',
  "First Name" varchar(20),
  "first Name" varchar(20),
  first_name varchar(20),
  firstName varchar(20),
  "last name" varchar(20),
  E_MAIL varchar(20),
  _id1 int not null,
  $id2 int not null,
  id5_ int not null,
  id6$ int not null,
  id7$$ int not null,
  EmailAddress varchar(30),
  "from" varchar(30),
  active bit not null,
  primary key("CuStOmEr iD")
);

-- this table should be ignored, nothing generated
create table BlobsOnly (
  blob1 longvarbinary,
  blob2 longvarbinary
);
