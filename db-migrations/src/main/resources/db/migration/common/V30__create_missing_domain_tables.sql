-- V30: Create missing domain tables and activity logs for Chenile STM compliance

-- 1. Rename customers to users to align with User.java entity
ALTER TABLE customers RENAME TO users;
ALTER TABLE orders RENAME COLUMN customer_id TO user_id;

-- 2. Create Cart tables
CREATE TABLE cart (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) REFERENCES users(id),
    total_amount NUMERIC(15,2),
    shipping_address JSONB,
    billing_address JSONB,
    applied_promo_code VARCHAR(255),
    discount_amount NUMERIC(15,2),
    description VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE cart_item (
    id VARCHAR(36) PRIMARY KEY,
    cart_id VARCHAR(36) NOT NULL REFERENCES cart(id) ON DELETE CASCADE,
    product_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    price_amount NUMERIC(15,2),
    price_currency VARCHAR(3),
    seller_id VARCHAR(36),
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 3. Create Supplier tables
CREATE TABLE supplier (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    email VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE supplierproduct (
    id VARCHAR(36) PRIMARY KEY,
    supplier_id VARCHAR(36) NOT NULL REFERENCES supplier(id),
    product_id VARCHAR(36) NOT NULL REFERENCES products(id),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 4. Create Activity Log tables for Chenile STM compliance
-- All activity log tables share a common structure: id, activity_name, activity_success, activity_comment, created_at, created_by

CREATE TABLE user_activity (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE product_activity (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE order_activity (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE inventory_activity (
    id VARCHAR(36) PRIMARY KEY,
    inventory_id VARCHAR(36) NOT NULL REFERENCES inventory(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE shipping_activity (
    id VARCHAR(36) PRIMARY KEY,
    shipping_id VARCHAR(36) NOT NULL REFERENCES shipments(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE payout_activity (
    id VARCHAR(36) PRIMARY KEY,
    payout_id VARCHAR(36) NOT NULL REFERENCES payouts(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE supplier_activity (
    id VARCHAR(36) PRIMARY KEY,
    supplier_id VARCHAR(36) NOT NULL REFERENCES supplier(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE supplierproduct_activity (
    id VARCHAR(36) PRIMARY KEY,
    supplierproduct_id VARCHAR(36) NOT NULL REFERENCES supplierproduct(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE cart_activity (
    id VARCHAR(36) PRIMARY KEY,
    cart_id VARCHAR(36) NOT NULL REFERENCES cart(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indices for performance
CREATE INDEX idx_user_activity_user ON user_activity(user_id);
CREATE INDEX idx_product_activity_product ON product_activity(product_id);
CREATE INDEX idx_order_activity_order ON order_activity(order_id);
CREATE INDEX idx_inventory_activity_inventory ON inventory_activity(inventory_id);
CREATE INDEX idx_cart_activity_cart ON cart_activity(cart_id);
