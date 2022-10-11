delete from medication_quantity;
insert into medication_quantity (id, name) values ('acac00f8-cf2a-4427-8a1b-e00ab06b1111', '#');
insert into medication (id, name, quantity_type_id)
    values ('ed8c8540-b080-4832-a663-ff8d2c195ae9', 'Hydrocortisone 14mg', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111'),
           ('72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef', 'Hydrocortisone 8mg', 'acac00f8-cf2a-4427-8a1b-e00ab06b1111');