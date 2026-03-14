-- V40: Tax rates, category mappings, and order tax lines

CREATE TABLE IF NOT EXISTS tax_rates (
    id                  VARCHAR(36) PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    region_code         VARCHAR(50) NOT NULL,          -- IN-TN, IN-KA, US-CA etc.
    tax_type            VARCHAR(50) NOT NULL,           -- GST, CGST, SGST, IGST, VAT, SALES_TAX
    rate                NUMERIC(6,4) NOT NULL,          -- e.g. 0.1800 = 18%
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    active              BOOLEAN DEFAULT TRUE,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS tax_category_mapping (
    id                  VARCHAR(36) PRIMARY KEY,
    category_id         VARCHAR(36) REFERENCES categories(id),
    hsn_code            VARCHAR(20),                    -- Harmonized System Nomenclature
    tax_rate_id         VARCHAR(36) NOT NULL REFERENCES tax_rates(id),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS order_tax_lines (
    id                  VARCHAR(36) PRIMARY KEY,
    order_id            VARCHAR(36) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    order_item_id       VARCHAR(36) REFERENCES order_items(id),
    tax_rate_id         VARCHAR(36) NOT NULL REFERENCES tax_rates(id),
    tax_type            VARCHAR(50) NOT NULL,
    taxable_amount      NUMERIC(15,2) NOT NULL,
    tax_amount          NUMERIC(15,2) NOT NULL,
    rate_applied        NUMERIC(6,4) NOT NULL,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_tax_rates_region ON tax_rates(region_code, active);
CREATE INDEX IF NOT EXISTS idx_tax_category_hsn ON tax_category_mapping(hsn_code);
CREATE INDEX IF NOT EXISTS idx_order_tax_lines_order ON order_tax_lines(order_id);
