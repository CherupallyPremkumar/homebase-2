-- V28: Make refund_requests gateway-agnostic

ALTER TABLE refund_requests
    ADD COLUMN IF NOT EXISTS gateway_type VARCHAR(20) NOT NULL DEFAULT 'stripe';

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'refund_requests'
          AND column_name = 'stripe_refund_id'
    ) THEN
        ALTER TABLE refund_requests RENAME COLUMN stripe_refund_id TO gateway_refund_id;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_refund_requests_gateway_refund_id
    ON refund_requests(gateway_refund_id);

CREATE INDEX IF NOT EXISTS idx_refund_requests_gateway_status
    ON refund_requests(gateway_type, status);
