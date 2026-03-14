-- V43: Support tickets with message thread (STM for SLA tracking)

CREATE TABLE IF NOT EXISTS support_tickets (
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL REFERENCES users(id),
    order_id            VARCHAR(36) REFERENCES orders(id),
    subject             VARCHAR(500) NOT NULL,
    category            VARCHAR(50),                    -- ORDER_ISSUE, PAYMENT, SHIPPING, PRODUCT, OTHER
    priority            VARCHAR(20) DEFAULT 'MEDIUM',   -- LOW, MEDIUM, HIGH, URGENT
    assigned_to         VARCHAR(100),
    resolved_at         TIMESTAMP,
    description         VARCHAR(2000),
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

CREATE TABLE IF NOT EXISTS ticket_messages (
    id                  VARCHAR(36) PRIMARY KEY,
    ticket_id           VARCHAR(36) NOT NULL REFERENCES support_tickets(id) ON DELETE CASCADE,
    sender_id           VARCHAR(36) NOT NULL,
    sender_type         VARCHAR(20) NOT NULL,           -- CUSTOMER, AGENT, SYSTEM
    message             TEXT NOT NULL,
    attachments         JSONB,                          -- [{url, filename, size}]
    -- BaseJpaEntity
    created_time        TIMESTAMP,
    last_modified_time  TIMESTAMP,
    last_modified_by    VARCHAR(100),
    tenant              VARCHAR(50),
    created_by          VARCHAR(100),
    version             BIGINT DEFAULT 0,
    test_entity         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ticket_activity (
    id                  VARCHAR(36) PRIMARY KEY,
    ticket_id           VARCHAR(36) NOT NULL REFERENCES support_tickets(id) ON DELETE CASCADE,
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

CREATE INDEX IF NOT EXISTS idx_tickets_user ON support_tickets(user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_order ON support_tickets(order_id);
CREATE INDEX IF NOT EXISTS idx_tickets_status ON support_tickets("stateId", priority);
CREATE INDEX IF NOT EXISTS idx_tickets_assigned ON support_tickets(assigned_to) WHERE assigned_to IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_ticket_messages_ticket ON ticket_messages(ticket_id);
