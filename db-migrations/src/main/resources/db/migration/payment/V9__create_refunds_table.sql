-- V9__create_refunds_table.sql

CREATE TABLE refunds (
    id VARCHAR(255) PRIMARY KEY,
    payment_transaction_id VARCHAR(255) NOT NULL,
    stripe_refund_id VARCHAR(255) NOT NULL UNIQUE,
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    reason VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_refunds_payment_transaction FOREIGN KEY (payment_transaction_id) REFERENCES payment_transactions(id)
);

CREATE INDEX idx_refunds_stripe_refund_id ON refunds(stripe_refund_id);
CREATE INDEX idx_refunds_charge_id ON refunds(payment_transaction_id);

-- Make payment_transactions immutable (remove mutable refund columns)
ALTER TABLE payment_transactions DROP COLUMN IF EXISTS refund_id;
ALTER TABLE payment_transactions DROP COLUMN IF EXISTS refund_reason;
