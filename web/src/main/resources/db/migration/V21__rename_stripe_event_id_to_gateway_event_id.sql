-- Rename stripe_event_id column to gateway_event_id for multi-gateway support
-- This column stores event IDs from both Stripe and Razorpay webhooks

ALTER TABLE payment_transactions 
RENAME COLUMN stripe_event_id TO gateway_event_id;

-- Update index name if it exists
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM pg_indexes 
        WHERE tablename = 'payment_transactions' 
        AND indexname LIKE '%stripe_event_id%'
    ) THEN
        ALTER INDEX IF EXISTS idx_payment_transactions_stripe_event_id 
        RENAME TO idx_payment_transactions_gateway_event_id;
    END IF;
END $$;
