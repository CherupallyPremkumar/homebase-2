-- V5: Add status column to products table and remove old price/in_stock columns
ALTER TABLE products ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'DRAFT';
ALTER TABLE products DROP COLUMN IF EXISTS price;
ALTER TABLE products DROP COLUMN IF EXISTS in_stock;

CREATE INDEX idx_products_status ON products(status);
