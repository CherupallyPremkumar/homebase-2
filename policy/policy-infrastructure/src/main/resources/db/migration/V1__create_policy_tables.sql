-- V1__create_policy_tables.sql

CREATE TABLE policies (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    default_effect VARCHAR(20) DEFAULT 'DENY'
);

CREATE TABLE rules (
    id VARCHAR(50) PRIMARY KEY,
    policy_id VARCHAR(50),
    name VARCHAR(255) NOT NULL,
    expression TEXT NOT NULL,
    effect VARCHAR(20) DEFAULT 'ALLOW',
    priority INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (policy_id) REFERENCES policies(id)
);

CREATE TABLE decisions (
    id VARCHAR(50) PRIMARY KEY,
    policy_id VARCHAR(50),
    subject_id VARCHAR(50),
    resource VARCHAR(255),
    action VARCHAR(50),
    effect VARCHAR(20),
    reasons TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE decision_metadata (
    decision_id VARCHAR(50),
    meta_key VARCHAR(255),
    meta_value TEXT,
    PRIMARY KEY (decision_id, meta_key),
    FOREIGN KEY (decision_id) REFERENCES decisions(id)
);
