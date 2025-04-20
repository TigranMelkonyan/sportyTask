CREATE TABLE customer_orders
(
    id                     VARCHAR(36)    NOT NULL PRIMARY KEY,
    created_on             TIMESTAMP      NOT NULL,
    updated_on             TIMESTAMP,
    deleted_on             TIMESTAMP,
    status                 ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    customer_id            VARCHAR(36)    NOT NULL,
    total_price            DECIMAL(10, 2) NOT NULL,
    total_items            INT            NOT NULL,
    loyalty_points_applied BOOLEAN        NOT NULL DEFAULT FALSE,
    order_status           ENUM('PENDING', 'COMPLETED', 'CANCELLED', 'SHIPPED') NOT NULL DEFAULT 'PENDING'
);
