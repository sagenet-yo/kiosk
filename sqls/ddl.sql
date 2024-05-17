-- A table with primary key as 1+ autogenerated serial
CREATE TABLE IF NOT EXISTS public.person (
	id SERIAL NOT NULL,
	"name" varchar(64) NULL,
	age int4 NULL,
	gender varchar NULL,
	CONSTRAINT person_age_check CHECK ((age >= 0)),
	CONSTRAINT person_gender_check CHECK (((gender = 'M') OR (gender = 'F'))),
	CONSTRAINT person_pkey PRIMARY KEY (id)
);
+

-- A table with primary key generated via a sequence
CREATE TABLE IF NOT EXISTS  public.vehicle (
	id int4 NOT NULL,
	make varchar NULL,
	model varchar NULL,
	year_of_production int4 NULL,
	CONSTRAINT vehicle_pkey PRIMARY KEY (id)
);

   
--  A function to call from the trigger
CREATE OR REPLACE FUNCTION public.set_vehicle_id()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
BEGIN
    NEW.id := nextval('vehicle__id_seq');
    RETURN NEW;
END;
$function$
;

-- A trigger
CREATE OR REPLACE TRIGGER before_insert_vehicle BEFORE
INSERT ON public.vehicle for each row execute function public.set_vehicle_id()
;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- now do only serial ids here onewards
CREATE TABLE address
(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	street VARCHAR,
	zip INT,
	state VARCHAR,
	country VARCHAR DEFAULT 'US'
)
;

- m-m relation
CREATE TABLE address_person_association
( 
	id SERIAL PRIMARY KEY,
	person_id INT NOT NULL,
    address_id UUID NOT NULL,
	FOREIGN KEY (person_id) REFERENCES person(id),
	FOREIGN KEY (address_id) REFERENCES address(id)
)
;
drop table address_person_association 
-- public.person_id_seq definition

-- DROP SEQUENCE public.person_id_seq;

CREATE SEQUENCE public.vehicle__id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
