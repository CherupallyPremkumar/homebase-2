-- V3: Add reconciliation columns to payment_transactions
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS stripe_payment_intent_id VARCHAR(255);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS currency VARCHAR(3) DEFAULT 'INR';
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS stripe_fee NUMERIC(10,2);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS net_amount NUMERIC(10,2);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS payment_method_type VARCHAR(50);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS refund_id VARCHAR(255);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS refund_reason VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_payment_tx_order ON payment_transactions(order_id);
CREATE INDEX IF NOT EXISTS idx_payment_tx_pi ON payment_transactions(stripe_payment_intent_id);
CREATE INDEX IF NOT EXISTS idx_payment_tx_status ON payment_transactions(status);
