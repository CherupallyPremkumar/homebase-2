-- =====================================================
-- Sample Data for Testing
-- =====================================================

-- Insert sample checkout
INSERT INTO checkouts (checkout_id, cart_id, user_id, state, idempotency_key)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', 
     '123e4567-e89b-12d3-a456-426614174000',
     '987fcdeb-51a3-12d3-a456-426614174000',
     'COMPLETED',
     'idem_key_123456789');

-- Insert checkout session
INSERT INTO checkout_sessions (checkout_id, locked_cart, locked_price)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000',
     '{"items": [{"productId": "prod_123", "quantity": 2}]}',
     '{"total": {"amount": 199.99, "currency": "USD"}}');

-- Insert audit log
INSERT INTO checkout_audit_log (checkout_id, action, from_state, to_state)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000',
     'STATE_CHANGED',
     'INITIALIZED',
     'CART_LOCKED');
