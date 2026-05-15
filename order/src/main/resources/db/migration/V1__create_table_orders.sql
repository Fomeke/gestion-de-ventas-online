CREATE TABLE orders (
    id                  BIGINT              AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT              NOT NULL,
    order_date          DATETIME            NOT NULL,
    total               DECIMAL(10,2)       NOT NULL,
    status              VARCHAR(20)         NOT NULL,
    payment_method      VARCHAR(20)         NOT NULL
);

CREATE TABLE order_item (
    id                  BIGINT              AUTO_INCREMENT PRIMARY KEY,
    order_id            BIGINT              NOT NULL,
    product_id          BIGINT              NOT NULL,
    quantity            INT                 NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);