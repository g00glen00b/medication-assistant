create table medication_availability (
    medication_id uuid not null,
    user_id uuid not null,
    quantity_type_id uuid not null,
    quantity decimal not null,
    primary key (medication_id, user_id),
    constraint fk_availability_user foreign key(user_id) references "user"(id),
    constraint fk_availability_medication foreign key (medication_id) references medication(id),
    constraint fk_availability_quantity_type foreign key (quantity_type_id) references medication_quantity_type(id)
);