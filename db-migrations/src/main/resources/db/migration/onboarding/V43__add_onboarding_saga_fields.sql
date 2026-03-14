-- V43: Add onboarding saga fields to supplier_onboarding table

ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS supplier_name VARCHAR(255);
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS email VARCHAR(255);
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS phone VARCHAR(50);
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS upi_id VARCHAR(255);
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS commission_percentage DOUBLE PRECISION;
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS error_message TEXT;
ALTER TABLE supplier_onboarding ADD COLUMN IF NOT EXISTS retry_count INT DEFAULT 0;
