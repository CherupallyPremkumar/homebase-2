-- V24: Payouts - gateway-agnostic payout tracking for Admin UI and reconciliation

CREATE TABLE payouts (
    id VARCHAR(36) PRIMARY KEY,
    gateway_type VARCHAR(20) NOT NULL,
    provider_payout_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payout_at TIMESTAMP,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    gross_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    fee_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    net_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_payouts_gateway_provider_payout_id
    ON payouts(gateway_type, provider_payout_id);

CREATE INDEX idx_payouts_gateway_payout_at
    ON payouts(gateway_type, payout_at);

CREATE TABLE payout_lines (
    id VARCHAR(36) PRIMARY KEY,
    payout_id VARCHAR(36) NOT NULL REFERENCES payouts(id) ON DELETE CASCADE,

    provider_balance_txn_id VARCHAR(255),

    internal_payment_transaction_id VARCHAR(36),
    internal_refund_id VARCHAR(255),

    line_type VARCHAR(50) NOT NULL,

    gross_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    fee_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    net_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,

    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    occurred_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payout_lines_payment_txn
        FOREIGN KEY (internal_payment_transaction_id) REFERENCES payment_transactions(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_payout_lines_refund
        FOREIGN KEY (internal_refund_id) REFERENCES refunds(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_payout_lines_payout_id
    ON payout_lines(payout_id);

CREATE INDEX idx_payout_lines_provider_balance_txn_id
    ON payout_lines(provider_balance_txn_id);

CREATE INDEX idx_payout_lines_internal_payment_txn
    ON payout_lines(internal_payment_transaction_id);

CREATE INDEX idx_payout_lines_internal_refund
    ON payout_lines(internal_refund_id);
