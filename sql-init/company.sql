create table company
(
    id                 int auto_increment
        primary key,
    created_by         varchar(50)  not null,
    created_date       datetime(6)  null,
    last_modified_by   varchar(50)  null,
    last_modified_date datetime(6)  null,
    name               varchar(255) null
);

