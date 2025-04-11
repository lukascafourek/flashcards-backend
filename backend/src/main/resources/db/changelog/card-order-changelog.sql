-- liquibase formatted sql

-- changeset lukas:1743866719585-1
ALTER TABLE cards
    ADD card_order INTEGER;

