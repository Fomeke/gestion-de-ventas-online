CREATE TABLE products(
    id  BIGINT NOT NULL AUTO_INCREMENT,
    fullname    VARCHAR(50) NOT NULL,
    price   DECIMAL NOT NULL,
    descr   VARCHAR(150),
    category_id BIGINT NOT NULL,

    CONSTRAINT pk_products  PRIMARY KEY (id),

);