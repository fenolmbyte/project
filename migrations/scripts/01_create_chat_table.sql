--liquibase formatted sql

--changeset fenolmbyte:01_create_chat_table
CREATE TABLE IF NOT EXISTS chat
(
    id BIGINT PRIMARY KEY
);
