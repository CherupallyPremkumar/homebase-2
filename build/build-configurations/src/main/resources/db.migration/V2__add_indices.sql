-- V2: Add performance indices and seed data

-- Additional performance indices
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_payment_transactions_order ON payment_transactions(order_id);

-- Seed sample products for testing
INSERT INTO products (id, name, description, price, category, in_stock) VALUES
    ('prod-001', 'Wireless Bluetooth Headphones', 'Premium wireless headphones with noise cancellation, 30-hour battery life, and comfortable over-ear design.', 7999.00, 'Electronics', true),
    ('prod-002', 'Organic Cotton T-Shirt', 'Soft, breathable organic cotton t-shirt available in multiple colors. Sustainable and comfortable.', 2999.00, 'Clothing', true),
    ('prod-003', 'Stainless Steel Water Bottle', 'Double-wall vacuum insulated water bottle. Keeps drinks cold for 24 hours or hot for 12 hours.', 2499.00, 'Kitchen', true),
    ('prod-004', 'Running Shoes', 'Lightweight running shoes with responsive cushioning and breathable mesh upper.', 11999.00, 'Sports', true),
    ('prod-005', 'Leather Wallet', 'Genuine leather bifold wallet with RFID blocking technology. Slim profile with multiple card slots.', 3999.00, 'Accessories', true),
    ('prod-006', 'Smart Watch', 'Feature-rich smartwatch with heart rate monitor, GPS, and 7-day battery life.', 19999.00, 'Electronics', true),
    ('prod-007', 'Yoga Mat', 'Non-slip premium yoga mat with alignment lines. Extra thick for comfort.', 3499.00, 'Sports', true),
    ('prod-008', 'Coffee Maker', 'Programmable 12-cup drip coffee maker with thermal carafe and auto-shutoff.', 5999.00, 'Kitchen', true);

-- Seed inventory for products
INSERT INTO inventory (id, product_id, quantity, reserved, low_stock_threshold) VALUES
    ('inv-001', 'prod-001', 50, 0, 10),
    ('inv-002', 'prod-002', 100, 0, 20),
    ('inv-003', 'prod-003', 75, 0, 15),
    ('inv-004', 'prod-004', 30, 0, 5),
    ('inv-005', 'prod-005', 60, 0, 10),
    ('inv-006', 'prod-006', 25, 0, 5),
    ('inv-007', 'prod-007', 80, 0, 15),
    ('inv-008', 'prod-008', 40, 0, 10);
