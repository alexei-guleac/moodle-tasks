create table notifications
(
    id              number(19, 0) generated as identity,
    issue_id        number(19, 0),
    priority        varchar2(255 char),
    user_created_id number(19, 0),
    assigned_id     number(19, 0),
    description     varchar2(255 char),
    assigned_date   timestamp,
    creation_date   timestamp,
    primary key (id)
);