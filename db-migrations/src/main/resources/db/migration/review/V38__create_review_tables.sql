-- V38: Reviews & Ratings (STM for moderation)

CREATE TABLE IF NOT EXISTS product_reviews (
    id                  VARCHAR(36) PRIMARY KEY,
    product_id          VARCHAR(36) NOT NULL REFERENCES products(id),
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id),
    order_id            VARCHAR(36) REFERENCES orders(id),
    rating              INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    title               VARCHAR(255),
    body                TEXT,
    verified_purchase   BOOLEAN DEFAULT FALSE,
    helpful_count       INT DEFAULT 0,
    unhelpful_count     INT DEFAULT 0,
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

CREATE TABLE IF NOT EXISTS review_images (
    id                  VARCHAR(36) PRIMARY KEY,
    review_id           VARCHAR(36) NOT NULL REFERENCES product_reviews(id) ON DELETE CASCADE,
    url                 VARCHAR(1000) NOT NULL,
    alt_text            VARCHAR(255),
    display_order       INT DEFAULT 0,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS review_activity (
    id                  VARCHAR(36) PRIMARY KEY,
    review_id           VARCHAR(36) NOT NULL REFERENCES product_reviews(id) ON DELETE CASCADE,
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

CREATE INDEX IF NOT EXISTS idx_reviews_product ON product_reviews(product_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user ON product_reviews(user_id);
CREATE INDEX IF NOT EXISTS idx_reviews_rating ON product_reviews(product_id, rating);
CREATE INDEX IF NOT EXISTS idx_review_images_review ON review_images(review_id);
