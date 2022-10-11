alter table "user" add timezone varchar(32) not null default 'UTC';

create table medication_schedule (
     id uuid not null primary key,
     medication_id uuid not null,
     user_id uuid not null,
     starting_at date not null,
     ending_at_inclusive date,
     interval varchar(32) not null,
     time time without time zone not null,
     description varchar(256),
     constraint fk_schedule_user foreign key (user_id) references "user"(id),
     constraint fk_schedule_medication foreign key (medication_id) references medication(id)
);

create table medication_schedule_completed_event (
    id uuid not null primary key,
    schedule_id uuid not null,
    event_date timestamp without time zone not null,
    completed_date timestamp without time zone not null,
    constraint fk_event_schedule foreign key (schedule_id) references medication_schedule(id)
);