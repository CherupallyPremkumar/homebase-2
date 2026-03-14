-- V1__init_user_schema.sql

CREATE TABLE user_profiles (
    id VARCHAR(255) PRIMARY KEY,
    created_time TIMESTAMP NOT NULL,
    last_modified_time TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,

    current_state VARCHAR(255),
    
    keycloak_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(50),
    avatar_url VARCHAR(1024),
    
    failed_login_attempts INT DEFAULT 0,
    lock_reason VARCHAR(1024),
    suspend_reason VARCHAR(1024),
    
    pref_language VARCHAR(10),
    pref_currency VARCHAR(10),
    pref_email_notif BOOLEAN DEFAULT true,
    pref_sms_notif BOOLEAN DEFAULT false,
    pref_timezone VARCHAR(50)
);

CREATE TABLE user_address (
    id VARCHAR(255) PRIMARY KEY,
    created_time TIMESTAMP NOT NULL,
    last_modified_time TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    
    label VARCHAR(255),
    line1 VARCHAR(255),
    line2 VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(50),
    zip VARCHAR(20),
    country VARCHAR(50),
    is_default BOOLEAN DEFAULT false,
    
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_address_user FOREIGN KEY (user_id) REFERENCES user_profiles(id) ON DELETE CASCADE
);

CREATE TABLE user_activity_log (
    id VARCHAR(255) PRIMARY KEY,
    created_time TIMESTAMP NOT NULL,
    last_modified_time TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    
    activity_name VARCHAR(255) NOT NULL,
    activity_success BOOLEAN DEFAULT true,
    activity_comment VARCHAR(2048),
    
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_activity_user FOREIGN KEY (user_id) REFERENCES user_profiles(id) ON DELETE CASCADE
);

-- Index for fast lookup by keycloakId
CREATE INDEX idx_user_profiles_keycloak_id ON user_profiles(keycloakId);
