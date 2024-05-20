create table product
(
    id                 int auto_increment
        primary key,
    created_by         varchar(50)    not null,
    created_date       datetime(6)    null,
    last_modified_by   varchar(50)    null,
    last_modified_date datetime(6)    null,
    active             bit            null,
    code               varchar(50)    not null,
    com_id             int            null,
    description        varchar(255)   null,
    image              varchar(500)   null,
    in_price           decimal(21, 6) null,
    name               varchar(500)   not null,
    normalized_name    varchar(255)   null,
    out_price          decimal(21, 6) null,
    unit               varchar(50)    null
);

INSERT INTO restaurant.product (id, created_by, created_date, last_modified_by, last_modified_date, active, code, com_id, description, image, in_price, name, normalized_name, out_price, unit) VALUES (1, 'admin', '2024-05-19 21:50:17.631678', 'admin', '2024-05-19 21:50:17.631678', true, 'SP_1_SEQ6BP0MT', 1, 'aaa', 'api/client/file/product/1/20240519215017_image10.jpg', 13.000000, 'a', 'a', 12.000000, '');
