DROP TABLE IF EXISTS review_responses;
DROP TABLE IF EXISTS review_votes;
DROP TABLE IF EXISTS review_images;
DROP TABLE IF EXISTS review_activity;
DROP TABLE IF EXISTS product_reviews;

CREATE TABLE product_reviews (
    id VARCHAR(255) PRIMARY KEY,
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(100),
    tenant VARCHAR(50),
    created_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    state_entry_time TIMESTAMP,
    sla_yellow_date TIMESTAMP,
    sla_red_date TIMESTAMP,
    sla_tending_late INT DEFAULT 0,
    sla_late INT DEFAULT 0,
    flow_id VARCHAR(100),
    state_id VARCHAR(100),
    product_id VARCHAR(255) NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(255),
    rating INT NOT NULL,
    title VARCHAR(255),
    body TEXT,
    images TEXT,
    verified_purchase BOOLEAN,
    helpful_count INT,
    report_count INT,
    moderator_notes TEXT,
    variant_id VARCHAR(255),
    review_source VARCHAR(50) DEFAULT 'WEB'
);

INSERT INTO product_reviews (id, product_id, customer_id, order_id, rating, title, body, verified_purchase, helpful_count, report_count, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('rev-1', 'prod-1', 'user-1', 'ord-1', 5, 'Excellent quality widget!', 'This widget is exactly what I needed.', true, 23, 0, 'PUBLISHED', 'review-flow', '2026-03-14 10:00:00', '2026-03-14 10:05:00', 'homebase', 2);

INSERT INTO product_reviews (id, product_id, customer_id, order_id, rating, title, body, verified_purchase, helpful_count, report_count, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('rev-2', 'prod-2', 'user-2', 'ord-2', 3, 'Decent but Bluetooth range is poor', 'The gadget works fine for basic use.', true, 15, 0, 'PUBLISHED', 'review-flow', '2026-03-15 14:00:00', '2026-03-15 14:10:00', 'homebase', 2);

INSERT INTO product_reviews (id, product_id, customer_id, order_id, rating, title, body, verified_purchase, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('rev-3', 'prod-3', 'user-3', 'ord-3', 4, 'Good value for money', 'Multi-purpose gizmo that does what it promises.', true, 'SUBMITTED', 'review-flow', '2026-03-16 10:00:00', '2026-03-16 10:00:00', 'homebase', 0);

INSERT INTO product_reviews (id, product_id, customer_id, rating, title, body, verified_purchase, helpful_count, report_count, moderator_notes, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('rev-4', 'prod-1', 'user-5', 1, 'DO NOT BUY FAKE PRODUCT', 'This is a fake product.', false, 2, 8, 'Flagged: no purchase history', 'FLAGGED', 'review-flow', '2026-03-10 08:00:00', '2026-03-10 12:00:00', 'homebase', 3);

INSERT INTO product_reviews (id, product_id, customer_id, rating, title, body, moderator_notes, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('rev-5', 'prod-2', 'user-5', 1, 'Profanity-laden review', '[Content removed]', 'Rejected: profanity', 'REJECTED', 'review-flow', '2026-03-05 10:00:00', '2026-03-05 15:00:00', 'homebase', 3);
