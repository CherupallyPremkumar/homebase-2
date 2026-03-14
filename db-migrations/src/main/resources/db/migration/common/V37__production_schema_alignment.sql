-- V37: Production Schema Alignment
-- Adds columns required by Java entity models that have no backing DB columns.
-- Also creates new supporting tables for production features.

-- ─────────────────────────────────────────────────────────────────────────────
-- 1. orders table — add columns needed by Order.java
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE orders ADD COLUMN IF NOT EXISTS gateway_transaction_id VARCHAR(255);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS webhook_processed_at TIMESTAMP;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS shipping_address JSONB;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS billing_address JSONB;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS applied_promo_code VARCHAR(255);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS discount_amount NUMERIC(10,2);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS cart_id VARCHAR(36);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS retry_count INT DEFAULT 0;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS previous_failed_order_id VARCHAR(36);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS description VARCHAR(2000);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS delivery_date TIMESTAMP;

-- ─────────────────────────────────────────────────────────────────────────────
-- 2. order_items table — add status and settlement tracking
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE order_items ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'ACTIVE';
ALTER TABLE order_items ADD COLUMN IF NOT EXISTS settlement_id VARCHAR(36);
ALTER TABLE order_items ADD COLUMN IF NOT EXISTS settlement_status VARCHAR(50) DEFAULT 'PENDING';
ALTER TABLE order_items ADD COLUMN IF NOT EXISTS currency VARCHAR(10) DEFAULT 'INR';

-- ─────────────────────────────────────────────────────────────────────────────
-- 3. products table — add catalog-related fields
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE products ADD COLUMN IF NOT EXISTS sku VARCHAR(100);
ALTER TABLE products ADD COLUMN IF NOT EXISTS weight NUMERIC(10,3);
ALTER TABLE products ADD COLUMN IF NOT EXISTS dimensions JSONB;
ALTER TABLE products ADD COLUMN IF NOT EXISTS brand VARCHAR(255);
ALTER TABLE products ADD COLUMN IF NOT EXISTS category_id VARCHAR(36);

CREATE UNIQUE INDEX IF NOT EXISTS idx_products_sku ON products(sku) WHERE sku IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_brand ON products(brand);

-- ─────────────────────────────────────────────────────────────────────────────
-- 4. users table — add profile fields
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS display_name VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS keycloak_id VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(500);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_keycloak ON users(keycloak_id) WHERE keycloak_id IS NOT NULL;

-- ─────────────────────────────────────────────────────────────────────────────
-- 5. supplier table — add business fields
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS user_id VARCHAR(36);
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS upi_id VARCHAR(100);
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS address VARCHAR(2000);
ALTER TABLE supplier ADD COLUMN IF NOT EXISTS commission_percentage NUMERIC(5,2);

-- ─────────────────────────────────────────────────────────────────────────────
-- 6. returnrequest table — add refund tracking
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE returnrequest ADD COLUMN IF NOT EXISTS quantity INT DEFAULT 1;
ALTER TABLE returnrequest ADD COLUMN IF NOT EXISTS refund_amount NUMERIC(10,2);
ALTER TABLE returnrequest ADD COLUMN IF NOT EXISTS return_type VARCHAR(50);

-- ─────────────────────────────────────────────────────────────────────────────
-- 7. inventory_reservation table — add session tracking and expiry
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE inventory_reservation ADD COLUMN IF NOT EXISTS session_id VARCHAR(255);
ALTER TABLE inventory_reservation ADD COLUMN IF NOT EXISTS reserved_at TIMESTAMP;
ALTER TABLE inventory_reservation ADD COLUMN IF NOT EXISTS expires_at TIMESTAMP;
ALTER TABLE inventory_reservation ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'ACTIVE';

CREATE INDEX IF NOT EXISTS idx_inventory_reservation_status ON inventory_reservation(status);
CREATE INDEX IF NOT EXISTS idx_inventory_reservation_expires ON inventory_reservation(expires_at) WHERE status = 'ACTIVE';

-- ─────────────────────────────────────────────────────────────────────────────
-- 8. shipments table — add delivery tracking
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE shipments ADD COLUMN IF NOT EXISTS shipping_address JSONB;
ALTER TABLE shipments ADD COLUMN IF NOT EXISTS delivered_at TIMESTAMP;

-- ─────────────────────────────────────────────────────────────────────────────
-- 9. New table: addresses (user shipping/billing addresses)
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS addresses (
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    label               VARCHAR(50),
    line1               VARCHAR(255) NOT NULL,
    line2               VARCHAR(255),
    city                VARCHAR(100) NOT NULL,
    state               VARCHAR(100),
    postal_code         VARCHAR(20) NOT NULL,
    country             VARCHAR(50) NOT NULL DEFAULT 'IN',
    phone               VARCHAR(20),
    is_default          BOOLEAN DEFAULT FALSE,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_addresses_user ON addresses(user_id);

-- ─────────────────────────────────────────────────────────────────────────────
-- 10. New table: product_images
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_images (
    id                  VARCHAR(36) PRIMARY KEY,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    url                 VARCHAR(1000) NOT NULL,
    alt_text            VARCHAR(255),
    media_type          VARCHAR(50),
    display_order       INT DEFAULT 0,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_product_images_product ON product_images(product_id);

-- ─────────────────────────────────────────────────────────────────────────────
-- 11. New table: promo_code_usage (track per-user promo code usage)
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS promo_code_usage (
    id                  VARCHAR(36) PRIMARY KEY,
    promo_code_id       VARCHAR(36) NOT NULL REFERENCES promo_code(id),
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id),
    order_id            VARCHAR(36),
    used_at             TIMESTAMP NOT NULL DEFAULT NOW(),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_promo_usage_user ON promo_code_usage(user_id);
CREATE INDEX IF NOT EXISTS idx_promo_usage_promo ON promo_code_usage(promo_code_id);

-- ─────────────────────────────────────────────────────────────────────────────
-- 12. New table: wishlist
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS wishlist (
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    added_at            TIMESTAMP NOT NULL DEFAULT NOW(),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE,
    UNIQUE (user_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_wishlist_user ON wishlist(user_id);

-- ─────────────────────────────────────────────────────────────────────────────
-- 13. Drop legacy stock_reservations (replaced by inventory_reservation)
-- ─────────────────────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS stock_reservation_items;
DROP TABLE IF EXISTS stock_reservations;

-- ─────────────────────────────────────────────────────────────────────────────
-- 14. Set default tenant for existing rows without a tenant value
-- ─────────────────────────────────────────────────────────────────────────────
UPDATE orders SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE order_items SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE products SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE users SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE inventory SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE supplier SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE cart SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE shipments SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE returnrequest SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE promo_code SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE settlements SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
UPDATE inventory_reservation SET tenant = 'homebase' WHERE tenant IS NULL OR tenant = '';
