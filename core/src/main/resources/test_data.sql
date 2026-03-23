-- Test data for Query Services
INSERT INTO products (id, name, description, image_url, category, created_at, updated_at) VALUES 
('prod-1', 'Hand-woven Saree', 'Silk saree', 'http://image/prod-1', 'Apparel', NOW(), NOW()),
('prod-2', 'Wooden Toys', 'Safe for kids', 'http://image/prod-2', 'Toys', NOW(), NOW()),
('prod-3', 'Brass Lamp', 'Traditional lamp', 'http://image/prod-3', 'Home Decor', NOW(), NOW());

INSERT INTO users (id, first_name, last_name, email, phone, role, created_at, updated_at) VALUES 
('user-1', 'John', 'Doe', 'john@example.com', '1234567890', 'CUSTOMER', NOW(), NOW()),
('user-2', 'Jane', 'Smith', 'jane@example.com', '0987654321', 'SUPPLIER', NOW(), NOW());

INSERT INTO supplier (id, user_id, name, description, email, phone, status, state_id) VALUES 
('supp-1', 'user-2', 'Artisan Works', 'Handicraft supplier', 'jane@example.com', '0987654321', 'ACTIVE', 'ACTIVE');

INSERT INTO supplierproduct (id, supplier_id, product_id, status, state_id, created_at) VALUES 
('sp-1', 'supp-1', 'prod-1', 'ACTIVE', 'ACTIVE', NOW()),
('sp-2', 'supp-1', 'prod-2', 'ACTIVE', 'ACTIVE', NOW());

INSERT INTO offers (id, product_id, seller_id, price, currency, active) VALUES 
('off-1', 'prod-1', 'supp-1', 199.99, 'USD', true),
('off-2', 'prod-2', 'supp-1', 49.99, 'USD', true),
('off-3', 'prod-3', 'supp-1', 29.99, 'USD', false);

INSERT INTO cart (id, customer_id, total_amount, currency, status, state_id, created_at, updated_at) VALUES 
('cart-1', 'user-1', 249.98, 'USD', 'ACTIVE', 'OPEN', NOW(), NOW()),
('cart-2', 'user-1', 0.00, 'USD', 'CLOSED', 'CLOSED', NOW(), NOW());

INSERT INTO orders (id, customer_id, total_amount, currency, status, state_id, created_at, updated_at) VALUES 
('ord-1', 'user-1', 249.98, 'USD', 'PAID', 'PAID', NOW(), NOW()),
('ord-2', 'user-1', 49.99, 'USD', 'CREATED', 'CREATED', NOW(), NOW());

INSERT INTO refund_requests (id, order_id, amount, reason, status, state_id, created_at) VALUES 
('ref-1', 'ord-1', 49.99, 'Defective item', 'PENDING', 'PENDING', NOW());

INSERT INTO shipments (id, order_id, carrier, tracking_number, tracking_url, status, state_id, created_at) VALUES 
('shp-1', 'ord-1', 'FedEx', 'TRACK123', 'http://fedex/TRACK123', 'SHIPPED', 'SHIPPED', NOW());

INSERT INTO settlements (id, supplier_id, period_start, period_end, total_sales, net_payout, status, state_id, created_at) VALUES 
('stl-1', 'supp-1', '2023-01-01 00:00:00', '2023-01-31 23:59:59', 1000.00, 900.00, 'PAID', 'PAID', NOW()),
('stl-2', 'supp-1', '2023-02-01 00:00:00', '2023-02-28 23:59:59', 500.00, 450.00, 'PENDING', 'PENDING', NOW());
