-- liquibase formatted sql

-- changeset lukas:1742591500414-1
ALTER TABLE cards
    DROP COLUMN back;
ALTER TABLE cards
    DROP COLUMN front;

-- changeset lukas:1742591500414-2
ALTER TABLE cards
    ADD back VARCHAR(510) NOT NULL;

-- changeset lukas:1742591500414-4
ALTER TABLE cards
    ADD front VARCHAR(510) NOT NULL;

