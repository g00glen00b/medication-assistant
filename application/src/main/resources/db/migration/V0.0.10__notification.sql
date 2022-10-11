create type notification_type as enum ('INFO', 'WARNING', 'ERROR');

create table notification (
  id uuid not null primary key,
  user_id uuid not null,
  notification varchar(512) not null,
  type notification_type not null,
  reference varchar(128) not null,
  active boolean not null default true,
  created_date timestamp default current_timestamp,
  last_modified_date timestamp default current_timestamp,
  constraint fk_notification_user foreign key (user_id) references "user"(id),
  constraint uq_notification_reference_user unique (user_id, reference)
);