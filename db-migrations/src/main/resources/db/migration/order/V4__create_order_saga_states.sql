-- V4: Create table for tracking Order Saga states
CREATE TABLE order_saga_states (
    order_id VARCHAR(36) PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    current_step VARCHAR(255),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_saga_status ON order_saga_states(status);
