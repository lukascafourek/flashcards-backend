-- liquibase formatted sql

-- changeset lukas:1741621566962-1
ALTER TABLE users
    DROP COLUMN provider_id;

