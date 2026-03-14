-- V7: Create persistent_logins table for remember-me and seed admin user

-- Required by Spring Security's JdbcTokenRepositoryImpl
CREATE TABLE IF NOT EXISTS persistent_logins (
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) PRIMARY KEY,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL
);

-- Seed admin user (password: admin123 encoded with BCrypt)
INSERT INTO customers (id, email, password, first_name, last_name, role, created_at, updated_at)
VALUES (
    'admin-001',
    'admin@homemade.com',
    '$2a$10$EHNSKs.I6SBo8C/wm.USVOU/MuYyZvN1BBpyb3MqKVo5Rd1z.ZF96',
    'Admin',
    'User',
    'ROLE_ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;
