-- V20: Add gateway_type to payment_transactions for dynamic gateway tracking
ALTER TABLE payment_transactions ADD COLUMN gateway_type VARCHAR(20);

COMMENT ON COLUMN payment_transactions.gateway_type IS 'The payment gateway used for this transaction (e.g., STRIPE, RAZORPAY)';
