INSERT INTO book_type (id, created_on, updated_on, deleted_on, status, name, price_multiplier, bundle_discount)
VALUES
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'NEW_RELEASE',  1.0, 1.0),
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'REGULAR',  1.0, 0.90),
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'OLD_EDITION', 0.8, 0.95);

