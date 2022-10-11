create table "user" (
    id  uuid not null primary key,
    email varchar(256) not null,
    password varchar(64) not null,
    name varchar(256)
);