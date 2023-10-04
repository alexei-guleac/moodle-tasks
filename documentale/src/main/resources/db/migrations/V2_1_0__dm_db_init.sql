drop table dm_document_type_ierarchy cascade constraints;
drop table dm_document_types cascade constraints;
drop table dm_documents cascade constraints;
drop table dm_institutions cascade constraints;
drop table dm_projects cascade constraints;
drop table dm_user_roles cascade constraints;
drop table dm_users_details cascade constraints;

create table dm_document_type_ierarchy
(
    id       number(19, 0) generated as identity,
    id_macro number(19, 0),
    id_micro number(19, 0),
    primary key (id)
);

create table dm_document_types
(
    id               number(19, 0) generated as identity,
    code             varchar2(255 char),
    is_date_grouped  number(1, 0),
    is_macro         number(1, 0),
    name             varchar2(255 char),
    type_description varchar2(255 char),
    primary key (id)
);

create table dm_documents
(
    id                number(19, 0) generated as identity,
    additional_info   varchar2(255 char),
    grouping_date     timestamp,
    name              varchar2(255 char),
    saved_path        varchar2(255 char),
    upload_date       timestamp,
    document_types_id number(19, 0),
    institution_id    number(19, 0),
    project_id        number(19, 0),
    user_created_id   number(19, 0),
    primary key (id)
);

create table dm_institutions
(
    id              number(19, 0) generated as identity,
    additional_info varchar2(255 char),
    code            varchar2(255 char),
    name            varchar2(255 char),
    primary key (id)
);

create table dm_projects
(
    id              number(19, 0) generated as identity,
    additional_info varchar2(255 char),
    date_from       timestamp,
    date_till       timestamp,
    is_active       number(1, 0),
    name            varchar2(255 char),
    institution_id  number(19, 0),
    user_created_id number(19, 0),
    primary key (id)
);

create table dm_user_roles
(
    id        number(19, 0) generated as identity,
    user_role varchar2(255 char),
    primary key (id)
);

create table dm_users_details
(
    id               number(19, 0) generated as identity,
    email            varchar2(255 char),
    encoded_password varchar2(255 char),
    is_enabled       number(1, 0),
    login            varchar2(255 char),
    name             varchar2(255 char),
    password         varchar2(255 char),
    patronymic       varchar2(255 char),
    surname          varchar2(255 char),
    institution_id   number(19, 0),
    user_role_id     number(19, 0),
    primary key (id)
);

alter table dm_document_type_ierarchy
    add constraint FKgmhyj7ny8jyv894mxw2t4l91x foreign key (id_macro) references dm_documents;

alter table dm_document_type_ierarchy
    add constraint FK4qwn6ppw7feo7xc6g19mtu55q foreign key (id_micro) references dm_documents;

alter table dm_documents
    add constraint FKd16pqhsdcv3r0jwquwo3qjwyd foreign key (document_types_id) references dm_document_types;

alter table dm_documents
    add constraint FKlqdspymxh75nsrnjwya404fd5 foreign key (institution_id) references dm_institutions;

alter table dm_documents
    add constraint FKerso0dwwlki46bxf879jte2wu foreign key (project_id) references dm_projects;

alter table dm_documents
    add constraint FKrrcowip6ga68k2u0ikdp7ufhf foreign key (user_created_id) references dm_users_details;

alter table dm_projects
    add constraint FKlun1i166f9o3npi64dk7wfi91 foreign key (institution_id) references dm_institutions;

alter table dm_projects
    add constraint FK4cpe0sv1yyvu35ii46s48fvrx foreign key (user_created_id) references dm_users_details;