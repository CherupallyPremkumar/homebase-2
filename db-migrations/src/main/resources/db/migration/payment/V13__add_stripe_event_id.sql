ALTER TABLE payment_transactions ADD COLUMN stripe_event_id VARCHAR(255);
ALTER TABLE payment_transactions ADD CONSTRAINT uk_stripe_event_id UNIQUE (stripe_event_id);
