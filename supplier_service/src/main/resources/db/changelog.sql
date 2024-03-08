--liquibase formatted sql

--changeset nig:init_schema
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;

create table products
(
    ID bigserial primary key,
    NAME        varchar(255)   not null,
    DESCRIPTION varchar(4096) not null,
    PRICE  integer   not null,
    CATEGORY_ID bigint
);

create table categories
(
    ID bigserial primary key,
    NAME      varchar(255) not null
);


--changeset nig:populate_data
insert into categories (NAME)
values ('bakery'),
       ('vegetables'),
       ('confectionery products');

insert into products (NAME, DESCRIPTION, PRICE, CATEGORY_ID)
values ('bread', 'just white bread', 50, 1),
       ('bun', 'hearty bun', 40, 1),
       ('cucumbers', 'ripe green', 50, 2),
       ('tomatoes', 'juicy Caucasian', 100, 2),
       ('chocolate', 'your teeth won''t thank you', 70, 3),
       ('cocoa', 'enjoy winter evenings with me', 80, 3);

alter table products
    add constraint FK_CATEGORY_PRODUCT foreign key (CATEGORY_ID) references categories (ID) on delete cascade;
