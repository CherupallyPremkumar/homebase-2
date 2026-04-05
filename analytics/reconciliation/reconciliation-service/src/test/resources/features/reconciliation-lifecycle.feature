Feature: Reconciliation Batch Lifecycle
  Tests the STM lifecycle of reconciliation batches through state transitions.

  Scenario: Create a reconciliation batch
    When I POST a REST request to URL "/reconciliation" with payload
    """
    {
      "batchDate": "2026-03-14",
      "periodStart": "2026-03-14T00:00:00",
      "periodEnd": "2026-03-14T23:59:59",
      "gatewayType": "RAZORPAY",
      "reconciliationMethod": "FULL",
      "tenant": "homebase"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"
    And the REST response key "mutatedEntity.gatewayType" is "RAZORPAY"
    And store "$.payload.mutatedEntity.id" from response to "batchId"

  Scenario: Start processing a reconciliation batch
    When I PATCH a REST request to URL "/reconciliation/${batchId}/startProcessing" with payload
    """
    {}
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"
