CREATE TABLE carts (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT          NOT NULL UNIQUE
);

CREATE TABLE cart_items(
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    product_id          BIGINT          NOT NULL,
    quantity            INTEGER         NOT NULL,
    cart_id             BIGINT,
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE
);