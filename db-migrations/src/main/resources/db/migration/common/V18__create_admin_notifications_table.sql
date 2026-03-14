-- Admin Notifications table for the bell icon notification system
CREATE TABLE admin_notifications (
    id VARCHAR(36) PRIMARY KEY,
    type VARCHAR(30) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    link VARCHAR(512),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_admin_notif_unread ON admin_notifications(is_read, created_at);
