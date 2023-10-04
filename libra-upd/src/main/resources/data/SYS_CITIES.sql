create table CITIES
(
    ID        NUMBER(19) generated as identity
        primary key,
    CITY_NAME VARCHAR2(255 char)
)
/

INSERT INTO SYS.CITIES (ID, CITY_NAME) VALUES (1, 'city1_name');
INSERT INTO SYS.CITIES (ID, CITY_NAME) VALUES (2, 'city2_name');
INSERT INTO SYS.CITIES (ID, CITY_NAME) VALUES (3, 'city3_name');
INSERT INTO SYS.CITIES (ID, CITY_NAME) VALUES (21, 'Somename');
INSERT INTO SYS.CITIES (ID, CITY_NAME) VALUES (22, 'City new');