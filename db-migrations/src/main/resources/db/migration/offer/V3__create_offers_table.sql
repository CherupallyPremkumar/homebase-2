-- V3: Create offers table for Product-Seller-Price decoupling
CREATE TABLE offers (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL REFERENCES products(id),
    seller_id VARCHAR(36) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_offers_product_id ON offers(product_id);
CREATE INDEX idx_offers_seller_id ON offers(seller_id);
