DROP TABLE IF EXISTS promo_code_activity_log;
DROP TABLE IF EXISTS coupon_usage_log;
DROP TABLE IF EXISTS coupon_compatibility;
DROP TABLE IF EXISTS coupon;

CREATE TABLE coupon (
    id VARCHAR(255) PRIMARY KEY,
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(100),
    tenant VARCHAR(50),
    created_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    state_entry_time TIMESTAMP,
    sla_yellow_date TIMESTAMP,
    sla_red_date TIMESTAMP,
    sla_tending_late INT DEFAULT 0,
    sla_late INT DEFAULT 0,
    flow_id VARCHAR(100),
    state_id VARCHAR(100),
    code VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    discount_type VARCHAR(50) NOT NULL,
    discount_value DOUBLE NOT NULL,
    min_order_value DOUBLE,
    max_discount_amount DOUBLE,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    usage_limit INT DEFAULT 0,
    usage_count INT DEFAULT 0,
    usage_per_customer INT DEFAULT 1,
    applicable_categories TEXT,
    applicable_products TEXT,
    stacking_allowed BOOLEAN DEFAULT FALSE,
    auto_apply BOOLEAN DEFAULT FALSE,
    target_audience VARCHAR(50) DEFAULT 'ALL',
    target_user_ids TEXT,
    minimum_items INT DEFAULT 0
);

INSERT INTO coupon (id, code, name, description, discount_type, discount_value, min_order_value, max_discount_amount, start_date, end_date, usage_limit, usage_count, usage_per_customer, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('coupon-1', 'FLAT200', 'Flat Rs.200 Off', 'Flat Rs.200 off on orders above Rs.999', 'FLAT', 200, 999, 200, '2026-03-01 00:00:00', '2026-06-30 23:59:59', 10000, 2345, 2, 'ACTIVE', 'promo-flow', '2026-02-25 10:00:00', '2026-03-01 00:00:00', 'homebase', 2);

INSERT INTO coupon (id, code, name, description, discount_type, discount_value, min_order_value, max_discount_amount, start_date, end_date, usage_limit, usage_count, usage_per_customer, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('coupon-2', 'ELEC15', '15% Off Electronics', '15% off on all electronics', 'PERCENTAGE', 15, 1499, 500, '2026-03-01 00:00:00', '2026-04-30 23:59:59', 5000, 890, 1, 'ACTIVE', 'promo-flow', '2026-02-28 10:00:00', '2026-03-01 00:00:00', 'homebase', 1);

INSERT INTO coupon (id, code, name, description, discount_type, discount_value, min_order_value, max_discount_amount, start_date, end_date, usage_limit, usage_count, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('coupon-3', 'DIWALI500', 'Diwali Special Rs.500 Off', 'Diwali festival special discount', 'FLAT', 500, 2999, 500, '2025-10-15 00:00:00', '2025-11-15 23:59:59', 20000, 18456, 'EXPIRED', 'promo-flow', '2025-10-01 10:00:00', '2025-11-16 00:00:00', 'homebase', 4);

INSERT INTO coupon (id, code, name, description, discount_type, discount_value, min_order_value, usage_limit, usage_count, usage_per_customer, start_date, end_date, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('coupon-4', 'FIRST100', 'First 100 Customers', 'Rs.300 off for first 100 customers', 'FLAT', 300, 0, 100, 100, 1, '2026-03-10 00:00:00', '2026-03-31 23:59:59', 'DEPLETED', 'promo-flow', '2026-03-08 10:00:00', '2026-03-12 15:00:00', 'homebase', 3);

INSERT INTO coupon (id, code, name, discount_type, discount_value, min_order_value, max_discount_amount, start_date, end_date, usage_limit, usage_count, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('coupon-5', 'SUMMER30', 'Summer Sale 30% Off', 'PERCENTAGE', 30, 1999, 1000, '2026-04-01 00:00:00', '2026-05-31 23:59:59', 50000, 0, 'DRAFT', 'promo-flow', '2026-03-15 10:00:00', '2026-03-15 10:00:00', 'homebase', 0);

INSERT INTO coupon (id, code, name, discount_type, discount_value, usage_limit, usage_count, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('coupon-6', 'REFER50', 'Referral Rs.50 Off', 'FLAT', 50, 0, 4567, 'SUSPENDED', 'promo-flow', '2025-12-01 10:00:00', '2026-02-20 10:00:00', 'homebase', 5);
