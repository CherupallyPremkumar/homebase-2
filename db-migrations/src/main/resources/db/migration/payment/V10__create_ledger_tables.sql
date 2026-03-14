-- V10__create_ledger_tables.sql

CREATE TABLE ledger_accounts (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_ledger_accounts_name ON ledger_accounts(name);

CREATE TABLE ledger_entries (
    id VARCHAR(255) PRIMARY KEY,
    ledger_account_id VARCHAR(255) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_ledger_entries_account FOREIGN KEY (ledger_account_id) REFERENCES ledger_accounts(id)
);

CREATE INDEX idx_ledger_entries_account ON ledger_entries(ledger_account_id);
CREATE INDEX idx_ledger_entries_transaction ON ledger_entries(transaction_id);

-- Seed basic accounts
INSERT INTO ledger_accounts (id, name, type, balance, created_at, updated_at) VALUES
('acc_ar_stripe', 'Accounts Receivable (Stripe)', 'ASSET', 0.00, NOW(), NOW()),
('acc_revenue_sales', 'Revenue (Sales)', 'REVENUE', 0.00, NOW(), NOW()),
('acc_expense_stripe_fees', 'Expense (Stripe Fees)', 'EXPENSE', 0.00, NOW(), NOW()),
('acc_liability_refunds', 'Customer Liability (Refunds)', 'LIABILITY', 0.00, NOW(), NOW());
