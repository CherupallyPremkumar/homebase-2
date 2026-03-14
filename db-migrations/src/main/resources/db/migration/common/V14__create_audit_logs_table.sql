-- Audit Logs table for tracking all admin actions
CREATE TABLE audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(255) NOT NULL,
    performed_by VARCHAR(255) NOT NULL,
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_performed_by ON audit_logs(performed_by);
CREATE INDEX idx_audit_created_at ON audit_logs(created_at);
