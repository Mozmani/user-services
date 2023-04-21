CREATE TABLE IF NOT EXISTS users (
    id uuid PRIMARY KEY,
    username VARCHAR(64) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_on TIMESTAMP NOT NULL,
    updated_on TIMESTAMP NOT NULL,
    last_login TIMESTAMP
);
-- Unique write and read index.
CREATE UNIQUE INDEX users_lowercase_username_idx ON users ((lower(username)));
CREATE UNIQUE INDEX users_lowercase_email_idx ON users ((lower(email)));

CREATE TABLE IF NOT EXISTS roles (
    id uuid PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);
-- Unique write and read index.
CREATE UNIQUE INDEX roles_uppercase_role_name_idx ON roles ((upper(role_name)));

CREATE TABLE IF NOT EXISTS user_roles (
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES users(id),
    role_id uuid REFERENCES roles(id)
);
-- Read indexes
CREATE INDEX user_roles_user_id_idx ON user_roles(user_id);
CREATE INDEX user_roles_role_id_idx ON user_roles(role_id);
-- Unique user <-> role write.
CREATE UNIQUE INDEX user_roles_user_id_role_id_idx ON user_roles(user_id, role_id);

-- Adding initial idempotent roles.
INSERT INTO roles (id, role_name)
VALUES
    ('90667212-e148-4c41-af7c-b8000d7c6327', 'ADMIN_SUPER_USER'),
    ('73ee4807-218a-4c48-9077-465378250a3c', 'ADMIN_CONTRIBUTOR'),
    ('c3774644-6ed2-4850-bd28-965828d2310a', 'EMPLOYEE_USER'),
    ('c411e9b2-1280-49e1-82f3-b30b76021555', 'CUSTOMER_USER');