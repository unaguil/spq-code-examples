CREATE DATABASE IF NOT EXISTS messages;
USE messages;

CREATE TABLE IF NOT EXISTS user (
    login VARCHAR(255) NOT NULL PRIMARY KEY,
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255),
    text TEXT,
    timestamp BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user(login)
);
