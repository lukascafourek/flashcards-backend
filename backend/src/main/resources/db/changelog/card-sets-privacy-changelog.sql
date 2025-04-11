-- liquibase formatted sql

-- changeset lukas:1743703325087-1
ALTER TABLE card_sets
    ADD privacy BOOLEAN DEFAULT TRUE;

-- changeset lukas:1743703325087-2
ALTER TABLE card_sets
    ALTER COLUMN privacy SET NOT NULL;

