INSERT INTO statuses (status)
VALUES ('NEW');
INSERT INTO statuses (status)
VALUES ('ASSIGNED');
INSERT INTO statuses (status)
VALUES ('IN_PROGRESS');
INSERT INTO statuses (status)
VALUES ('PENDING');

INSERT INTO connection_types (connection_type)
VALUES ('REMOTE');
INSERT INTO connection_types (connection_type)
VALUES ('WIFI');

INSERT INTO issue_types (insert_date, issue_level, issue_type_name)
VALUES (current_timestamp, 'FIRST', 'TECHNICAL_ISSUE');
INSERT INTO issue_types (insert_date, issue_level, issue_type_name)
VALUES (current_timestamp, 'SECOND', 'NON_TECH_ISSUE');

INSERT INTO users_types (user_type)
VALUES ('ADMIN');
INSERT INTO users_types (user_type)
VALUES ('TECHNICAL_GROUP');
