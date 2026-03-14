Feature: Supplier Settlement
  Tests the full settlement lifecycle including calculation, payout confirmation,
  rejection, and retry flows using the Chenile STM-based REST API.

  Scenario: Monthly settlement creation for supplier
    Given that "flowName" equals "settlement-flow"
    And that "initialState" equals "PENDING"
    When I POST a REST request to URL "/settlement" with payload
    """json
    {
        "description": "January 2026 Settlement for SUP-001",
        "supplierId": "SUP-001",
        "periodMonth": 1,
        "periodYear": 2026
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "id"
    And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"
    And the REST response key "mutatedEntity.supplierId" is "SUP-001"
    And the REST response key "mutatedEntity.periodMonth" is "1"
    And the REST response key "mutatedEntity.periodYear" is "2026"

  Scenario: Trigger month-end settlement (PENDING -> PROCESSING)
    Given that "event" equals "monthEnd"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    {
        "comment": "Month end reached, initiating settlement"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

  Scenario: Calculate settlement amounts (PROCESSING -> READY_FOR_PAYMENT)
    Given that "event" equals "calculateSettlement"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    {
        "comment": "Calculating settlement with 10% commission"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "READY_FOR_PAYMENT"
    And the REST response key "mutatedEntity.totalSalesAmount.amount" is "15000.00"
    And the REST response key "mutatedEntity.commissionAmount.amount" is "1500.00"
    And the REST response key "mutatedEntity.netPayoutAmount.amount" is "13500.00"

  Scenario: Confirm payout above minimum threshold (READY_FOR_PAYMENT -> SETTLED)
    Given that "event" equals "confirmPayout"
    When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
    """json
    {
        "paymentReference": "UPI-REF-123",
        "notes": "Payout confirmed via UPI"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "SETTLED"

  Scenario: Verify settled state is terminal
    When I PATCH a REST request to URL "/settlement/${id}/monthEnd" with payload
    """json
    {
        "comment": "Attempting event on settled entity"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  Scenario: Create settlement for rejection flow
    When I POST a REST request to URL "/settlement" with payload
    """json
    {
        "description": "Feb 2026 Settlement for rejection test",
        "supplierId": "SUP-002",
        "periodMonth": 2,
        "periodYear": 2026
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "id2"
    And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

  Scenario: Move to PROCESSING for rejection test
    Given that "event" equals "monthEnd"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    {
        "comment": "Month end processing"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

  Scenario: Calculate settlement for rejection test
    Given that "event" equals "calculateSettlement"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    {
        "comment": "Calculating amounts"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "READY_FOR_PAYMENT"

  Scenario: Reject settlement (READY_FOR_PAYMENT -> FAILED)
    Given that "event" equals "rejectSettlement"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    {
        "comment": "Settlement rejected due to discrepancy"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id2}"
    And the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

  Scenario: Retry failed settlement (FAILED -> PROCESSING)
    Given that "event" equals "retrySettlement"
    When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
    """json
    {
        "comment": "Admin triggered retry after fixing discrepancy"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id2}"
    And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

  Scenario: Send an invalid event to settlement
    When I PATCH a REST request to URL "/settlement/${id2}/invalidEvent" with payload
    """json
    {
        "comment": "This should fail"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422
