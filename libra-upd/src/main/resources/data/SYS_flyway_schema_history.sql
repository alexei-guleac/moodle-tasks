create table "flyway_schema_history"
(
    "installed_rank" NUMBER                                 not null
        constraint "flyway_schema_history_pk"
            primary key,
    "version"        VARCHAR2(50),
    "description"    VARCHAR2(200)                          not null,
    "type"           VARCHAR2(20)                           not null,
    "script"         VARCHAR2(1000)                         not null,
    "checksum"       NUMBER,
    "installed_by"   VARCHAR2(100)                          not null,
    "installed_on"   TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
    "execution_time" NUMBER                                 not null,
    "success"        NUMBER(1)                              not null
)
/

create index "flyway_schema_history_s_idx"
    on "flyway_schema_history" ("success")
/

INSERT INTO SYS."flyway_schema_history" ("installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success") VALUES (1, '1.1.0', 'db init', 'SQL', 'V1_1_0__db_init.sql', -1834942361, 'SYS', TO_TIMESTAMP('2023-09-15 09:55:32.033000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 212, 1);
INSERT INTO SYS."flyway_schema_history" ("installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success") VALUES (2, '1.1.1', 'insert initial data', 'SQL', 'V1_1_1__insert_initial_data.sql', 1209478513, 'SYS', TO_TIMESTAMP('2023-09-15 09:55:32.063000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 13, 1);
INSERT INTO SYS."flyway_schema_history" ("installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success") VALUES (3, '1.1.2', 'insert users', 'SQL', 'V1_1_2__insert_users.sql', -675981003, 'SYS', TO_TIMESTAMP('2023-09-15 09:55:32.077000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 2, 1);
INSERT INTO SYS."flyway_schema_history" ("installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success") VALUES (4, '1.1.3', 'insert pos', 'SQL', 'V1_1_3__insert_pos.sql', 1412467301, 'SYS', TO_TIMESTAMP('2023-09-15 09:55:32.097000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 8, 1);
INSERT INTO SYS."flyway_schema_history" ("installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success") VALUES (5, '1.1.4', 'add notifications table', 'SQL', 'V1_1_4__add_notifications_table.sql', -77479081, 'SYS', TO_TIMESTAMP('2023-09-21 15:31:35.911000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 56, 1);