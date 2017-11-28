/* Gigantic Granite database */

DROP DATABASE IF EXISTS giganticgranite;
CREATE DATABASE giganticgranite;
USE giganticgranite;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id         INT             NOT NULL AUTO_INCREMENT,
    username        VARCHAR(32)     NOT NULL,
    email           VARCHAR(128)    NOT NULL,
    password_hash   VARCHAR(256)    NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE images (
    image_id            INT             NOT NULL AUTO_INCREMENT,
    user_id             INT             NOT NULL,
    date_of_addition    DATETIME        NOT NULL DEFAULT NOW(),
    image_path          VARCHAR(256)    NOT NULL,
    actors              VARCHAR(1024)   NOT NULL,
    PRIMARY KEY (image_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- DROP USER 'web'@'localhost';

CREATE USER 'web'@'localhost' IDENTIFIED BY 'qwfJMCV7658FDJHFG';
GRANT SELECT, INSERT ON giganticgranite.* TO 'web'@'localhost';
