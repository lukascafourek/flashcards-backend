-- liquibase formatted sql

-- changeset lukas:1741891791322-1
ALTER TABLE set_statistics
    ALTER COLUMN base_method_mode SET DEFAULT 0;

-- changeset lukas:1741891791322-2
ALTER TABLE user_statistics
    ALTER COLUMN base_method_modes SET DEFAULT 0;

-- changeset lukas:1741891791322-3
ALTER TABLE user_statistics
    ALTER COLUMN cards_created SET DEFAULT 0;

-- changeset lukas:1741891791322-4
ALTER TABLE set_statistics
    ALTER COLUMN cards_learned SET DEFAULT 0;

-- changeset lukas:1741891791322-5
ALTER TABLE set_statistics
    ALTER COLUMN cards_to_learn_again SET DEFAULT 0;

-- changeset lukas:1741891791322-6
ALTER TABLE set_statistics
    ALTER COLUMN connection_mode SET DEFAULT 0;

-- changeset lukas:1741891791322-7
ALTER TABLE user_statistics
    ALTER COLUMN connection_modes SET DEFAULT 0;

-- changeset lukas:1741891791322-8
ALTER TABLE set_statistics
    ALTER COLUMN multiple_choice_mode SET DEFAULT 0;

-- changeset lukas:1741891791322-9
ALTER TABLE user_statistics
    ALTER COLUMN multiple_choice_modes SET DEFAULT 0;

-- changeset lukas:1741891791322-10
ALTER TABLE user_statistics
    ALTER COLUMN sets_created SET DEFAULT 0;

-- changeset lukas:1741891791322-11
ALTER TABLE set_statistics
    ALTER COLUMN sets_learned SET DEFAULT 0;

-- changeset lukas:1741891791322-12
ALTER TABLE user_statistics
    ALTER COLUMN total_cards_learned SET DEFAULT 0;

-- changeset lukas:1741891791322-13
ALTER TABLE user_statistics
    ALTER COLUMN total_cards_to_learn_again SET DEFAULT 0;

-- changeset lukas:1741891791322-14
ALTER TABLE user_statistics
    ALTER COLUMN total_sets_learned SET DEFAULT 0;

