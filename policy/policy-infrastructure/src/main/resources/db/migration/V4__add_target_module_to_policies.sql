-- V4__add_target_module_to_policies.sql
ALTER TABLE policies ADD COLUMN target_module VARCHAR(50);
ALTER TABLE decisions ADD COLUMN target_module VARCHAR(50);
