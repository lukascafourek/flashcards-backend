-- liquibase formatted sql

-- changeset lukas:1740349818490-1
CREATE TABLE card_sets
(
    id            UUID         NOT NULL,
    name          VARCHAR(255) NOT NULL,
    category      VARCHAR(255) NOT NULL,
    creation_date date         NOT NULL,
    user_id       UUID         NOT NULL,
    CONSTRAINT pk_card_sets PRIMARY KEY (id)
);

-- changeset lukas:1740349818490-2
CREATE TABLE cards
(
    id          UUID         NOT NULL,
    front       VARCHAR(255) NOT NULL,
    back        VARCHAR(255) NOT NULL,
    card_set_id UUID         NOT NULL,
    CONSTRAINT pk_cards PRIMARY KEY (id)
);

-- changeset lukas:1740349818490-3
CREATE TABLE favorite_sets
(
    card_set_id UUID NOT NULL,
    user_id     UUID NOT NULL
);

-- changeset lukas:1740349818490-4
CREATE TABLE pictures
(
    card_id UUID  NOT NULL,
    picture BYTEA NOT NULL,
    CONSTRAINT pk_pictures PRIMARY KEY (card_id)
);

-- changeset lukas:1740349818490-5
CREATE TABLE set_statistics
(
    id                   UUID    NOT NULL,
    sets_learned         INTEGER NOT NULL,
    cards_learned        INTEGER NOT NULL,
    cards_to_learn_again INTEGER NOT NULL,
    base_method_mode     INTEGER NOT NULL,
    multiple_choice_mode INTEGER NOT NULL,
    connection_mode      INTEGER NOT NULL,
    card_set_id          UUID    NOT NULL,
    user_id              UUID    NOT NULL,
    CONSTRAINT pk_set_statistics PRIMARY KEY (id)
);

-- changeset lukas:1740349818490-6
CREATE TABLE tokens
(
    user_id         UUID                        NOT NULL,
    reset_token     VARCHAR(255)                NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (user_id)
);

-- changeset lukas:1740349818490-7
CREATE TABLE user_statistics
(
    user_id                    UUID    NOT NULL,
    total_sets_learned         INTEGER NOT NULL,
    total_cards_learned        INTEGER NOT NULL,
    total_cards_to_learn_again INTEGER NOT NULL,
    sets_created               INTEGER NOT NULL,
    cards_created              INTEGER NOT NULL,
    base_method_modes          INTEGER NOT NULL,
    multiple_choice_modes      INTEGER NOT NULL,
    connection_modes           INTEGER NOT NULL,
    CONSTRAINT pk_user_statistics PRIMARY KEY (user_id)
);

-- changeset lukas:1740349818490-8
CREATE TABLE users
(
    id       UUID         NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

-- changeset lukas:1740349818490-9
ALTER TABLE tokens
    ADD CONSTRAINT uc_tokens_reset_token UNIQUE (reset_token);

-- changeset lukas:1740349818490-10
ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

-- changeset lukas:1740349818490-11
ALTER TABLE cards
    ADD CONSTRAINT FK_CARDS_ON_CARD_SET FOREIGN KEY (card_set_id) REFERENCES card_sets (id);

-- changeset lukas:1740349818490-12
ALTER TABLE card_sets
    ADD CONSTRAINT FK_CARD_SETS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset lukas:1740349818490-13
ALTER TABLE pictures
    ADD CONSTRAINT FK_PICTURES_ON_CARD FOREIGN KEY (card_id) REFERENCES cards (id);

-- changeset lukas:1740349818490-14
ALTER TABLE set_statistics
    ADD CONSTRAINT FK_SET_STATISTICS_ON_CARD_SET FOREIGN KEY (card_set_id) REFERENCES card_sets (id);

-- changeset lukas:1740349818490-15
ALTER TABLE set_statistics
    ADD CONSTRAINT FK_SET_STATISTICS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset lukas:1740349818490-16
ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset lukas:1740349818490-17
ALTER TABLE user_statistics
    ADD CONSTRAINT FK_USER_STATISTICS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset lukas:1740349818490-18
ALTER TABLE favorite_sets
    ADD CONSTRAINT fk_favset_on_card_set FOREIGN KEY (card_set_id) REFERENCES card_sets (id);

-- changeset lukas:1740349818490-19
ALTER TABLE favorite_sets
    ADD CONSTRAINT fk_favset_on_user FOREIGN KEY (user_id) REFERENCES users (id);

