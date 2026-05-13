CREATE TABLE inventory(
    id            BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id    BIGINT          NOT NULL,
    stock         INT             NOT NULL
);