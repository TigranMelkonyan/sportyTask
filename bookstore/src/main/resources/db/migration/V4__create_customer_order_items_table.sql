CREATE TABLE customer_order_items
(
    id          VARCHAR(36)    NOT NULL PRIMARY KEY,
    created_on  TIMESTAMP      NOT NULL,
    updated_on  TIMESTAMP,
    deleted_on  TIMESTAMP,
    status      ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    order_id    VARCHAR(36)    NOT NULL,
    book_id     VARCHAR(36)    NOT NULL,
    quantity    INT            NOT NULL,
    unit_price  DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES customer_orders (id),
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES book (id)
);
