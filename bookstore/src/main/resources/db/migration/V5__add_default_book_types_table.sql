INSERT INTO book_type (id, created_on, updated_on, deleted_on, status, name, eligible_for_loyalty, price_multiplier, bundle_discount)
VALUES
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'NEW_RELEASE', TRUE, 1.0, 1.0),
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'OLD_EDITION', FALSE, 1.0, 0.9),
    (UUID(), NOW(), NOW(), NULL, 'ACTIVE', 'REGULAR', TRUE, 0.8, 0.95);
