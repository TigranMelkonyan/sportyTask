INSERT INTO book_type (id, created_on, updated_on, deleted_on, status, name, eligible_for_discount, price_multiplier, bundle_discount)
VALUES
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'NEW_RELEASE', FALSE, 1.0, 1.0),
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'REGULAR', TRUE, 1.0, 0.90),
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'OLD_EDITION', TRUE, 0.8, 0.95);

