-- liquibase formatted sql

-- changeset lukas:1743970498940-1
ALTER TABLE cards
    ALTER COLUMN card_order SET NOT NULL;

