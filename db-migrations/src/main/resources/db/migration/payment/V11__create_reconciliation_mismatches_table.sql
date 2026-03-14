-- V11__create_reconciliation_mismatches_table.sql

CREATE TABLE reconciliation_mismatches (
    id VARCHAR(255) PRIMARY KEY,
    stripe_charge_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(255),
    mismatch_type VARCHAR(50) NOT NULL,
    stripe_amount DECIMAL(19, 2),
    db_amount DECIMAL(19, 2),
    stripe_status VARCHAR(50),
    db_status VARCHAR(50),
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_at TIMESTAMP,
    resolution_notes TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_recon_mismatch_stripe_charge ON reconciliation_mismatches(stripe_charge_id);
CREATE INDEX idx_recon_mismatch_resolved ON reconciliation_mismatches(resolved);
