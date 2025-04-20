CREATE TABLE user (
                       id VARCHAR(36) NOT NULL UNIQUE,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255),
                       loyalty_points INT DEFAULT 0,
                       active BOOLEAN DEFAULT TRUE,

                       created_on DATETIME(6) NOT NULL,
                       updated_on DATETIME(6),
                       deleted_on DATETIME(6),
                       status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',

                       PRIMARY KEY (id)
);