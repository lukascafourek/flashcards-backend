-- liquibase formatted sql

-- changeset lukas:1740522020685-2
ALTER TABLE users
    ADD provider VARCHAR(255);
ALTER TABLE users
    ADD provider_id VARCHAR(255);

-- changeset lukas:1740522020685-3
ALTER TABLE users
    ALTER COLUMN provider SET NOT NULL;

-- changeset lukas:1740522020685-1
ALTER TABLE users
    ALTER COLUMN password DROP NOT NULL;

