-- liquibase formatted sql

-- changeset lukas:1743884894381-1
ALTER TABLE users
    ADD role VARCHAR(255) DEFAULT 'USER';

-- changeset lukas:1743884894381-2
ALTER TABLE users
    ALTER COLUMN role SET NOT NULL;

