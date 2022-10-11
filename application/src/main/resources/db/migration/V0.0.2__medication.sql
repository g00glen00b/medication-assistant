create table medication_quantity (
    id  uuid not null primary key,
    name varchar(256) not null
);

create table medication (
     id  uuid not null primary key,
     name varchar(256) not null,
     quantity_type_id uuid not null,
     constraint fk_medication_quantity foreign key (quantity_type_id) references medication_quantity(id)
);

insert into medication_quantity (id, name) values
    (gen_random_uuid(), '#'),
    (gen_random_uuid(), 'mg'),
    (gen_random_uuid(), 'ml');