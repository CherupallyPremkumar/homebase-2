-- V23: Provider Imports - immutable storage of gateway objects for reconciliation

CREATE TABLE provider_objects (
    id VARCHAR(36) PRIMARY KEY,
    gateway_type VARCHAR(20) NOT NULL,
    object_type VARCHAR(50) NOT NULL,
    provider_object_id VARCHAR(255) NOT NULL,
    payload JSONB NOT NULL,
    fetched_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_provider_objects_gateway_object_provider_id
    ON provider_objects(gateway_type, object_type, provider_object_id);

CREATE INDEX idx_provider_objects_gateway_object_type
    ON provider_objects(gateway_type, object_type);
