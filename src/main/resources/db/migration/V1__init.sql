CREATE TABLE feature
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        LONGTEXT NOT NULL UNIQUE,
    description LONGTEXT NULL,
    enabled     BOOL DEFAULT FALSE,
    expiration  DATETIME NULL
)
