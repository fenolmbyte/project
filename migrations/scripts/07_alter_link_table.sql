--liquibase formatted sql

--changeset fenolmbyte:0_alter_link_table
ALTER TABLE link
    ALTER COLUMN last_update_time SET NOT NULL;