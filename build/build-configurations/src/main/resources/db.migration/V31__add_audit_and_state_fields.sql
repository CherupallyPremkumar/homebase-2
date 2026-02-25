-- V31: Add BaseJpaEntity and AbstractJpaStateEntity fields for consistent auditing and state management
-- Aligning with user-provided Java definitions for org.chenile.jpautils.entity.BaseJpaEntity and AbstractJpaStateEntity

-- 1. Auditing fields for all domain tables (BaseJpaEntity)
-- Fields: created_time, last_modified_time, last_modified_by, tenant, created_by, version, test_entity

DO $$ 
BEGIN 
    -- Generic list of all core tables to receive BaseJpaEntity fields
    
    FOR r IN (SELECT table_name 
              FROM information_schema.tables 
              WHERE table_schema = 'public' 
              AND table_name IN ('users', 'products', 'orders', 'order_items', 'inventory', 
                                 'shipments', 'payouts', 'payout_lines', 'refunds', 
                                 'refund_requests', 'cart', 'cart_item', 'supplier', 
                                 'supplierproduct', 'webhook_events')) 
    LOOP
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS created_time TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS last_modified_time TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS last_modified_by VARCHAR(100)';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS tenant VARCHAR(50)';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS created_by VARCHAR(100)';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS test_entity BOOLEAN DEFAULT FALSE';
    END LOOP;

    -- 2. State management fields for Aggregate Roots (AbstractJpaStateEntity)
    -- Fields: state_entry_time, sla_yellow_date, sla_red_date, sla_tending_late, sla_late, flowId, stateId
    -- Note: Using camelCase for flowId and stateId as per user @AttributeOverride requirement
    
    FOR r IN (SELECT table_name 
              FROM information_schema.tables 
              WHERE table_schema = 'public' 
              AND table_name IN ('users', 'products', 'orders', 'shipments', 'inventory', 
                                 'payouts', 'refunds', 'refund_requests', 'cart', 
                                 'supplier', 'supplierproduct')) 
    LOOP
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS state_entry_time TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS sla_yellow_date TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS sla_red_date TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS sla_tending_late INT DEFAULT 0';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS sla_late INT DEFAULT 0';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS "flowId" VARCHAR(100)';
        EXECUTE 'ALTER TABLE ' || r.table_name || ' ADD COLUMN IF NOT EXISTS "stateId" VARCHAR(100)';
    END LOOP;

END $$;
