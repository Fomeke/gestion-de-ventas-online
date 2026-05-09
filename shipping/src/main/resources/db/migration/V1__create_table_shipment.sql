CREATE TABLE shipment(
    id                  BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id            BIGINT          NOT NULL UNIQUE,
    tracking_number     VARCHAR(200)    NOT NULL UNIQUE,
    carrier             VARCHAR(100)    NOT NULL,
    shipping_address    VARCHAR(300)    NOT NULL,
    est_delivery_date   DATE
);