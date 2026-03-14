-- V36: Create missing domain tables not yet represented in Flyway schema
-- Tables: returnrequest, promo_code, catalog (items, categories, collections, mappings),
--         inventory_reservation, and associated activity logs.
-- All tables include full BaseJpaEntity columns.
-- State-machine aggregate roots also include AbstractJpaStateEntity columns.

-- ─────────────────────────────────────────────────────────────────────────────
-- A. BaseJpaEntity column fragment (included inline for clarity)
--    id, created_time, last_modified_time, last_modified_by, tenant,
--    created_by, version, test_entity
-- B. AbstractJpaStateEntity fragment (for STM aggregate roots)
--    + state_entry_time, sla_yellow_date, sla_red_date,
--      sla_tending_late, sla_late, "flowId", "stateId"
-- ─────────────────────────────────────────────────────────────────────────────

-- 1. Return Request (STM Aggregate Root)
CREATE TABLE IF NOT EXISTS returnrequest (
    id                  VARCHAR(36) PRIMARY KEY,
    -- domain fields
    order_id            VARCHAR(36) REFERENCES orders(id),
    order_item_id       VARCHAR(36) REFERENCES order_items(id),
    reason              VARCHAR(1000),
    description         VARCHAR(2000),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE,
    -- AbstractJpaStateEntity
    state_entry_time    TIMESTAMP,
    sla_yellow_date     TIMESTAMP,
    sla_red_date        TIMESTAMP,
    sla_tending_late    INT DEFAULT 0,
    sla_late            INT DEFAULT 0,
    "flowId"            VARCHAR(100),
    "stateId"           VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS returnrequest_activity (
    id                  VARCHAR(36) PRIMARY KEY,
    returnrequest_id    VARCHAR(36) REFERENCES returnrequest(id) ON DELETE CASCADE,
    activity_name       VARCHAR(255) NOT NULL,
    activity_success    BOOLEAN NOT NULL,
    activity_comment    TEXT,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

-- 2. Promo Code (STM Aggregate Root - uses business key 'code' as PK, not UUID)
CREATE TABLE IF NOT EXISTS promo_code (
    id                  VARCHAR(36) PRIMARY KEY,
    code                VARCHAR(100) UNIQUE NOT NULL,
    description         VARCHAR(1000),
    discount_type       VARCHAR(50),       -- PERCENTAGE, FLAT
    discount_value      NUMERIC(10,2),
    min_order_amount    NUMERIC(10,2),
    max_discount_amount NUMERIC(10,2),
    expiry_date         TIMESTAMP,
    valid_product_ids   JSONB,
    valid_category_ids  JSONB,
    usage_count         INT DEFAULT 0,
    max_usage           INT,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE,
    -- AbstractJpaStateEntity
    state_entry_time    TIMESTAMP,
    sla_yellow_date     TIMESTAMP,
    sla_red_date        TIMESTAMP,
    sla_tending_late    INT DEFAULT 0,
    sla_late            INT DEFAULT 0,
    "flowId"            VARCHAR(100),
    "stateId"           VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS promo_code_activity_log (
    id                  VARCHAR(36) PRIMARY KEY,
    promo_code          VARCHAR(100) REFERENCES promo_code(code) ON DELETE CASCADE,
    activity_name       VARCHAR(255) NOT NULL,
    activity_success    BOOLEAN NOT NULL,
    activity_comment    TEXT,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

-- 3. Inventory Reservation (no STM - plain BaseJpaEntity)
CREATE TABLE IF NOT EXISTS inventory_reservation (
    id                  VARCHAR(36) PRIMARY KEY,
    inventory_id        VARCHAR(36) NOT NULL REFERENCES inventory(id) ON DELETE CASCADE,
    order_id            VARCHAR(36) NOT NULL REFERENCES orders(id),
    quantity            INT NOT NULL,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

-- 4. Catalog Module tables (all extend BaseJpaEntity, no STM)

CREATE TABLE IF NOT EXISTS categories (
    id                  VARCHAR(36) PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) UNIQUE,
    description         VARCHAR(1000),
    parent_id           VARCHAR(36) REFERENCES categories(id),
    image_url           VARCHAR(500),
    display_order       INT,
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

CREATE TABLE IF NOT EXISTS collections (
    id                  VARCHAR(36) PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) UNIQUE,
    description         VARCHAR(1000),
    image_url           VARCHAR(500),
    display_order       INT,
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

CREATE TABLE IF NOT EXISTS catalog_items (
    id                      VARCHAR(36) PRIMARY KEY,
    product_id              VARCHAR(36) NOT NULL REFERENCES products(id),
    featured                BOOLEAN DEFAULT FALSE,
    display_order           INT,
    active                  BOOLEAN DEFAULT TRUE,
    visibility_start_date   TIMESTAMP,
    visibility_end_date     TIMESTAMP,
    name                    VARCHAR(255),       -- denormalized projection from product
    price                   NUMERIC(10,2),      -- denormalized projection from offer
    -- BaseJpaEntity
    created_time            TIMESTAMP,
    last_modified_time      TIMESTAMP,
    last_modified_by        VARCHAR(100),
    tenant                  VARCHAR(50),
    created_by              VARCHAR(100),
    version                 BIGINT DEFAULT 0,
    test_entity             BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS catalog_item_tags (
    catalog_item_id     VARCHAR(36) NOT NULL REFERENCES catalog_items(id) ON DELETE CASCADE,
    tag                 VARCHAR(50) NOT NULL,
    PRIMARY KEY (catalog_item_id, tag)
);

CREATE TABLE IF NOT EXISTS category_product_mapping (
    id                  VARCHAR(36) PRIMARY KEY,
    category_id         VARCHAR(36) NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id),
    display_order       INT,
    UNIQUE (category_id, product_id),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS collection_product_mapping (
    id                  VARCHAR(36) PRIMARY KEY,
    collection_id       VARCHAR(36) NOT NULL REFERENCES collections(id) ON DELETE CASCADE,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id),
    display_order       INT,
    UNIQUE (collection_id, product_id),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

-- 5. Indexes
CREATE INDEX IF NOT EXISTS idx_returnrequest_order ON returnrequest(order_id);
CREATE INDEX IF NOT EXISTS idx_inventory_reservation_inventory ON inventory_reservation(inventory_id);
CREATE INDEX IF NOT EXISTS idx_catalog_items_product ON catalog_items(product_id);
CREATE INDEX IF NOT EXISTS idx_catalog_items_active ON catalog_items(active, featured);
CREATE INDEX IF NOT EXISTS idx_categories_parent ON categories(parent_id);
CREATE INDEX IF NOT EXISTS idx_promo_code_expiry ON promo_code(expiry_date);
