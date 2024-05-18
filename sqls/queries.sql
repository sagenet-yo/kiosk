INSERT INTO public.vehicle  (make , model, year_of_production)
VALUES ('Toyota', 'Camry', '2000');


INSERT INTO address_person_association  (person_id, address_id)
VALUES (2, '9087be14-f3c0-4f84-b995-8724943788c7'::uuid);

INSERT INTO address (street, zip, state)
VALUES ('Riverlook Pkwy', 30067, 'GA');

SELECT * FROM vehicle;
select * from address_person_association
select * from person
select * from address 
select * from vehicle

