-- User Table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Habit Table
CREATE TABLE habits (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        target_days_per_week INT CHECK (target_days_per_week BETWEEN 1 AND 7),
                        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Habit Logs Table
CREATE TABLE habit_logs (
                            id BIGSERIAL PRIMARY KEY,
                            habit_id BIGINT REFERENCES habits(id) ON DELETE CASCADE,
                            log_date DATE NOT NULL,
                            completed BOOLEAN NOT NULL,
                            UNIQUE (habit_id, log_date) -- Mandatory Constraint [cite: 216]
);