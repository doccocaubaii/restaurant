create table bill
(
    id                       int auto_increment
        primary key,
    created_by               varchar(50)    not null,
    created_date             datetime(6)    null,
    last_modified_by         varchar(50)    null,
    last_modified_date       datetime(6)    null,
    amount                   decimal(21, 6) null,
    bill_date                datetime(6)    null,
    code                     varchar(255)   null,
    com_id                   int            null,
    customer_name            varchar(255)   null,
    customer_normalized_name varchar(255)   null,
    delivery_type            int            null,
    description              varchar(255)   null,
    quantity                 decimal(21, 6) null,
    status                   int            null,
    total_amount             decimal(21, 6) null
);

INSERT INTO restaurant.bill (id, created_by, created_date, last_modified_by, last_modified_date, amount, bill_date, code, com_id, customer_name, customer_normalized_name, delivery_type, description, quantity, status, total_amount) VALUES (1, 'admin', '2024-05-19 21:50:25.281639', 'admin', '2024-05-19 21:50:25.281639', 12.000000, '2024-05-19 21:50:25.000000', 'DH_1_SEQBU0TUB', 1, 'khách lẻ', 'khachle', 2, null, 1.000000, 1, 12.000000);
