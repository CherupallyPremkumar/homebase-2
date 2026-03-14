-- =====================================================
-- Table Partitioning for Large Tables
-- =====================================================

-- Partition checkout_audit_log by month (for better performance on large datasets)
CREATE TABLE checkout_audit_log_partitioned (
    LIKE checkout_audit_log INCLUDING ALL
) PARTITION BY RANGE (created_at);

-- Create partitions for current year and next 2 years
DO $$
DECLARE
    start_date DATE;
    end_date DATE;
    partition_name TEXT;
BEGIN
    FOR i IN 0..24 LOOP  -- 24 months (2 years)
        start_date := DATE_TRUNC('month', CURRENT_DATE) + (i || ' months')::INTERVAL;
        end_date := start_date + INTERVAL '1 month';
        partition_name := 'checkout_audit_log_' || TO_CHAR(start_date, 'YYYY_MM');
        
        EXECUTE format(
            'CREATE TABLE IF NOT EXISTS %I PARTITION OF checkout_audit_log_partitioned
             FOR VALUES FROM (%L) TO (%L)',
            partition_name, start_date, end_date
        );
    END LOOP;
END $$;

-- Partition saga_execution_log by month
CREATE TABLE saga_execution_log_partitioned (
    LIKE saga_execution_log INCLUDING ALL
) PARTITION BY RANGE (started_at);

-- Create partitions for saga_execution_log
DO $$
DECLARE
    start_date DATE;
    end_date DATE;
    partition_name TEXT;
BEGIN
    FOR i IN 0..24 LOOP
        start_date := DATE_TRUNC('month', CURRENT_DATE) + (i || ' months')::INTERVAL;
        end_date := start_date + INTERVAL '1 month';
        partition_name := 'saga_execution_log_' || TO_CHAR(start_date, 'YYYY_MM');
        
        EXECUTE format(
            'CREATE TABLE IF NOT EXISTS %I PARTITION OF saga_execution_log_partitioned
             FOR VALUES FROM (%L) TO (%L)',
            partition_name, start_date, end_date
        );
    END LOOP;
END $$;
