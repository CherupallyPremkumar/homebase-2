-- V42: Supplier onboarding, KYC documents, and bank details (STM)

CREATE TABLE IF NOT EXISTS supplier_onboarding (
    id                  VARCHAR(36) PRIMARY KEY,
    supplier_id         VARCHAR(36) NOT NULL REFERENCES supplier(id),
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

CREATE TABLE IF NOT EXISTS supplier_documents (
    id                  VARCHAR(36) PRIMARY KEY,
    supplier_id         VARCHAR(36) NOT NULL REFERENCES supplier(id),
    onboarding_id       VARCHAR(36) REFERENCES supplier_onboarding(id),
    document_type       VARCHAR(50) NOT NULL,           -- PAN, GST, BANK_PROOF, AADHAAR, TRADE_LICENSE
    document_number     VARCHAR(100),
    file_url            VARCHAR(1000),
    verification_status VARCHAR(50) DEFAULT 'PENDING',  -- PENDING, VERIFIED, REJECTED
    rejection_reason    VARCHAR(500),
    verified_at         TIMESTAMP,
    verified_by         VARCHAR(100),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS supplier_bank_details (
    id                  VARCHAR(36) PRIMARY KEY,
    supplier_id         VARCHAR(36) NOT NULL REFERENCES supplier(id),
    account_holder_name VARCHAR(255) NOT NULL,
    account_number      VARCHAR(50) NOT NULL,
    ifsc_code           VARCHAR(20) NOT NULL,
    bank_name           VARCHAR(255),
    branch_name         VARCHAR(255),
    is_primary          BOOLEAN DEFAULT TRUE,
    verified            BOOLEAN DEFAULT FALSE,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS onboarding_activity (
    id                  VARCHAR(36) PRIMARY KEY,
    onboarding_id       VARCHAR(36) NOT NULL REFERENCES supplier_onboarding(id) ON DELETE CASCADE,
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

CREATE INDEX IF NOT EXISTS idx_supplier_onboarding_supplier ON supplier_onboarding(supplier_id);
CREATE INDEX IF NOT EXISTS idx_supplier_documents_supplier ON supplier_documents(supplier_id);
CREATE INDEX IF NOT EXISTS idx_supplier_bank_supplier ON supplier_bank_details(supplier_id);
