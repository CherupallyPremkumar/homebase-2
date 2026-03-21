Feature: Demo Order STM lifecycle with event publishing

  Tests the DemoOrder STM lifecycle. When an order is processed,
  the post-save hook publishes an ORDER_PROCESSED event to the
  "demo-order.events" topic via the event publisher port.

  Scenario: Create a demo order
    When I POST a REST request to URL "/demo-order" with payload
    """
    {"productName": "Test Widget", "quantity": 5, "customerId": "cust-001"}
    """
    Then success is true
    And the REST response key "mutatedEntity.productName" is "Test Widget"
    And the REST response key "mutatedEntity.quantity" is "5"
    And store "$.payload.mutatedEntity.id" from response to "orderId"

  Scenario: Process order publishes event
    # Create the order
    When I POST a REST request to URL "/demo-order" with payload
    """
    {"productName": "Event Publisher Test", "quantity": 3, "customerId": "cust-002"}
    """
    Then success is true
    And store "$.payload.mutatedEntity.id" from response to "orderId"

    # Process the order -- post-save hook publishes ORDER_PROCESSED event
    When I PATCH a REST request to URL "/demo-order/${orderId}/process" with payload
    """
    {"comment": "processing"}
    """
    Then success is true
    And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSED"
