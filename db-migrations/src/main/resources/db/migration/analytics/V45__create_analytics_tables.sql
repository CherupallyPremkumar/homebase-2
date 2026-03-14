-- V45: Analytics aggregation tables

CREATE TABLE IF NOT EXISTS daily_sales_summary (
    id                  VARCHAR(36) PRIMARY KEY,
    summary_date        DATE NOT NULL UNIQUE,
    total_orders        INT DEFAULT 0,
    total_revenue       NUMERIC(15,2) DEFAULT 0.00,
    total_units_sold    INT DEFAULT 0,
    total_discount      NUMERIC(15,2) DEFAULT 0.00,
    total_tax           NUMERIC(15,2) DEFAULT 0.00,
    total_shipping      NUMERIC(15,2) DEFAULT 0.00,
    avg_order_value     NUMERIC(15,2) DEFAULT 0.00,
    new_customers       INT DEFAULT 0,
    returning_customers INT DEFAULT 0,
    currency            VARCHAR(3) DEFAULT 'INR',
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS product_performance (
    id                  VARCHAR(36) PRIMARY KEY,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id),
    period_date         DATE NOT NULL,
    views               INT DEFAULT 0,
    add_to_cart_count   INT DEFAULT 0,
    purchases           INT DEFAULT 0,
    units_sold          INT DEFAULT 0,
    revenue             NUMERIC(15,2) DEFAULT 0.00,
    returns_count       INT DEFAULT 0,
    avg_rating          NUMERIC(3,2),
    UNIQUE (product_id, period_date),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS supplier_performance (
    id                  VARCHAR(36) PRIMARY KEY,
    supplier_id         VARCHAR(36) NOT NULL REFERENCES supplier(id),
    period_month        INT NOT NULL,
    period_year         INT NOT NULL,
    total_orders        INT DEFAULT 0,
    total_revenue       NUMERIC(15,2) DEFAULT 0.00,
    total_returns       INT DEFAULT 0,
    avg_fulfillment_days NUMERIC(5,2),
    avg_rating          NUMERIC(3,2),
    cancellation_rate   NUMERIC(5,4),
    UNIQUE (supplier_id, period_month, period_year),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_daily_sales_date ON daily_sales_summary(summary_date);
CREATE INDEX IF NOT EXISTS idx_product_perf_product ON product_performance(product_id, period_date);
CREATE INDEX IF NOT EXISTS idx_supplier_perf_supplier ON supplier_performance(supplier_id, period_year, period_month);
