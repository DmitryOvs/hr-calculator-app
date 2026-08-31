-- Тестовые данные для разработки
INSERT INTO employees (id, full_name, position, phone_number, personnel_number, hourly_rate, is_north_region, north_experience_years, hiring_date, is_active, email)
VALUES
    (gen_random_uuid(), 'Иван Иванов', 'Senior Developer', '+7-999-123-45-67', 'EMP-001', 1500.00, true, 5, '2020-01-15 10:00:00', true, 'ivan.ivanov@company.ru'),
    (gen_random_uuid(), 'Петр Петров', 'Team Lead', '+7-999-234-56-78', 'EMP-002', 2000.00, false, 0, '2019-03-20 10:00:00', true, 'petr.petrov@company.ru'),
    (gen_random_uuid(), 'Сергей Сергеев', 'Developer', '+7-999-345-67-89', 'EMP-003', 1200.00, true, 3, '2021-06-10 10:00:00', true, 'sergey.sergeev@company.ru'),
    (gen_random_uuid(), 'Анна Смирнова', 'QA Engineer', '+7-999-456-78-90', 'EMP-004', 1100.00, false, 0, '2022-02-01 10:00:00', true, 'anna.smirnova@company.ru'),
    (gen_random_uuid(), 'Михаил Кузнецов', 'DevOps', '+7-999-567-89-01', 'EMP-005', 1800.00, true, 7, '2018-11-05 10:00:00', true, 'mikhail.kuznetsov@company.ru');