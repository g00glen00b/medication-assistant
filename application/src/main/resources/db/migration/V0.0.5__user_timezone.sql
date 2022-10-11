alter table "user" alter column timezone set default 'Z';
update "user" set timezone = 'Z' where timezone = 'UTC';