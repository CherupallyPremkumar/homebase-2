-- =====================================================
-- Checkout Service Database Schema
-- PostgreSQL 14+
-- =====================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- Table: checkouts
-- Purpose: Main checkout aggregate root
-- =====================================================
CREATE TABLE checkouts (
    checkout_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cart_id UUID NOT NULL,
    user_id UUID NOT NULL,
    order_id UUID,
    
    -- State machine
    state VARCHAR(50) NOT NULL,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Idempotency
    idempotency_key VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_checkout_cart_id ON checkouts(cart_id);
CREATE INDEX idx_checkout_user_id ON checkouts(user_id);
CREATE INDEX idx_checkout_order_id ON checkouts(order_id);
CREATE INDEX idx_checkout_state ON checkouts(state);
CREATE INDEX idx_checkout_created_at ON checkouts(created_at);
CREATE INDEX idx_checkout_idempotency_key ON checkouts(idempotency_key);

-- =====================================================
-- Table: checkout_sessions
-- Purpose: Detailed checkout session data
-- =====================================================
CREATE TABLE checkout_sessions (
    session_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    
    -- Snapshots (JSONB for flexibility)
    locked_cart JSONB,
    locked_price JSONB,
    order_details JSONB,
    payment_details JSONB,
    shipping_details JSONB,
    
    -- Metadata
    metadata JSONB,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_session_checkout_id ON checkout_sessions(checkout_id);

-- =====================================================
-- Table: checkout_audit_log
-- Purpose: Audit trail for all checkout state changes
-- =====================================================
CREATE TABLE checkout_audit_log (
    audit_id BIGSERIAL PRIMARY KEY,
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    
    -- Audit fields
    action VARCHAR(100) NOT NULL,
    from_state VARCHAR(50),
    to_state VARCHAR(50),
    
    -- Context
    details JSONB,
    user_id UUID,
    ip_address VARCHAR(45),
    user_agent TEXT,
    
    -- Timestamp
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_checkout_id ON checkout_audit_log(checkout_id);
CREATE INDEX idx_audit_created_at ON checkout_audit_log(created_at);
CREATE INDEX idx_audit_action ON checkout_audit_log(action);

-- =====================================================
-- Table: saga_execution_log
-- Purpose: Track saga step execution and compensation
-- =====================================================
CREATE TABLE saga_execution_log (
    log_id BIGSERIAL PRIMARY KEY,
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    
    -- Step information
    step_name VARCHAR(100) NOT NULL,
    step_order INTEGER,
    status VARCHAR(50) NOT NULL, -- STARTED, COMPLETED, FAILED, COMPENSATED, COMPENSATION_FAILED
    
    -- Execution details
    input_data JSONB,
    output_data JSONB,
    error_message TEXT,
    stack_trace TEXT,
    
    -- Retry information
    attempt_number INTEGER DEFAULT 1,
    max_retries INTEGER,
    
    -- Timestamps
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    duration_ms BIGINT
);

CREATE INDEX idx_saga_log_checkout_id ON saga_execution_log(checkout_id);
CREATE INDEX idx_saga_log_status ON saga_execution_log(status);
CREATE INDEX idx_saga_log_started_at ON saga_execution_log(started_at);

-- =====================================================
-- Table: price_locks
-- Purpose: Track price lock tokens and expiration
-- =====================================================
CREATE TABLE price_locks (
    lock_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    
    -- Lock details
    lock_token VARCHAR(255) NOT NULL UNIQUE,
    price_hash VARCHAR(255) NOT NULL,
    
    -- Locked price breakdown (JSONB)
    price_snapshot JSONB NOT NULL,
    
    -- Currency
    currency VARCHAR(3) NOT NULL,
    total_amount DECIMAL(19, 4) NOT NULL,
    
    -- Lock lifecycle
    locked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    released_at TIMESTAMP,
    
    -- Status
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_price_lock_checkout_id ON price_locks(checkout_id);
CREATE INDEX idx_price_lock_token ON price_locks(lock_token);
CREATE INDEX idx_price_lock_expires_at ON price_locks(expires_at);
CREATE INDEX idx_price_lock_active ON price_locks(is_active);

-- =====================================================
-- Table: cart_locks
-- Purpose: Track cart lock status
-- =====================================================
CREATE TABLE cart_locks (
    lock_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    cart_id UUID NOT NULL,
    
    -- Cart snapshot
    cart_snapshot JSONB NOT NULL,
    
    -- Lock lifecycle
    locked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    unlocked_at TIMESTAMP,
    
    -- Status
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_cart_lock_checkout_id ON cart_locks(checkout_id);
CREATE INDEX idx_cart_lock_cart_id ON cart_locks(cart_id);
CREATE INDEX idx_cart_lock_expires_at ON cart_locks(expires_at);
CREATE INDEX idx_cart_lock_active ON cart_locks(is_active);

-- =====================================================
-- Table: inventory_reservations
-- Purpose: Track inventory reservations during checkout
-- =====================================================
CREATE TABLE inventory_reservations (
    reservation_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    
    -- Product information
    product_id UUID NOT NULL,
    sku VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    
    -- Reservation lifecycle
    reserved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    released_at TIMESTAMP,
    committed_at TIMESTAMP,
    
    -- Status
    status VARCHAR(50) NOT NULL -- RESERVED, COMMITTED, RELEASED, EXPIRED
);

CREATE INDEX idx_inventory_checkout_id ON inventory_reservations(checkout_id);
CREATE INDEX idx_inventory_product_id ON inventory_reservations(product_id);
CREATE INDEX idx_inventory_sku ON inventory_reservations(sku);
CREATE INDEX idx_inventory_expires_at ON inventory_reservations(expires_at);
CREATE INDEX idx_inventory_status ON inventory_reservations(status);

-- =====================================================
-- Table: payment_intents
-- Purpose: Track payment intent creation and status
-- =====================================================
CREATE TABLE payment_intents (
    intent_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    checkout_id UUID NOT NULL REFERENCES checkouts(checkout_id) ON DELETE CASCADE,
    
    -- Payment provider details
    provider VARCHAR(50) NOT NULL, -- STRIPE, PAYPAL, etc.
    provider_intent_id VARCHAR(255) NOT NULL UNIQUE,
    client_secret VARCHAR(500),
    
    -- Amount
    currency VARCHAR(3) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    
    -- Status
    status VARCHAR(50) NOT NULL, -- CREATED, PENDING, SUCCEEDED, FAILED, CANCELLED
    
    -- URLs
    payment_url TEXT,
    success_url TEXT,
    cancel_url TEXT,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    succeeded_at TIMESTAMP,
    failed_at TIMESTAMP,
    
    -- Metadata
    metadata JSONB
);

CREATE INDEX idx_payment_checkout_id ON payment_intents(checkout_id);
CREATE INDEX idx_payment_provider_intent_id ON payment_intents(provider_intent_id);
CREATE INDEX idx_payment_status ON payment_intents(status);
CREATE INDEX idx_payment_created_at ON payment_intents(created_at);

-- =====================================================
-- Table: webhook_events
-- Purpose: Store incoming webhook events for idempotency
-- =====================================================
CREATE TABLE webhook_events (
    event_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- Provider information
    provider VARCHAR(50) NOT NULL,
    provider_event_id VARCHAR(255) NOT NULL UNIQUE,
    event_type VARCHAR(100) NOT NULL,
    
    -- Related entities
    checkout_id UUID REFERENCES checkouts(checkout_id),
    payment_intent_id VARCHAR(255),
    
    -- Event payload
    payload JSONB NOT NULL,
    
    -- Processing
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMP,
    error_message TEXT,
    
    -- Timestamps
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_webhook_provider_event_id ON webhook_events(provider_event_id);
CREATE INDEX idx_webhook_checkout_id ON webhook_events(checkout_id);
CREATE INDEX idx_webhook_processed ON webhook_events(processed);
CREATE INDEX idx_webhook_received_at ON webhook_events(received_at);

-- =====================================================
-- Trigger: Update updated_at timestamp
-- =====================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_checkouts_updated_at
    BEFORE UPDATE ON checkouts
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_checkout_sessions_updated_at
    BEFORE UPDATE ON checkout_sessions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payment_intents_updated_at
    BEFORE UPDATE ON payment_intents
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- Comments
-- =====================================================
COMMENT ON TABLE checkouts IS 'Main checkout aggregate root tracking checkout lifecycle';
COMMENT ON TABLE checkout_sessions IS 'Detailed checkout session data with snapshots';
COMMENT ON TABLE checkout_audit_log IS 'Audit trail for all checkout state changes';
COMMENT ON TABLE saga_execution_log IS 'Saga pattern execution and compensation tracking';
COMMENT ON TABLE price_locks IS 'Price lock tokens with expiration for checkout';
COMMENT ON TABLE cart_locks IS 'Cart lock status during checkout';
COMMENT ON TABLE inventory_reservations IS 'Inventory reservations during checkout saga';
COMMENT ON TABLE payment_intents IS 'Payment provider intent tracking';
COMMENT ON TABLE webhook_events IS 'Incoming webhook events for idempotent processing';
