-- liquibase formatted sql

-- changeset oyvey1984:7

ALTER TABLE users ADD CONSTRAINT users_email_unique UNIQUE (email);