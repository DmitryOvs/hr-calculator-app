-- Очистка таблиц перед заполнением (для разработки)
TRUNCATE TABLE salary_statements CASCADE;
TRUNCATE TABLE employees CASCADE;

-- Добавление тестовых сотрудников
INSERT INTO employees (tab_number, full_name, position, phone_number, hourly_rate, district_coefficient, northern_percentage, work_method, harmful_conditions_percent, travel_compensation_rate)
VALUES
    (228, 'Пыраков А.Н.', 'Начальник ЛЭС', '+79991234567', 250.00, 2.00, 100.00, 'VAXTA', 0.00, 5747.00),
    (13, 'Авдошин А.В.', 'Мастер ЛЭС', '+79997654321', 200.00, 2.00, 100.00, 'VAXTA', 0.00, 5747.00),
    (123, 'Ларин А.В.', 'Главный специалист АСУП', '+79990001122', 180.00, 2.00, 100.00, 'VAXTA', 0.00, 5747.00),
    (55, 'Овсяников Д.Е.', 'Оператор АГРС', '+79993334455', 174.00, 2.00, 100.00, 'VAXTA', 6.60, 5747.00);
