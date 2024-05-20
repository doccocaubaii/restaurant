create table bill_product
(
    id                      int auto_increment
        primary key,
    created_by              varchar(50)    not null,
    created_date            datetime(6)    null,
    last_modified_by        varchar(50)    null,
    last_modified_date      datetime(6)    null,
    amount                  decimal(21, 6) null,
    position                int            null,
    product_code            varchar(255)   null,
    product_id              int            null,
    product_name            varchar(255)   null,
    product_normalized_name varchar(255)   null,
    quantity                decimal(21, 6) null,
    total_amount            decimal(21, 6) null,
    unit                    varchar(255)   null,
    unit_price              decimal(21, 6) null,
    bill_id                 int            not null,
    constraint FK5d4mpa9hm51rpdnk7wfgo0rtj
        foreign key (bill_id) references bill (id)
);

INSERT INTO restaurant.bill_product (id, created_by, created_date, last_modified_by, last_modified_date, amount, position, product_code, product_id, product_name, product_normalized_name, quantity, total_amount, unit, unit_price, bill_id) VALUES (1, 'admin', '2024-05-19 21:50:25.319623', 'admin', '2024-05-19 21:50:25.319623', 12.000000, 1, 'SP_1_SEQ6BP0MT', 1, 'a', 'a', 1.000000, 12.000000, null, 12.000000, 1);
