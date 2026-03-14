-- Add optimistic locking version column to support multi-instance concurrency correctness

ALTER TABLE inventory
ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
