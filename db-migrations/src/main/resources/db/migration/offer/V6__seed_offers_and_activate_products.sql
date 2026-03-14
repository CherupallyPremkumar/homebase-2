-- V6: Seed offers for existing products and activate them
-- Since Rule 5 moved price/stock out of Product, we need to populate the offers table

-- prod-001: Wireless Bluetooth Headphones
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-001', 'SYSTEM', 7999.00, true);

-- prod-002: Organic Cotton T-Shirt
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-002', 'SYSTEM', 2999.00, true);

-- prod-003: Stainless Steel Water Bottle
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-003', 'SYSTEM', 2499.00, true);

-- prod-004: Running Shoes
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-004', 'SYSTEM', 11999.00, true);

-- prod-005: Leather Wallet
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-005', 'SYSTEM', 3999.00, true);

-- prod-006: Smart Watch
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-006', 'SYSTEM', 19999.00, true);

-- prod-007: Yoga Mat
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-007', 'SYSTEM', 3499.00, true);

-- prod-008: Coffee Maker
INSERT INTO offers (id, product_id, seller_id, price, active)
VALUES (gen_random_uuid(), 'prod-008', 'SYSTEM', 5999.00, true);

-- Set all existing products to ACTIVE
UPDATE products SET status = 'ACTIVE' WHERE status = 'DRAFT';
