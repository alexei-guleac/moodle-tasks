INSERT INTO dm_institutions (additional_info, code, name) VALUES ('ADDITIONAL_INFO', '07878', 'Institution 1 name');

INSERT INTO dm_user_roles (user_role) VALUES ('ADMIN');
INSERT INTO dm_user_roles (user_role) VALUES ('CED_OPERATOR');
INSERT INTO dm_user_roles (user_role) VALUES ('BANK_OPERATOR');

INSERT INTO dm_users_details (institution_id, name, email, login, password, encoded_password, surname, patronymic, is_enabled, user_role_id)
VALUES (1, 'Name', 'Email', 'admin', 'password', '{bcrypt}$2a$10$9TyzlHEH833VBl5R6RN2KOLIn/jPlWkVfStwA7DPp1ltmHHHIe3jK', 'Surname','Patronymic',true, 1);


INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('SERVICE_REPORT', 'TypeCode','Description', true, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('NETWORK', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('SAFETY', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('CHANGE', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('BACKUP', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('REPORT_SLA', 'TypeCode','Description', true, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('DESIGN', 'TypeCode','Description', true, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('ANALYSIS', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('TRANSITION', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('PRODUCTION', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('TEST', 'TypeCode','Description', false, false);
INSERT INTO dm_document_types (name, code, type_description, is_macro, is_date_grouped) VALUES ('MONITORING', 'TypeCode','Description', false, false);
