-- Shipments table for tracking order shipments
CREATE TABLE shipments (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    carrier VARCHAR(50) NOT NULL,
    tracking_number VARCHAR(255) NOT NULL,
    tracking_url VARCHAR(512),
    shipped_at TIMESTAMP,
    estimated_delivery TIMESTAMP,
    status VARCHAR(30) DEFAULT 'SHIPPED',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_shipment_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE INDEX idx_shipment_order ON shipments(order_id);
