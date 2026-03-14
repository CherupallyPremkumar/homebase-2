-- V31: Add BaseJpaEntity and AbstractJpaStateEntity fields for consistent auditing and state management
-- Aligns ALL entity tables with org.chenile.jpautils.entity.BaseJpaEntity and AbstractJpaStateEntity
-- BaseJpaEntity fields: created_time, last_modified_time, last_modified_by, tenant, created_by, version, test_entity
-- AbstractJpaStateEntity extra fields: state_entry_time, sla_yellow_date, sla_red_date, sla_tending_late, sla_late, flowId, stateId

DO $$
BEGIN

    -- ─────────────────────────────────────────────────────────────────────────────
    -- 1. BaseJpaEntity audit columns → ALL entity tables
    -- Includes all domain aggregate roots, line items, and payment infrastructure
    -- ─────────────────────────────────────────────────────────────────────────────
    FOR r IN (SELECT table_name
              FROM information_schema.tables
              WHERE table_schema = 'public'
              AND table_name IN (
                  -- Domain aggregate roots & line items
                  'users', 'products', 'orders', 'order_items',
                  'inventory', 'shipments', 'cart', 'cart_item',
                  'supplier', 'supplierproduct', 'offers',
                  -- Settlement
                  'settlements', 'settlement_lines',
                  -- Return requests
                  'returnrequest',
                  -- Payment aggregate roots (extend AbstractJpaStateEntity)
                  'payouts', 'refunds', 'refund_requests',
                  -- Payment infrastructure (extend BaseJpaEntity directly)
                  'payment_transactions', 'webhook_events', 'provider_objects',
                  'reconciliation_mismatches', 'reconciliation_items',
                  'reconciliation_runs', 'provider_balance_transactions', 'payout_lines',
                  -- Activity logs (extend BaseJpaEntity)
                  'payment_activity', 'user_activity', 'product_activity',
                  'order_activity', 'inventory_activity', 'shipping_activity',
                  'payout_activity', 'supplier_activity', 'supplierproduct_activity',
                  'cart_activity', 'settlement_activity'
              ))
    LOOP
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS created_time TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS last_modified_time TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS last_modified_by VARCHAR(100)';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS tenant VARCHAR(50)';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS created_by VARCHAR(100)';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS test_entity BOOLEAN DEFAULT FALSE';
    END LOOP;

    -- ─────────────────────────────────────────────────────────────────────────────
    -- 2. AbstractJpaStateEntity STM columns → Aggregate roots only
    -- These are the entities that drive workflow state machines (Chenile STM)
    -- ─────────────────────────────────────────────────────────────────────────────
    FOR r IN (SELECT table_name
              FROM information_schema.tables
              WHERE table_schema = 'public'
              AND table_name IN (
                  'users', 'products', 'orders', 'inventory', 'shipments',
                  'cart', 'supplier', 'supplierproduct',
                  'settlements', 'returnrequest',
                  'payouts', 'refunds', 'refund_requests'
              ))
    LOOP
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS state_entry_time TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS sla_yellow_date TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS sla_red_date TIMESTAMP';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS sla_tending_late INT DEFAULT 0';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS sla_late INT DEFAULT 0';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS "flowId" VARCHAR(100)';
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ADD COLUMN IF NOT EXISTS "stateId" VARCHAR(100)';
    END LOOP;

END $$;
