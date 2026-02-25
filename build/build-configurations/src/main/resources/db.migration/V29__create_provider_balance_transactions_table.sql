-- V29: Provider Balance Transactions - normalized gateway-side movements (fees/net/payout truth)

CREATE TABLE provider_balance_transactions (
    id VARCHAR(36) PRIMARY KEY,
    gateway_type VARCHAR(20) NOT NULL,
    provider_balance_txn_id VARCHAR(255) NOT NULL,

    txn_type VARCHAR(50) NOT NULL,

    source_object_type VARCHAR(50),
    source_object_id VARCHAR(255),

    provider_payout_id VARCHAR(255),

    currency VARCHAR(3) NOT NULL DEFAULT 'INR',

    amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    fee_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    net_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,

    occurred_at TIMESTAMP,

    payload JSONB NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_provider_balance_txn_gateway_provider_id
    ON provider_balance_transactions(gateway_type, provider_balance_txn_id);

CREATE INDEX idx_provider_balance_txn_gateway_occurred_at
    ON provider_balance_transactions(gateway_type, occurred_at);

CREATE INDEX idx_provider_balance_txn_provider_payout_id
    ON provider_balance_transactions(provider_payout_id);

CREATE INDEX idx_provider_balance_txn_source_object
    ON provider_balance_transactions(source_object_type, source_object_id);
