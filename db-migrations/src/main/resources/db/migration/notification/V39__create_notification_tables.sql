-- V39: Notifications (STM for delivery tracking)

CREATE TABLE IF NOT EXISTS notifications (
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id),
    channel             VARCHAR(50) NOT NULL,          -- EMAIL, SMS, PUSH, IN_APP
    template_code       VARCHAR(100),
    subject             VARCHAR(500),
    body                TEXT,
    reference_type      VARCHAR(50),                   -- ORDER, SHIPMENT, PROMO, SYSTEM
    reference_id        VARCHAR(36),
    read_at             TIMESTAMP,
    sent_at             TIMESTAMP,
    error_message       TEXT,
    retry_count         INT DEFAULT 0,
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

CREATE TABLE IF NOT EXISTS notification_templates (
    id                  VARCHAR(36) PRIMARY KEY,
    code                VARCHAR(100) UNIQUE NOT NULL,
    channel             VARCHAR(50) NOT NULL,
    subject_template    VARCHAR(500),
    body_template       TEXT NOT NULL,
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

CREATE TABLE IF NOT EXISTS notification_preferences (
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    channel             VARCHAR(50) NOT NULL,
    category            VARCHAR(50) NOT NULL,          -- ORDER_UPDATES, PROMOTIONS, SYSTEM
    enabled             BOOLEAN DEFAULT TRUE,
    UNIQUE (user_id, channel, category),
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS notification_activity (
    id                  VARCHAR(36) PRIMARY KEY,
    notification_id     VARCHAR(36) NOT NULL REFERENCES notifications(id) ON DELETE CASCADE,
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

CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_channel ON notifications(channel, "stateId");
CREATE INDEX IF NOT EXISTS idx_notifications_reference ON notifications(reference_type, reference_id);
CREATE INDEX IF NOT EXISTS idx_notification_prefs_user ON notification_preferences(user_id);
