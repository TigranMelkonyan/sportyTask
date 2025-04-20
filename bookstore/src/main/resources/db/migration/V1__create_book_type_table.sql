CREATE TABLE book_type
(
    id                   VARCHAR(36)  NOT NULL PRIMARY KEY,
    created_on           TIMESTAMP    NOT NULL,
    updated_on           TIMESTAMP,
    deleted_on           TIMESTAMP,
    status               ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    name                 VARCHAR(255) NOT NULL UNIQUE,
    eligible_for_loyalty BOOLEAN      NOT NULL,
    price_multiplier DOUBLE NOT NULL,
    bundle_discount DOUBLE NOT NULL
);