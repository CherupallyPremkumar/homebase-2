Feature: Supplier Settlement
  Tests the full settlement lifecycle including calculation, disbursement,
  dispute, and resolution flows using the Chenile STM-based REST API.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

  Scenario: Monthly settlement creation for supplier
    Given that "flowName" equals "settlement-flow"
    And that "initialState" equals "PENDING"
    When I POST a REST request to URL "/settlement" with payload
    """json
    {
        "description": "January 2026 Settlement for SUP-001",
        "supplierId": "SUP-001",
        "orderId": "ORD-SUP-001",
        "orderAmount": {"amount": 15000, "currency": "INR"},
        "currency": "INR",
        "settlementPeriodStart": "2026-01-01",
        "settlementPeriodEnd": "2026-01-15"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "id"
    And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"
    And the REST response key "mutatedEntity.supplierId" is "SUP-001"

  Scenario: Calculate settlement step 1 (PENDING -> CALCULATING)
    Given that "event" equals "calculate"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    { "comment": "Initiating settlement calculation" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATING"

  Scenario: Calculate settlement step 2 (CALCULATING -> CALCULATED)
    Given that "event" equals "calculate"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    { "comment": "Calculating with 15% commission, 2% platform fee" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATED"

  Scenario: Approve settlement (CALCULATED -> APPROVED)
    Given that "event" equals "approve"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    { "comment": "Settlement approved" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  Scenario: Disburse to supplier (APPROVED -> DISBURSED)
    Given that "event" equals "disburse"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    { "comment": "Disbursing to supplier bank" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "DISBURSED"

  Scenario: Complete settlement (DISBURSED -> COMPLETED)
    Given that "event" equals "approve"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    { "comment": "Settlement completed" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

  Scenario: Verify completed state is terminal
    When I PATCH a REST request to URL "/settlement/${id}/calculate" with payload
    """json
    { "comment": "Attempting event on completed entity" }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  Scenario: Create settlement for dispute flow
    When I POST a REST request to URL "/settlement" with payload
    """json
    {
        "description": "Feb 2026 Settlement for dispute test",
        "supplierId": "SUP-002",
        "orderId": "ORD-SUP-002",
        "orderAmount": {"amount": 6000, "currency": "INR"},
        "currency": "INR",
        "settlementPeriodStart": "2026-02-01",
        "settlementPeriodEnd": "2026-02-15"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "id2"
    And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

  Scenario: Calculate second settlement step 1
    Given that "event" equals "calculate"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    { "comment": "Calculate step 1" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATING"

  Scenario: Calculate second settlement step 2
    Given that "event" equals "calculate"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    { "comment": "Calculate step 2" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATED"

  Scenario: Approve second settlement
    Given that "event" equals "approve"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    { "comment": "Approved" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  Scenario: Dispute settlement (APPROVED -> DISPUTED)
    Given that "event" equals "dispute"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    {
        "disputeReason": "Settlement amount does not match contract terms",
        "comment": "Supplier disputes settlement"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "DISPUTED"

  Scenario: Resolve dispute (DISPUTED -> APPROVED)
    Given that "event" equals "resolve"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    {
        "resolution": "Verified contract. Settlement amounts are correct.",
        "comment": "Dispute resolved"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  Scenario: Send an invalid event to settlement
    When I PATCH a REST request to URL "/settlement/${id2}/invalidEvent" with payload
    """json
    { "comment": "This should fail" }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422
