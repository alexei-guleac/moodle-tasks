INSERT INTO cities (city_name)
VALUES ('city1_name');
INSERT INTO cities (city_name)
VALUES ('city2_name');
INSERT INTO cities (city_name)
VALUES ('city3_name');

INSERT INTO pos (pos_name, telephone, cellphone, address, city_id, model, brand, connection_type_id,
                 morning_opening, morning_closing, afternoon_opening, afternoon_closing,
                 days_closed, insert_date)
VALUES ('Name', '373386787', '022555757', 'Address1', 1, 'model1', 'brand1', 1, true, false, false,
        false, 1, current_timestamp);

INSERT INTO pos (pos_name, telephone, cellphone, address, city_id, model, brand, connection_type_id,
                 morning_opening, morning_closing, afternoon_opening, afternoon_closing,
                 days_closed, insert_date)
VALUES ('Name2', '373647647', '022796945', 'Address2', 2, 'model2', 'brand2', 2, true, false, true,
        false, 8, current_timestamp);

INSERT INTO pos (pos_name, telephone, cellphone, address, city_id, model, brand, connection_type_id,
                 morning_opening, morning_closing, afternoon_opening, afternoon_closing,
                 days_closed, insert_date)
VALUES ('Name3', '373348554', '022452535', 'Address3', 3, 'model3', 'brand3', 1, false, false, true,
        true, 4, current_timestamp);

