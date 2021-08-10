create table "user" (
    id  uuid not null primary key,
    email varchar(256) not null,
    password varchar(64) not null,
    first_name varchar(256),
    last_name varchar(256)
);