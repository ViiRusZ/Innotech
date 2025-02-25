CREATE TABLE IF NOT EXISTS task(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(128) NOT NULL,
    description VARCHAR,
    user_id BIGINT CHECK ( user_id > 0 )
);