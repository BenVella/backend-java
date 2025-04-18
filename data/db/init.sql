\c app_db

CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    auth_id VARCHAR(255) NOT NULL UNIQUE,
    code CHAR(8) NOT NULL UNIQUE,
    nickname VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);