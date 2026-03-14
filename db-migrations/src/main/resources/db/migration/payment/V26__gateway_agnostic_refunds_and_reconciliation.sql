-- V26: Make legacy refunds + reconciliation mismatches gateway-agnostic (rename Stripe-specific columns)

-- -----------------
-- refunds
-- -----------------
ALTER TABLE refunds
    ADD COLUMN IF NOT EXISTS gateway_type VARCHAR(20) NOT NULL DEFAULT 'stripe';

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'refunds'
          AND column_name = 'stripe_refund_id'
    ) THEN
        ALTER TABLE refunds RENAME COLUMN stripe_refund_id TO gateway_refund_id;
    END IF;
END $$;

DROP INDEX IF EXISTS idx_refunds_stripe_refund_id;
CREATE INDEX IF NOT EXISTS idx_refunds_gateway_refund_id ON refunds(gateway_refund_id);

-- -----------------
-- reconciliation_mismatches
-- -----------------
ALTER TABLE reconciliation_mismatches
    ADD COLUMN IF NOT EXISTS gateway_type VARCHAR(20) NOT NULL DEFAULT 'stripe';

ALTER TABLE reconciliation_mismatches
    ADD COLUMN IF NOT EXISTS resolved_by VARCHAR(255);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'reconciliation_mismatches'
          AND column_name = 'stripe_charge_id'
    ) THEN
        ALTER TABLE reconciliation_mismatches RENAME COLUMN stripe_charge_id TO provider_transaction_id;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'reconciliation_mismatches'
          AND column_name = 'stripe_amount'
    ) THEN
        ALTER TABLE reconciliation_mismatches RENAME COLUMN stripe_amount TO provider_amount;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'reconciliation_mismatches'
          AND column_name = 'db_amount'
    ) THEN
        ALTER TABLE reconciliation_mismatches RENAME COLUMN db_amount TO internal_amount;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'reconciliation_mismatches'
          AND column_name = 'stripe_status'
    ) THEN
        ALTER TABLE reconciliation_mismatches RENAME COLUMN stripe_status TO provider_status;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'reconciliation_mismatches'
          AND column_name = 'db_status'
    ) THEN
        ALTER TABLE reconciliation_mismatches RENAME COLUMN db_status TO internal_status;
    END IF;
END $$;

DROP INDEX IF EXISTS idx_recon_mismatch_stripe_charge;
CREATE INDEX IF NOT EXISTS idx_recon_mismatch_provider_txn ON reconciliation_mismatches(provider_transaction_id);
