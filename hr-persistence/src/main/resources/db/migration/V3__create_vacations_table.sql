-- Создание таблицы отпусков
CREATE TABLE IF NOT EXISTS vacations (
                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         employee_id UUID NOT NULL,
                                         start_date DATE NOT NULL,
                                         end_date DATE NOT NULL,
                                         days_count INT NOT NULL,
                                         is_paid BOOLEAN DEFAULT TRUE,
                                         is_approved BOOLEAN DEFAULT FALSE,
                                         comment VARCHAR(500),
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         CONSTRAINT fk_vacations_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- Индексы
CREATE INDEX IF NOT EXISTS idx_vacations_employee_dates ON vacations(employee_id, start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_vacations_dates ON vacations(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_vacations_employee ON vacations(employee_id);