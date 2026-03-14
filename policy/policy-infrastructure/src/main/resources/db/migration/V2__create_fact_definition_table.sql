CREATE TABLE IF NOT EXISTS fact_definition (
    id VARCHAR(255) PRIMARY KEY,
    module_name VARCHAR(255) NOT NULL,
    entity_name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    attribute VARCHAR(255) NOT NULL,
    data_type VARCHAR(50) NOT NULL,
    created_time TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    tenant VARCHAR(50),
    test_entity BOOLEAN DEFAULT FALSE,
    version BIGINT DEFAULT 0
);
