delete from medication_quantity;
insert into medication_quantity (id, name) values ('acac00f8-cf2a-4427-8a1b-e00ab06b1111', '#');
insert into medication (id, name, quantity_type_id)
    values ('ed8c8540-b080-4832-a663-ff8d2c195ae9', 'Hydrocortisone 14mg', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111');
insert into "user" (id, email, password, name, timezone)
    values ('0a808223-9fd3-425e-9953-bbadbf44a79d', 'me@example.org', 'password', 'Rachel Thomas', 'Australia/Brisbane'),
           ('2b5ecbc9-7665-4263-9b75-16bc3f05e352', 'me2@example.org', 'password2', 'Erika Johnson', 'UTC');

insert into medication_schedule (id, medication_id, user_id, starting_at, ending_at_inclusive, interval, time, quantity, description)
values ('ea8dea2b-e4a0-47b4-a0c1-b67ed738610f', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', '0a808223-9fd3-425e-9953-bbadbf44a79d', '2022-10-01', '2022-10-31', 'P2D', '08:00', 1, 'Morning user 1'),
       ('ca14b071-3a82-4739-859e-4b3dea3f9719', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', '0a808223-9fd3-425e-9953-bbadbf44a79d', '2022-10-02', '2022-10-31', 'P2D', '08:00', 1, 'Morning other day user 1'),
       ('b7839760-0b1b-4b68-a5b7-c18b2e17d4b9', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', '2b5ecbc9-7665-4263-9b75-16bc3f05e352', '2022-10-01', '2022-10-31', 'P1D', '08:00', 1, 'Morning user 2');

insert into medication_schedule_completed_event (id, schedule_id, event_date, completed_date)
values ('abb7322a-824b-4b88-8cdd-4b073ad25483', 'ea8dea2b-e4a0-47b4-a0c1-b67ed738610f', '2022-10-01 08:00', '2022-10-01 08:01'),
       ('ccaa7bfc-3a4e-45b5-9b0b-10e3fa180d87', 'ca14b071-3a82-4739-859e-4b3dea3f9719', '2022-10-02 08:00', '2022-10-02 07:59'),
       ('d65ec5ef-ae2c-4ba1-90da-f78aa5116a8c', 'ea8dea2b-e4a0-47b4-a0c1-b67ed738610f', '2022-10-03 08:00', '2022-10-03 08:02');