delete from medication_schedule_completed_event where created_date is null;
delete from notification where created_date is null;

alter table "user" alter column created_date set not null;
alter table "user" alter column last_modified_date set not null;
alter table "medication" alter column created_date set not null;
alter table "medication" alter column last_modified_date set not null;
alter table "medication_quantity" alter column created_date set not null;
alter table "medication_quantity" alter column last_modified_date set not null;
alter table "medication_availability" alter column created_date set not null;
alter table "medication_availability" alter column last_modified_date set not null;
alter table "medication_schedule" alter column created_date set not null;
alter table "medication_schedule" alter column last_modified_date set not null;
alter table "medication_schedule_completed_event" alter column created_date set not null;
alter table "medication_schedule_completed_event" alter column last_modified_date set not null;
alter table "notification" alter column created_date set not null;
alter table "notification" alter column last_modified_date set not null;