update medication_quantity set name = 'capsules' where name = '#';
insert into medication_quantity (id, name) values
    (gen_random_uuid(), 'tablets'),
    (gen_random_uuid(), 'drops'),
    (gen_random_uuid(), 'injections'),
    (gen_random_uuid(), 'inhales'),
    (gen_random_uuid(), 'patches'),
    (gen_random_uuid(), 'suppositories');