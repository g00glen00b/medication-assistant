delete from medication_quantity;
insert into medication_quantity (id, name) values ('acac00f8-cf2a-4427-8a1b-e00ab06b1111', '#');
insert into medication (id, name, quantity_type_id)
    values ('ed8c8540-b080-4832-a663-ff8d2c195ae9', 'Hydrocortisone', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111'),
           ('72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef', 'Sustanon', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111'),
           ('2deea386-9ae9-45c8-881d-bb3cc12d8d51', 'Genotonorm', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111');
insert into "user" (id, email, password, name, timezone)
    values ('0a808223-9fd3-425e-9953-bbadbf44a79d', 'me1@example.org', 'password', 'Rachel Thomas', 'Australia/Brisbane'),
           ('b40fbbac-60c5-478c-89ad-198e38e2d457', 'me2@example.org', 'password', 'William Raymond', 'UTC'),
           ('f49ae7d4-ee91-4832-a81c-a4e731a29dfe', 'me3@example.org', 'password', 'Phillip Elliott', 'Europe/Berlin'),
           ('31144060-5894-48a8-a40d-18de1100085f', 'me4@example.org', 'password', 'Brittany Holmes', 'America/Vancouver');
insert into medication_availability (id, medication_id, user_id, quantity, initial_quantity, expiry_date)
           --- Low quantity
    values ('e38e146f-a13c-4c54-a02a-b4547126f1f6', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', '0a808223-9fd3-425e-9953-bbadbf44a79d', 1, 10, '2022-10-31'),
           ('0d3b47e8-c2b1-48e9-954c-b4e98446ad97', '72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef', '0a808223-9fd3-425e-9953-bbadbf44a79d', 5, 10, '2022-11-01'),
           --- Low quantity
           --- No quantity
           ('0960ab5f-8a2b-4954-a0fb-22b89f262075', '2deea386-9ae9-45c8-881d-bb3cc12d8d51', 'b40fbbac-60c5-478c-89ad-198e38e2d457', 0, 10, '2022-10-10'),
           --- Low quantity
           --- Expires today
           ('1a921354-a131-429b-8f1f-7b40e59c8b1a', '72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef', '31144060-5894-48a8-a40d-18de1100085f', 1, 10, '2022-10-10'),
           --- Expires soon
           ('89251a0d-ebef-4a2e-8472-09e9087bfd5a', 'ed8c8540-b080-4832-a663-ff8d2c195ae9', 'f49ae7d4-ee91-4832-a81c-a4e731a29dfe', 7, 10, '2022-10-11');
