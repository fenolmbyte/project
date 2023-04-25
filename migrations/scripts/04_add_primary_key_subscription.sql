--liquibase formatted sql

--changeset fenolmbyte:CRS-4_add_primary_key_subscription
ALTER  TABLE  subscription  ADD  PRIMARY  KEY  (chat_id, link_id);
