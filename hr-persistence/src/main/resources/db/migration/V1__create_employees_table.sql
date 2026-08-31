-- Создание таблицы сотрудников
CREATE TABLE IF NOT EXISTS employees (
                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         full_name VARCHAR(255) NOT NULL,
                                         position VARCHAR(100),
                                         phone_number VARCHAR(20),
                                         personnel_number VARCHAR(50) UNIQUE NOT NULL,
                                         hourly_rate DECIMAL(10, 2) NOT NULL,
                                         is_north_region BOOLEAN DEFAULT FALSE,
                                         north_experience_years INT DEFAULT 0,
                                         hiring_date TIMESTAMP,
                                         is_active BOOLEAN DEFAULT TRUE,
                                         email VARCHAR(100),
                                         birth_date TIMESTAMP,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы
CREATE INDEX IF NOT EXISTS idx_employees_personnel_number ON employees(personnel_number);
CREATE INDEX IF NOT EXISTS idx_employees_full_name ON employees(full_name);
CREATE INDEX IF NOT EXISTS idx_employees_position ON employees(position);
CREATE INDEX IF NOT EXISTS idx_employees_active ON employees(is_active);