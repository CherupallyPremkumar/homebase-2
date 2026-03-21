Feature: Demo Notification STM lifecycle

  Tests the DemoNotification STM lifecycle independently.
  Cross-module event tests require both modules on classpath (integration test).

  Scenario: Create a notification directly via REST
    When I POST a REST request to URL "/demo-notif" with payload
    """
    {"orderId": "manual-order-001", "message": "Manual notification", "channel": "SMS"}
    """
    Then success is true
    And the REST response key "mutatedEntity.orderId" is "manual-order-001"
    And the REST response key "mutatedEntity.message" is "Manual notification"
    And the REST response key "mutatedEntity.channel" is "SMS"
    And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"
    And store "$.payload.mutatedEntity.id" from response to "notifId"

  Scenario: Acknowledge a notification via STM event
    # Create notification
    When I POST a REST request to URL "/demo-notif" with payload
    """
    {"orderId": "ack-order-001", "message": "To be acknowledged", "channel": "PUSH"}
    """
    Then success is true
    And store "$.payload.mutatedEntity.id" from response to "notifId"

    # Acknowledge it
    When I PATCH a REST request to URL "/demo-notif/${notifId}/acknowledge" with payload
    """
    {"comment": "acknowledged"}
    """
    Then success is true
    And the REST response key "mutatedEntity.currentState.stateId" is "ACKNOWLEDGED"
