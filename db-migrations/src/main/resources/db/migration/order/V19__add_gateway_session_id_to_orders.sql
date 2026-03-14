-- V19: Gateway Abstraction - Rename Stripe-specific columns to gateway-agnostic names
-- This migration aligns the database schema with the refactored gateway-agnostic domain entities.

-- =====================================================
-- 1. ORDERS TABLE: Rename Stripe columns to Gateway columns
-- =====================================================
ALTER TABLE orders RENAME COLUMN stripe_session_id TO gateway_session_id;
ALTER TABLE orders RENAME COLUMN stripe_payment_intent_id TO gateway_transaction_id;

-- Recreate index with new name
DROP INDEX IF EXISTS idx_orders_stripe_session;
CREATE INDEX idx_orders_gateway_session ON orders(gateway_session_id);

-- =====================================================
-- 2. PAYMENT_TRANSACTIONS TABLE: Add missing gateway columns
-- =====================================================
-- Rename stripe_charge_id to gateway_charge_id
ALTER TABLE payment_transactions RENAME COLUMN stripe_charge_id TO gateway_charge_id;

-- Add new gateway-agnostic columns
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS gateway_transaction_id VARCHAR(255);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS currency VARCHAR(3) DEFAULT 'INR';
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS stripe_fee NUMERIC(10,2);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS net_amount NUMERIC(10,2);
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS payment_method_type VARCHAR(50);

-- =====================================================
-- 3. WEBHOOK_EVENTS TABLE: Rename stripe_event_id to gateway_event_id
-- =====================================================
ALTER TABLE webhook_events RENAME COLUMN stripe_event_id TO gateway_event_id;
