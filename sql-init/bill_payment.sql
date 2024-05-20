create table bill_payment
(
    id                 int auto_increment
        primary key,
    created_by         varchar(50)    not null,
    created_date       datetime(6)    null,
    last_modified_by   varchar(50)    null,
    last_modified_date datetime(6)    null,
    amount             decimal(21, 6) null,
    payment_method     varchar(255)   null,
    bill_id            int            not null,
    constraint FKjnqsfr0b9lukfdd6577vtngd5
        foreign key (bill_id) references bill (id)
);

INSERT INTO restaurant.bill_payment (id, created_by, created_date, last_modified_by, last_modified_date, amount, payment_method, bill_id) VALUES (1, 'admin', '2024-05-19 21:50:25.306558', 'admin', '2024-05-19 21:50:25.306558', 0.000000, 'Tiền mặt', 1);
