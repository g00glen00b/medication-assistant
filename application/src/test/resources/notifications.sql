insert into "user" (id, email, password, name, timezone)
    values ('0a808223-9fd3-425e-9953-bbadbf44a79d', 'me@example.org', 'password', 'Rachel Thomas', 'Australia/Brisbane'),
           ('2b5ecbc9-7665-4263-9b75-16bc3f05e352', 'me2@example.org', 'password2', 'Erika Johnson', 'UTC');

insert into notification (id, user_id, message, type, reference, active)
values ('2b338afc-1ec9-48c9-9fca-f4aa046ecd51', '0a808223-9fd3-425e-9953-bbadbf44a79d', 'Notification 1 - User 1', 'INFO', 'REF-1', true),
       ('5e0f559d-b4ed-4bfd-8d49-77093363cf84', '0a808223-9fd3-425e-9953-bbadbf44a79d', 'Notification 2 - User 1', 'INFO', 'REF-2', false),
       ('16f148aa-face-4cd9-9e5e-95cce4defee5', '2b5ecbc9-7665-4263-9b75-16bc3f05e352', 'Notification 1 - User 2', 'INFO', 'REF-1', true);