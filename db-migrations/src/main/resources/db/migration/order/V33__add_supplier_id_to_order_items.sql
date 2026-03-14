-- V33: Add supplier_id to order_items for settlement tracking
ALTER TABLE order_items ADD COLUMN supplier_id VARCHAR(36);
-- Set default value for existing items if any (though unlikely for new setup)
-- UPDATE order_items SET supplier_id = 'default-supplier' WHERE supplier_id IS NULL;
-- ALTER TABLE order_items ALTER COLUMN supplier_id SET NOT NULL;
CREATE INDEX idx_order_items_supplier ON order_items(supplier_id);
