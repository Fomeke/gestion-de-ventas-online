CREATE TABLE users (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(20)     NOT NULL UNIQUE,
    full_name       VARCHAR(150)    NOT NULL,
    password        VARCHAR(50)     NOT NULL,
    correo          VARCHAR(80)     NOT NULL UNIQUE,
    phone           VARCHAR(20)     NULL
);