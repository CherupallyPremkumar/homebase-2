-- V15__create_refund_requests.sql

CREATE TABLE refund_requests (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    reason VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    stripe_refund_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
