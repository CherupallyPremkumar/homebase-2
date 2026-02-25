-- V25: Reconciliation Runs - gateway-agnostic mismatch workflow

CREATE TABLE reconciliation_runs (
    id VARCHAR(36) PRIMARY KEY,
    gateway_type VARCHAR(20) NOT NULL,

    period_start DATE NOT NULL,
    period_end DATE NOT NULL,

    status VARCHAR(20) NOT NULL,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,

    summary JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reconciliation_runs_gateway_period
    ON reconciliation_runs(gateway_type, period_start, period_end);

CREATE TABLE reconciliation_items (
    id VARCHAR(36) PRIMARY KEY,
    run_id VARCHAR(36) NOT NULL REFERENCES reconciliation_runs(id) ON DELETE CASCADE,

    category VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,

    provider_ref VARCHAR(255),
    internal_ref VARCHAR(255),

    expected_amount NUMERIC(10,2),
    actual_amount NUMERIC(10,2),
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',

    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',

    notes TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    resolved_by VARCHAR(255)
);

CREATE INDEX idx_reconciliation_items_run_status
    ON reconciliation_items(run_id, status);

CREATE INDEX idx_reconciliation_items_category
    ON reconciliation_items(category);
