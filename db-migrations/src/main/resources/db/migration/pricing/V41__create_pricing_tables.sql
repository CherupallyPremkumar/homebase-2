-- V41: Pricing rules and price history

CREATE TABLE IF NOT EXISTS pricing_rules (
    id                  VARCHAR(36) PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    rule_type           VARCHAR(50) NOT NULL,           -- VOLUME_DISCOUNT, TIERED, TIME_BASED, BUNDLE
    priority            INT DEFAULT 0,
    conditions          JSONB,                          -- {"min_qty": 5, "category_ids": [...]}
    discount_type       VARCHAR(50),                    -- PERCENTAGE, FLAT, FIXED_PRICE
    discount_value      NUMERIC(10,2),
    active_from         TIMESTAMP,
    active_to           TIMESTAMP,
    active              BOOLEAN DEFAULT TRUE,
    applicable_products JSONB,                          -- product IDs or null for all
    applicable_categories JSONB,                        -- category IDs
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS price_history (
    id                  VARCHAR(36) PRIMARY KEY,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id),
    old_price           NUMERIC(15,2),
    new_price           NUMERIC(15,2) NOT NULL,
    change_reason       VARCHAR(255),
    changed_by          VARCHAR(100),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_pricing_rules_type ON pricing_rules(rule_type, active);
CREATE INDEX IF NOT EXISTS idx_pricing_rules_dates ON pricing_rules(active_from, active_to) WHERE active = TRUE;
CREATE INDEX IF NOT EXISTS idx_price_history_product ON price_history(product_id);
