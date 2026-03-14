-- V35: Add created_at to orders table for cancellation window policy enforcement
-- Required by OrderPolicyValidator.validateCancellationWindow()
-- Seeds from created_time (BaseJpaEntity field added in V31) so existing rows are valid.

ALTER TABLE orders ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;

-- Seed existing rows from the BaseJpaEntity auditing field
UPDATE orders SET created_at = created_time WHERE created_at IS NULL AND created_time IS NOT NULL;

-- For any rows where created_time is also null (data predating V31), set to a safe default
UPDATE orders SET created_at = NOW() WHERE created_at IS NULL;

-- Index to support efficient range queries for SLA/window checks
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
