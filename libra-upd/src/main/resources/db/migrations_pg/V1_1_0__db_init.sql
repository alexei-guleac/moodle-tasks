drop table if exists cities cascade;
drop table if exists connection_types cascade;
drop table if exists issue_types cascade;
drop table if exists issues cascade;
drop table if exists logs cascade;
drop table if exists pos cascade;
drop table if exists statuses cascade;
drop table if exists users_details cascade;
drop table if exists users_types cascade;

create table cities
(
    id        bigserial not null,
    city_name varchar(255),
    primary key (id)
);

create table connection_types
(
    id              bigserial not null,
    connection_type varchar(255),
    primary key (id)
);

create table issue_types
(
    id              bigserial not null,
    insert_date     timestamp,
    issue_level     varchar(255),
    issue_type_name varchar(255),
    primary key (id)
);

create table issues
(
    id              bigserial not null,
    assigned_date   timestamp,
    creation_date   timestamp,
    description     varchar(255),
    memo            varchar(255),
    modif_date      timestamp,
    priority        varchar(255),
    problem_id      int4,
    solution        varchar(255),
    assigned_id     int8,
    issue_type_id   int8,
    pos_id          int8,
    status_id       int8,
    user_created_id int8,
    primary key (id)
);

create table logs
(
    id          bigserial not null,
    action      varchar(255),
    insert_date timestamp,
    notes       varchar(255),
    issue_id    int8,
    user_id     int8,
    primary key (id)
);

create table pos
(
    id                 bigserial not null,
    address            varchar(255),
    afternoon_closing  boolean,
    afternoon_opening  boolean,
    brand              varchar(255),
    cellphone          varchar(255),
    days_closed        int4,
    insert_date        timestamp,
    model              varchar(255),
    morning_closing    boolean,
    morning_opening    boolean,
    pos_name           varchar(255),
    telephone          varchar(255),
    city_id            int8,
    connection_type_id int8,
    primary key (id)
);

create table statuses
(
    id     bigserial not null,
    status varchar(255),
    primary key (id)
);

create table users_details
(
    id               bigserial not null,
    email            varchar(255),
    encoded_password varchar(255),
    login            varchar(255),
    name             varchar(255),
    password         varchar(255),
    telephone        varchar(255),
    user_type_id     int8,
    primary key (id)
);

create table users_types
(
    id        bigserial not null,
    user_type varchar(255),
    primary key (id)
);

alter table issues
    add constraint FK31m2dmwnvp2xk1rwv2f21dmf6 foreign key (assigned_id) references users_details;
alter table issues
    add constraint FK7duglnmclygbsw99f42eegmjy foreign key (issue_type_id) references issue_types;
alter table issues
    add constraint FKcdd3jn0vwrp6pf3bdbft91e75 foreign key (pos_id) references pos;
alter table issues
    add constraint FKrkaegn3ep72mhdkovirrr0h3e foreign key (status_id) references statuses;
alter table issues
    add constraint FKrrx6hnudvg6i5v21ldcemmmod foreign key (user_created_id) references users_details;
alter table logs
    add constraint FKpbbrnfv1ducutu3mr2hcgk794 foreign key (issue_id) references issues;
alter table logs
    add constraint FKomf5irr4v01q8mjdmth8v9pp8 foreign key (user_id) references users_details;
alter table pos
    add constraint FK754bq3md4fe3xoeng4iyra8an foreign key (city_id) references cities;
alter table pos
    add constraint FKq5ii9ait98tpmx3wo1m11bcss foreign key (connection_type_id) references connection_types;
alter table users_details
    add constraint FK6ssq6npxa5xl0vd0ugw7huvcn foreign key (user_type_id) references users_types;