create table medication_schedule (
    id uuid not null primary key,
    medication_id uuid not null,
    user_id uuid not null,
    quantity_type_id uuid not null,
    quantity decimal not null,
    starting_at date not null,
    ending_at date,
    interval varchar(16) not null,
    time time not null,
    constraint fk_schedule_user foreign key(user_id) references "user"(id),
    constraint fk_schedule_medication foreign key (medication_id) references medication(id),
    constraint fk_schedule_quantity_type foreign key (quantity_type_id) references medication_quantity_type(id)
);

create table medication_schedule_event (
  id uuid not null primary key,
  time timestamp not null,
  schedule_id uuid not null,
  active boolean not null,
  constraint fk_event_schedule foreign key(schedule_id) references medication_schedule(id)
);