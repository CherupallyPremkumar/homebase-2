-- =====================================================
-- Additional Indexes for Performance
-- =====================================================

-- Composite indexes for common queries
CREATE INDEX idx_checkouts_user_state ON checkouts(user_id, state);
CREATE INDEX idx_checkouts_cart_state ON checkouts(cart_id, state);
CREATE INDEX idx_saga_log_checkout_step ON saga_execution_log(checkout_id, step_name);
CREATE INDEX idx_price_locks_active_expires ON price_locks(is_active, expires_at) WHERE is_active = TRUE;
CREATE INDEX idx_cart_locks_active_expires ON cart_locks(is_active, expires_at) WHERE is_active = TRUE;

-- GIN indexes for JSONB columns (for faster JSON queries)
CREATE INDEX idx_checkout_sessions_metadata ON checkout_sessions USING GIN (metadata);
CREATE INDEX idx_audit_log_details ON checkout_audit_log USING GIN (details);
CREATE INDEX idx_saga_log_output ON saga_execution_log USING GIN (output_data);
CREATE INDEX idx_webhook_payload ON webhook_events USING GIN (payload);

-- =====================================================
-- Materialized Views for Analytics
-- =====================================================

-- View: Checkout success rate by day
CREATE MATERIALIZED VIEW mv_checkout_success_rate AS
SELECT 
    DATE(created_at) as checkout_date,
    COUNT(*) as total_checkouts,
    SUM(CASE WHEN state = 'COMPLETED' THEN 1 ELSE 0 END) as successful_checkouts,
    SUM(CASE WHEN state IN ('ABANDONED', 'COMPENSATION_COMPLETED') THEN 1 ELSE 0 END) as failed_checkouts,
    ROUND(100.0 * SUM(CASE WHEN state = 'COMPLETED' THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 2) as success_rate_pct
FROM checkouts
WHERE created_at >= CURRENT_DATE - INTERVAL '90 days'
GROUP BY DATE(created_at);

CREATE UNIQUE INDEX idx_mv_checkout_success_rate ON mv_checkout_success_rate(checkout_date);

-- View: Saga step failure analysis
CREATE MATERIALIZED VIEW mv_saga_step_failures AS
SELECT 
    step_name,
    COUNT(*) as total_executions,
    SUM(CASE WHEN status = 'FAILED' THEN 1 ELSE 0 END) as failures,
    SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) as successes,
    ROUND(100.0 * SUM(CASE WHEN status = 'FAILED' THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 2) as failure_rate_pct,
    AVG(duration_ms) as avg_duration_ms,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY duration_ms) as p95_duration_ms
FROM saga_execution_log
WHERE started_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY step_name;

CREATE UNIQUE INDEX idx_mv_saga_step_failures ON mv_saga_step_failures(step_name);

-- =====================================================
-- Functions for Business Logic
-- =====================================================

-- Function: Check if price lock is valid
CREATE OR REPLACE FUNCTION is_price_lock_valid(p_lock_token VARCHAR)
RETURNS BOOLEAN AS $$
DECLARE
    lock_exists BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 
        FROM price_locks 
        WHERE lock_token = p_lock_token 
        AND is_active = TRUE 
        AND expires_at > CURRENT_TIMESTAMP
    ) INTO lock_exists;
    
    RETURN lock_exists;
END;
$$ LANGUAGE plpgsql;

-- Function: Cleanup expired locks (background job)
CREATE OR REPLACE FUNCTION cleanup_expired_locks()
RETURNS INTEGER AS $$
DECLARE
    rows_updated INTEGER;
BEGIN
    -- Deactivate expired price locks
    UPDATE price_locks
    SET is_active = FALSE
    WHERE is_active = TRUE 
    AND expires_at <= CURRENT_TIMESTAMP;
    
    GET DIAGNOSTICS rows_updated = ROW_COUNT;
    
    -- Deactivate expired cart locks
    UPDATE cart_locks
    SET is_active = FALSE
    WHERE is_active = TRUE 
    AND expires_at <= CURRENT_TIMESTAMP;
    
    GET DIAGNOSTICS rows_updated = rows_updated + ROW_COUNT;
    
    -- Release expired inventory reservations
    UPDATE inventory_reservations
    SET status = 'EXPIRED',
        released_at = CURRENT_TIMESTAMP
    WHERE status = 'RESERVED'
    AND expires_at <= CURRENT_TIMESTAMP;
    
    GET DIAGNOSTICS rows_updated = rows_updated + ROW_COUNT;
    
    RETURN rows_updated;
END;
$$ LANGUAGE plpgsql;
