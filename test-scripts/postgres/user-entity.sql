-- Create the main users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create the user_roles collection table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255),
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
);

-- Create index on user_id for better join performance
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);

-- Optional: Add comments for documentation
COMMENT ON TABLE users IS 'Stores user account information';
COMMENT ON TABLE user_roles IS 'Stores user roles as a collection (one-to-many relationship)';
COMMENT ON COLUMN users.username IS 'Unique username for login';
COMMENT ON COLUMN users.password IS 'Encrypted password';
COMMENT ON COLUMN user_roles.role IS 'Individual role assigned to a user';