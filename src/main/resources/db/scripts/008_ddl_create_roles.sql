-- liquibase formatted sql

-- changeset oyvey1984:8

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    role_id INT REFERENCES roles(id),
    user_id INT REFERENCES users(id)
);