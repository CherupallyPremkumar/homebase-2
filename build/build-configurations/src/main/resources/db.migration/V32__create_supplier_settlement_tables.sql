-- V32: Create Supplier Settlement tables for 100-saree-maker marketplace

CREATE TABLE settlements (
    id VARCHAR(36) PRIMARY KEY,
    supplier_id VARCHAR(36) NOT NULL REFERENCES supplier(id),
    period_month INT NOT NULL,
    period_year INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_sales_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    commission_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    net_payout_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(100),
    tenant VARCHAR(50),
    created_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    test_entity BOOLEAN DEFAULT FALSE,

    -- AbstractJpaStateEntity fields
    state_entry_time TIMESTAMP,
    sla_yellow_date TIMESTAMP,
    sla_red_date TIMESTAMP,
    sla_tending_late INT DEFAULT 0,
    sla_late INT DEFAULT 0,
    "flowId" VARCHAR(100),
    "stateId" VARCHAR(100)
);

CREATE INDEX idx_settlements_supplier ON settlements(supplier_id);
CREATE INDEX idx_settlements_period ON settlements(period_month, period_year);

CREATE TABLE settlement_lines (
    id VARCHAR(36) PRIMARY KEY,
    settlement_id VARCHAR(36) NOT NULL REFERENCES settlements(id) ON DELETE CASCADE,
    order_id VARCHAR(36) NOT NULL REFERENCES orders(id),
    order_item_id VARCHAR(36) NOT NULL REFERENCES order_items(id),
    
    item_sales_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    item_commission_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',

    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(100),
    tenant VARCHAR(50),
    created_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    test_entity BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_settlement_lines_order ON settlement_lines(order_id);

CREATE TABLE settlement_activity (
    id VARCHAR(36) PRIMARY KEY,
    settlement_id VARCHAR(36) NOT NULL REFERENCES settlements(id) ON DELETE CASCADE,
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN NOT NULL,
    activity_comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(100),
    tenant VARCHAR(50),
    created_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    test_entity BOOLEAN DEFAULT FALSE
);
