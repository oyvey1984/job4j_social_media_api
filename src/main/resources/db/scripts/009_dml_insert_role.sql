-- liquibase formatted sql

-- changeset oyvey1984:9

insert into roles (name) VALUES ('ROLE_USER'),
                                ('ROLE_ADMIN'),
                                ('ROLE_MODERATOR');
