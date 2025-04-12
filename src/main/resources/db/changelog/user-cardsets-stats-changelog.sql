-- liquibase formatted sql

-- changeset lukas:1743539167735-1
ALTER TABLE card_sets
    ADD description VARCHAR(255);

-- changeset lukas:1743539167735-2
ALTER TABLE users
    ADD number_of_images INTEGER DEFAULT 0;

-- changeset lukas:1743539167735-3
ALTER TABLE users
    ALTER COLUMN number_of_images SET NOT NULL;

-- changeset lukas:1743539167735-4
ALTER TABLE set_statistics
    ADD true_false_mode INTEGER DEFAULT 0;

-- changeset lukas:1743539167735-5
ALTER TABLE set_statistics
    ALTER COLUMN true_false_mode SET NOT NULL;

-- changeset lukas:1743539167735-6
ALTER TABLE user_statistics
    ADD true_false_modes INTEGER DEFAULT 0;

-- changeset lukas:1743539167735-7
ALTER TABLE user_statistics
    ALTER COLUMN true_false_modes SET NOT NULL;

-- changeset lukas:1743539167735-8
ALTER TABLE set_statistics
    DROP COLUMN connection_mode;

-- changeset lukas:1743539167735-9
ALTER TABLE user_statistics
    DROP COLUMN connection_modes;

