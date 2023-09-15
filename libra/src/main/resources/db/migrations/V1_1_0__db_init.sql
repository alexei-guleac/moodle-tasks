drop table cities cascade constraints;
drop table connection_types cascade constraints;
drop table issue_types cascade constraints;
drop table issues cascade constraints;
drop table logs cascade constraints;
drop table pos cascade constraints;
drop table statuses cascade constraints;
drop table users_details cascade constraints;
drop table users_types cascade constraints;

create table cities
(
    id        number(19, 0) generated as identity,
    city_name varchar2(255 char),
    primary key (id)
);

create table connection_types
(
    id              number(19, 0) generated as identity,
    connection_type varchar2(255 char),
    primary key (id)
);

create table issue_types
(
    id              number(19, 0) generated as identity,
    insert_date     timestamp,
    issue_level     varchar2(255 char),
    issue_type_name varchar2(255 char),
--     parent_issue_id number(19, 0),
    primary key (id)
);

create table issues
(
    id              number(19, 0) generated as identity,
    assigned_date   timestamp,
    creation_date   timestamp,
    description     varchar2(255 char),
    memo            varchar2(255 char),
    modif_date      timestamp,
    priority        varchar2(255 char),
    problem_id      number(10, 0),
    solution        varchar2(255 char),
    assigned_id     number(19, 0),
    issue_type_id   number(19, 0),
    pos_id          number(19, 0),
    status_id       number(19, 0),
    user_created_id number(19, 0),
    primary key (id)
);

create table logs
(
    id          number(19, 0) generated as identity,
    action      varchar2(255 char),
    insert_date timestamp,
    notes       varchar2(255 char),
    issue_id    number(19, 0),
    user_id     number(19, 0),
    primary key (id)
);

create table pos
(
    id                 number(19, 0) generated as identity,
    address            varchar2(255 char),
    afternoon_closing  number(1, 0),
    afternoon_opening  number(1, 0),
    brand              varchar2(255 char),
    cellphone          varchar2(255 char),
    days_closed        number(10, 0),
    insert_date        timestamp,
    model              varchar2(255 char),
    morning_closing    number(1, 0),
    morning_opening    number(1, 0),
    pos_name           varchar2(255 char),
    telephone          varchar2(255 char),
    city_id            number(19, 0),
    connection_type_id number(19, 0),
    primary key (id)
);

create table statuses
(
    id     number(19, 0) generated as identity,
    status varchar2(255 char),
    primary key (id)
);

create table users_details
(
    id               number(19, 0) generated as identity,
    email            varchar2(255 char),
    login            varchar2(255 char),
    name             varchar2(255 char),
    password         varchar2(255 char),
    encoded_password varchar2(255 char),
    telephone        varchar2(255 char),
    user_type_id     number(19, 0),
    primary key (id)
);

create table users_types
(
    id        number(19, 0) generated as identity,
    user_type varchar2(255 char),
    primary key (id)
);