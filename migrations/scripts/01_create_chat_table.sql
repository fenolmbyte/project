--liquibase formatted sql

--changeset fenolmbyte:CRS-1_create_chat_table
CREATE TABLE IF NOT EXISTS chat
(
    id BIGINT PRIMARY KEY
);
