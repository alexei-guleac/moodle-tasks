alter table dm_document_type_ierarchy
    drop constraint FKgmhyj7ny8jyv894mxw2t4l91x;
alter table dm_document_type_ierarchy
    drop constraint FK4qwn6ppw7feo7xc6g19mtu55q;
alter table dm_documents
    drop constraint FKd16pqhsdcv3r0jwquwo3qjwyd;
alter table dm_documents
    drop constraint FKlqdspymxh75nsrnjwya404fd5;
alter table dm_documents
    drop constraint FKerso0dwwlki46bxf879jte2wu;
alter table dm_documents
    drop constraint FKrrcowip6ga68k2u0ikdp7ufhf;
alter table dm_projects
    drop constraint FKlun1i166f9o3npi64dk7wfi91;
alter table dm_projects
    drop constraint FK4cpe0sv1yyvu35ii46s48fvrx;
alter table dm_users_details
    drop constraint FKa826nbolw1gq511i31wrnknhk;
alter table dm_users_details
    drop constraint FK8388u8s11vf4x7v6frwtqvu9t;

drop table if exists dm_document_type_ierarchy cascade;
drop table if exists dm_document_types cascade;
drop table if exists dm_documents cascade;
drop table if exists dm_institutions cascade;
drop table if exists dm_projects cascade;
drop table if exists dm_user_roles cascade;
drop table if exists dm_users_details cascade;

create table dm_document_type_ierarchy
(
    id       bigserial not null,
    id_macro int8,
    id_micro int8,
    primary key (id)
);

create table dm_document_types
(
    id               bigserial not null,
    code             varchar(255),
    is_date_grouped  boolean,
    is_macro         boolean,
    name             varchar(255),
    type_description varchar(255),
    primary key (id)
);

create table dm_documents
(
    id                bigserial not null,
    additional_info   varchar(255),
    grouping_date     timestamp,
    name              varchar(255),
    saved_path        varchar(255),
    upload_date       timestamp,
    document_types_id int8,
    institution_id    int8,
    project_id        int8,
    user_created_id   int8,
    primary key (id)
);

create table dm_institutions
(
    id              bigserial not null,
    additional_info varchar(255),
    code            varchar(255),
    name            varchar(255),
    primary key (id)
);

create table dm_projects
(
    id              bigserial not null,
    additional_info varchar(255),
    date_from       timestamp,
    date_till       timestamp,
    is_active       boolean,
    name            varchar(255),
    institution_id  int8,
    user_created_id int8,
    primary key (id)
);

create table dm_user_roles
(
    id        bigserial not null,
    user_role varchar(255),
    primary key (id)
);

create table dm_users_details
(
    id               bigserial not null,
    email            varchar(255),
    encoded_password varchar(255),
    is_enabled       boolean,
    login            varchar(255),
    name             varchar(255),
    password         varchar(255),
    patronymic       varchar(255),
    surname          varchar(255),
    institution_id   int8,
    user_role_id     int8,
    primary key (id)
);


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
alter table dm_users_details
    add constraint FKa826nbolw1gq511i31wrnknhk foreign key (institution_id) references dm_institutions;
alter table dm_users_details
    add constraint FK8388u8s11vf4x7v6frwtqvu9t foreign key (user_role_id) references dm_user_roles;