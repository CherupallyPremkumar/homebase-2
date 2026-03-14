-- V44: CMS pages and banners

CREATE TABLE IF NOT EXISTS cms_pages (
    id                  VARCHAR(36) PRIMARY KEY,
    slug                VARCHAR(255) UNIQUE NOT NULL,
    title               VARCHAR(500) NOT NULL,
    body                TEXT,
    page_type           VARCHAR(50),                    -- STATIC, LANDING, FAQ, POLICY
    meta_title          VARCHAR(255),
    meta_description    VARCHAR(500),
    published           BOOLEAN DEFAULT FALSE,
    published_at        TIMESTAMP,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS banners (
    id                  VARCHAR(36) PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    image_url           VARCHAR(1000) NOT NULL,
    mobile_image_url    VARCHAR(1000),
    link_url            VARCHAR(1000),
    position            VARCHAR(50),                    -- HERO, SIDEBAR, FOOTER, CATEGORY
    display_order       INT DEFAULT 0,
    active_from         TIMESTAMP,
    active_to           TIMESTAMP,
    active              BOOLEAN DEFAULT TRUE,
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_cms_pages_slug ON cms_pages(slug);
CREATE INDEX IF NOT EXISTS idx_banners_position ON banners(position, active);
CREATE INDEX IF NOT EXISTS idx_banners_dates ON banners(active_from, active_to) WHERE active = TRUE;
