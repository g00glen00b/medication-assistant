alter table "user" alter column timezone set default 'UTC';
update "user" set timezone = 'UTC' where timezone = 'Z';