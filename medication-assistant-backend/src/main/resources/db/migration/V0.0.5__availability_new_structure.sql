drop table medication_availability;

create table medication_availability (
    id  uuid not null primary key,
    medication_id uuid not null,
    user_id uuid not null,
    quantity_type_id uuid not null,
    quantity decimal not null,
    initial_quantity decimal not null,
    expiry_date date,
    constraint fk_availability_user foreign key(user_id) references "user"(id),
    constraint fk_availability_medication foreign key (medication_id) references medication(id),
    constraint fk_availability_quantity_type foreign key (quantity_type_id) references medication_quantity_type(id)
);