DROP TABLE IF EXISTS ticket_activity;
DROP TABLE IF EXISTS ticket_messages;
DROP TABLE IF EXISTS support_tickets;

CREATE TABLE support_tickets (
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
    customer_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(255),
    subject VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    priority VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    assigned_agent_id VARCHAR(255),
    resolved_at TIMESTAMP,
    reopen_count INT DEFAULT 0,
    sla_breached BOOLEAN DEFAULT FALSE,
    auto_close_ready BOOLEAN DEFAULT FALSE,
    channel VARCHAR(50) DEFAULT 'WEB',
    related_entity_type VARCHAR(50),
    related_entity_id VARCHAR(255),
    satisfaction_rating INT,
    resolution_notes VARCHAR(2000),
    escalated BOOLEAN DEFAULT FALSE,
    escalation_reason VARCHAR(500)
);

INSERT INTO support_tickets (id, customer_id, order_id, subject, category, priority, description, assigned_agent_id, resolved_at, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('ticket-1', 'user-1', 'ord-1', 'Where is my order? Tracking not updating', 'DELIVERY', 'MEDIUM', 'Order tracking shows no update for 24 hours.', 'user-8', '2026-03-13 15:00:00', 'RESOLVED', 'support-flow', '2026-03-13 09:00:00', '2026-03-13 15:00:00', 'homebase', 5);

INSERT INTO support_tickets (id, customer_id, order_id, subject, category, priority, description, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('ticket-2', 'user-2', 'ord-5', 'Refund not received after 5 business days', 'PAYMENT', 'HIGH', 'Return was approved but refund not received.', 'OPEN', 'support-flow', '2026-03-15 10:00:00', '2026-03-15 10:00:00', 'homebase', 0);

INSERT INTO support_tickets (id, customer_id, order_id, subject, category, priority, description, assigned_agent_id, sla_breached, escalated, escalation_reason, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('ticket-3', 'user-3', 'ord-8', 'Package returned to origin', 'DELIVERY', 'CRITICAL', 'My order was returned to warehouse.', 'user-8', true, true, 'SLA breached', 'ESCALATED', 'support-flow', '2026-03-11 10:00:00', '2026-03-14 10:00:00', 'homebase', 6);

INSERT INTO support_tickets (id, customer_id, subject, category, priority, description, resolved_at, auto_close_ready, satisfaction_rating, state_id, flow_id, created_time, last_modified_time, tenant, version)
VALUES ('ticket-4', 'user-1', 'How to change my default address?', 'ACCOUNT', 'LOW', 'I want to change my default delivery address.', '2026-03-10 11:00:00', true, 5, 'CLOSED', 'support-flow', '2026-03-10 10:00:00', '2026-03-13 10:00:00', 'homebase', 4);
