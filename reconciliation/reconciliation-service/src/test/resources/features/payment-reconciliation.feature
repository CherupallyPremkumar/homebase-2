Feature: Payment Reconciliation
  Tests the payment reconciliation OWIZ command chain that compares gateway
  transactions with system records, detects mismatches, auto-resolves minor
  differences, and generates a summary report.

  Background: Reconciliation runs for a given date period and gateway type

  Scenario: All transactions match
    Given gateway transactions TXN-001=$100, TXN-002=$250.50, TXN-003=$75
    And system transactions TXN-001=$100, TXN-002=$250.50, TXN-003=$75
    When reconciliation runs for period 2026-03-01 to 2026-03-14 with gateway STRIPE
    Then total transactions is 3
    And matched count is 3
    And mismatched count is 0
    And auto-resolved count is 0
    And pending review count is 0

  Scenario: Amount mismatch detected
    Given gateway transactions TXN-001=$100, TXN-002=$250
    And system transactions TXN-001=$100, TXN-002=$200
    When reconciliation runs for period 2026-03-01 to 2026-03-14 with gateway STRIPE
    Then total transactions is 2
    And matched count is 1
    And mismatched count is 1
    And mismatch for TXN-002 has type AMOUNT_MISMATCH with gateway=$250 and system=$200

  Scenario: Missing transaction in system
    Given gateway transactions TXN-001=$100, TXN-MISSING=$300
    And system transactions TXN-001=$100
    When reconciliation runs for period 2026-03-01 to 2026-03-14 with gateway STRIPE
    Then total transactions is 2
    And matched count is 1
    And mismatched count is 1
    And mismatch for TXN-MISSING has type MISSING_IN_SYSTEM

  Scenario: Auto-resolve within tolerance
    Given gateway transactions TXN-001=$100, TXN-002=$250.30
    And system transactions TXN-001=$100, TXN-002=$250
    When reconciliation runs for period 2026-03-01 to 2026-03-14 with gateway STRIPE
    Then total transactions is 2
    And matched count is 1
    And auto-resolved count is 1
    And pending review count is 0

  Scenario: Manual review required for large mismatch
    Given gateway transactions TXN-001=$100, TXN-002=$255, and no TXN-003
    And system transactions TXN-001=$100, TXN-002=$250, TXN-003=$80
    When reconciliation runs for period 2026-03-01 to 2026-03-14 with gateway RAZORPAY
    Then total transactions is 3
    And matched count is 1
    And auto-resolved count is 0
    And pending review count is 2
    And mismatch for TXN-002 has type AMOUNT_MISMATCH
    And mismatch for TXN-003 has type MISSING_IN_GATEWAY
