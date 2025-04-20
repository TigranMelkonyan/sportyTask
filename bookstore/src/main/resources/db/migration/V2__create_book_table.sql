CREATE TABLE book
(
    id             VARCHAR(36)    NOT NULL PRIMARY KEY,
    created_on     TIMESTAMP      NOT NULL,
    updated_on     TIMESTAMP,
    deleted_on     TIMESTAMP,
    status         ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    title          VARCHAR(255)   NOT NULL,
    author         VARCHAR(255)   NOT NULL,
    base_price     DECIMAL(10, 2) NOT NULL,
    type_id        VARCHAR(36)    NOT NULL,
    stock_quantity INT            NOT NULL,
    CONSTRAINT fk_book_type FOREIGN KEY (type_id) REFERENCES book_type (id)
);
