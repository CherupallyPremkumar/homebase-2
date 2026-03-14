-- V27__add_resolved_by_index.sql
-- Ensure resolved_by exists and is indexed (gateway-agnostic reconciliation uses this for auditability).

ALTER TABLE reconciliation_mismatches
    ADD COLUMN IF NOT EXISTS resolved_by VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_recon_mismatch_resolved_by
    ON reconciliation_mismatches(resolved_by);
