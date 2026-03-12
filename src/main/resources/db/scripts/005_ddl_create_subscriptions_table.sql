-- liquibase formatted sql

-- changeset oyvey1984:5

CREATE TABLE subscriptions (
    id SERIAL PRIMARY KEY,
    follower_id INT  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    following_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(follower_id, following_id)
);