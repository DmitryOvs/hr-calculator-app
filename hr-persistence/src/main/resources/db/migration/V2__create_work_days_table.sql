-- Создание таблицы рабочих дней
CREATE TABLE IF NOT EXISTS work_days (
                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         employee_id UUID NOT NULL,
                                         work_date DATE NOT NULL,
                                         hours_worked DECIMAL(5, 2) DEFAULT 0,
                                         is_holiday BOOLEAN DEFAULT FALSE,
                                         is_sick_leave BOOLEAN DEFAULT FALSE,
                                         is_vacation BOOLEAN DEFAULT FALSE,
                                         overtime_hours DECIMAL(5, 2) DEFAULT 0,
                                         comment VARCHAR(500),
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         CONSTRAINT fk_work_days_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- Индексы
CREATE INDEX IF NOT EXISTS idx_workdays_employee_date ON work_days(employee_id, work_date);
CREATE INDEX IF NOT EXISTS idx_workdays_date ON work_days(work_date);
CREATE INDEX IF NOT EXISTS idx_workdays_employee ON work_days(employee_id);