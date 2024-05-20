create table ep_user
(
    id                 int auto_increment
        primary key,
    created_by         varchar(50)  not null,
    created_date       datetime(6)  null,
    last_modified_by   varchar(50)  null,
    last_modified_date datetime(6)  null,
    address            varchar(512) null,
    email              varchar(255) null,
    full_name          varchar(100) null,
    normalized_name    varchar(255) null,
    password           varchar(255) null,
    phone_number       varchar(255) null,
    status             int          null,
    username           varchar(100) not null,
    company_id         int          null
);



INSERT INTO restaurant.ep_user (id, created_by, created_date, last_modified_by, last_modified_date, address, email, full_name, normalized_name, password, phone_number, status, username, company_id) VALUES (1, 'a', '2024-05-19 16:02:29.000000', 'a', '2024-05-19 16:02:25.000000', 'a', 'admin@email.com', 'admin', 'admin', '$2a$10$ssovSyiFHJAvI2PYbQEUbOsLBVMdNI7H.3vP8DprD4CmnZIiySWUG', '01232456798', 1, 'admin', 1);
