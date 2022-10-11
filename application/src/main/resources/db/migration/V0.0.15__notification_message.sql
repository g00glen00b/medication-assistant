alter table notification drop column notification;
alter table notification add column message varchar(512) not null;