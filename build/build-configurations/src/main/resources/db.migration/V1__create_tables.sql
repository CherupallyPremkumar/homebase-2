-- V1: Create core tables for eCommerce application

-- Products
CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    price NUMERIC(10,2) NOT NULL,
    image_url VARCHAR(500),
    category VARCHAR(100),
    in_stock BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Customers
CREATE TABLE customers (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    stripe_customer_id VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Orders
CREATE TABLE orders (
    id VARCHAR(36) PRIMARY KEY,
    customer_id VARCHAR(36) NOT NULL,
    stripe_session_id VARCHAR(255) UNIQUE,
    stripe_payment_intent_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    tax_amount NUMERIC(10,2) NOT NULL,
    shipping_amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    idempotency_key VARCHAR(255) UNIQUE,
    metadata JSONB,
    webhook_processed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_user_created ON orders(customer_id, created_at);
CREATE INDEX idx_orders_stripe_session ON orders(stripe_session_id);
CREATE INDEX idx_orders_status ON orders(status);

-- Order Items
CREATE TABLE order_items (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id VARCHAR(36) NOT NULL,
    product_name VARCHAR(255),
    quantity INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    total_price NUMERIC(10,2) NOT NULL
);

CREATE INDEX idx_order_items_order ON order_items(order_id);

-- Inventory
CREATE TABLE inventory (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) UNIQUE NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    reserved INT NOT NULL DEFAULT 0,
    low_stock_threshold INT DEFAULT 10,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inventory_product ON inventory(product_id);

-- Stock Reservations
CREATE TABLE stock_reservations (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_reservation_items (
    reservation_id VARCHAR(36) NOT NULL REFERENCES stock_reservations(id) ON DELETE CASCADE,
    product_id VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (reservation_id, product_id)
);

-- Payment Transactions
CREATE TABLE payment_transactions (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL REFERENCES orders(id),
    stripe_charge_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    error_code VARCHAR(100),
    error_message TEXT,
    retry_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Webhook Events (for idempotent processing)
CREATE TABLE webhook_events (
    id VARCHAR(36) PRIMARY KEY,
    stripe_event_id VARCHAR(255) UNIQUE NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    processed_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_webhook_events_type_processed ON webhook_events(event_type, processed);

-- Idempotency Keys
CREATE TABLE idempotency_keys (
    key VARCHAR(255) PRIMARY KEY,
    operation_type VARCHAR(100),
    result JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

CREATE INDEX idx_idempotency_expires ON idempotency_keys(expires_at);
