-- V1__Create_Offer_Tables.sql

CREATE TABLE offer (
    id VARCHAR(50) PRIMARY KEY,
    variant_id VARCHAR(50) NOT NULL,
    supplier_id VARCHAR(50) NOT NULL,
    price_amount DECIMAL(19, 2),
    price_currency VARCHAR(3),
    msrp_amount DECIMAL(19, 2),
    msrp_currency VARCHAR(3),
    current_state VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE offer_activity_log (
    id VARCHAR(50) PRIMARY KEY,
    offer_id VARCHAR(50) NOT NULL,
    event_id VARCHAR(50) NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_offer_log FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE
);

CREATE INDEX idx_offer_variant ON offer(variant_id);
CREATE INDEX idx_offer_supplier ON offer(supplier_id);
CREATE INDEX idx_offer_state ON offer(current_state);
