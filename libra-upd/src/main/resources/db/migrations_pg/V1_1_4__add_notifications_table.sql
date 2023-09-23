create table notifications
(
    id              bigserial not null,
    issue_id        int8,
    priority        varchar(255),
    user_created_id int8,
    assigned_id     int8,
    description     varchar(255),
    assigned_date   timestamp,
    creation_date   timestamp,
    primary key (id)
);