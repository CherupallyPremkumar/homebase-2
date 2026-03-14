-- V3__add_base_entity_columns_to_decisions.sql

ALTER TABLE decisions ADD COLUMN created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE decisions ADD COLUMN created_by VARCHAR(50) DEFAULT 'system';
ALTER TABLE decisions ADD COLUMN last_modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE decisions ADD COLUMN last_modified_by VARCHAR(50) DEFAULT 'system';
ALTER TABLE decisions ADD COLUMN tenant VARCHAR(50) DEFAULT 'default';
ALTER TABLE decisions ADD COLUMN version INT DEFAULT 1;
