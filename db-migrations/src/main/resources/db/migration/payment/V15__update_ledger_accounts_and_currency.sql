-- V14__update_ledger_accounts_and_currency.sql

ALTER TABLE ledger_accounts ADD COLUMN currency VARCHAR(3) DEFAULT 'INR' NOT NULL;

UPDATE ledger_accounts SET name = 'Account_Receivable_Gateway' WHERE id = 'acc_ar_stripe';
UPDATE ledger_accounts SET name = 'Sales_Revenue' WHERE id = 'acc_revenue_sales';
UPDATE ledger_accounts SET name = 'Payment_Gateway_Fee_Exp' WHERE id = 'acc_expense_stripe_fees';

INSERT INTO ledger_accounts (id, name, type, balance, currency, created_at, updated_at) 
VALUES ('acc_liability_stripe_fee', 'Fee_Payable_Stripe', 'LIABILITY', 0.00, 'INR', NOW(), NOW());
