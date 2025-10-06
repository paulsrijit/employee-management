-- Create the employees table
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    department VARCHAR(255),
    salary DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups (automatically created with UNIQUE constraint)
-- CREATE INDEX idx_employees_email ON employees(email);

-- Optional: Create indexes on frequently queried columns
CREATE INDEX idx_employees_department ON employees(department);
CREATE INDEX idx_employees_created_at ON employees(created_at);

-- Add comments for documentation
COMMENT ON TABLE employees IS 'Employee information table';
COMMENT ON COLUMN employees.id IS 'Primary key, auto-generated';
COMMENT ON COLUMN employees.email IS 'Unique email address';
COMMENT ON COLUMN employees.salary IS 'Employee salary';
COMMENT ON COLUMN employees.created_at IS 'Timestamp when the record was created';

-- Sample DML: Insert 10 employees
INSERT INTO employees (name, email, department, salary, created_at) VALUES
('John Smith', 'john.smith@company.com', 'Engineering', 85000.00, '2024-01-15 09:30:00'),
('Sarah Johnson', 'sarah.johnson@company.com', 'Marketing', 72000.00, '2024-02-20 10:15:00'),
('Michael Chen', 'michael.chen@company.com', 'Engineering', 92000.00, '2024-03-10 14:20:00'),
('Emily Davis', 'emily.davis@company.com', 'Human Resources', 68000.00, '2024-03-25 11:45:00'),
('Robert Wilson', 'robert.wilson@company.com', 'Sales', 78000.00, '2024-04-05 08:30:00'),
('Lisa Anderson', 'lisa.anderson@company.com', 'Finance', 81000.00, '2024-05-12 13:00:00'),
('David Martinez', 'david.martinez@company.com', 'Engineering', 95000.00, '2024-06-18 09:15:00'),
('Jessica Taylor', 'jessica.taylor@company.com', 'Marketing', 75000.00, '2024-07-22 10:30:00'),
('James Brown', 'james.brown@company.com', 'Sales', 82000.00, '2024-08-30 15:45:00'),
('Maria Garcia', 'maria.garcia@company.com', 'Finance', 88000.00, '2024-09-14 12:20:00');