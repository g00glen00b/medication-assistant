delete from medication_quantity;
insert into medication_quantity (id, name) values ('acac00f8-cf2a-4427-8a1b-e00ab06b1111', '#');
insert into medication (id, name, quantity_type_id)
    values ('ed8c8540-b080-4832-a663-ff8d2c195ae9', 'Hydrocortisone', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111'),
           ('72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef', 'Sustanon', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111');
insert into "user" (id, email, password, name, timezone)
    values ('0a808223-9fd3-425e-9953-bbadbf44a79d', 'me@example.org', 'password', 'Rachel Thomas', 'Australia/Brisbane');
insert into medication_availability (id, medication_id, user_id, quantity, initial_quantity, expiry_date)
    values ('e38e146f-a13c-4c54-a02a-b4547126f1f6', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', '0a808223-9fd3-425e-9953-bbadbf44a79d', 1, 10, '2022-10-31'),
           ('0d3b47e8-c2b1-48e9-954c-b4e98446ad97', '72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef', '0a808223-9fd3-425e-9953-bbadbf44a79d', 5, 10, '2022-11-01'),
           ('1d382721-ff30-4d93-9b65-21e05d8b6dc1', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', '0a808223-9fd3-425e-9953-bbadbf44a79d', 0, 10, '2022-10-31');
