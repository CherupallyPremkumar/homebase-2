Feature: Shipping Management
  Tests express shipping with cancellation and activity tracking.
  Validates state transitions and security context.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

  Scenario: Create express shipment
    Given that "flowName" equals "shipping-flow"
    And that "initialState" equals "PENDING"
    When I POST a REST request to URL "/shipping" with payload
    """json
    {
        "description": "Express shipment for order ORD-EXPRESS-001",
        "orderId": "ORD-EXPRESS-001",
        "customerId": "CUST-EXPRESS",
        "carrier": "DHL EXPRESS",
        "toAddress": "42 Handmade Lane, Bangalore 560001"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "id"
    And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"
    And the REST response key "mutatedEntity.description" is "Express shipment for order ORD-EXPRESS-001"

  Scenario: Create label with EXPRESS method
    Given that "comment" equals "Express label created"
    And that "event" equals "createLabel"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "carrier": "DHL EXPRESS",
        "trackingNumber": "DHL-EXPRESS-001",
        "shippingMethod": "EXPRESS",
        "estimatedDeliveryDays": 2
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "LABEL_CREATED"
    And the REST response key "mutatedEntity.shippingMethod" is "EXPRESS"
    And the REST response key "mutatedEntity.trackingNumber" is "DHL-EXPRESS-001"
    And the REST response contains key "mutatedEntity.estimatedDeliveryDate"

  Scenario: Verify shipment details via GET
    When I GET a REST request to URL "/shipping/${id}"
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "LABEL_CREATED"
    And the REST response contains key "mutatedEntity.trackingNumber"
    And the REST response contains key "mutatedEntity.carrier"

  Scenario: Pick up, transit, out for delivery, then deliver
    Given that "event" equals "pickUp"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "Picked up from DHL hub"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"

  Scenario: Transit
    Given that "event" equals "updateTransit"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "In transit",
        "currentLocation": "Mumbai Airport"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"

  Scenario: Out for delivery
    Given that "event" equals "outForDelivery"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "Out for delivery",
        "localHub": "Bangalore HSR Hub"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"

  Scenario: Deliver
    Given that "event" equals "deliver"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "Delivered",
        "deliveryProof": "signature-express-001.png",
        "receivedBy": "Ramesh Kumar"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
    And the REST response contains key "mutatedEntity.actualDeliveryDate"
    And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

  Scenario: Send an invalid event to shipping - should fail
    When I PATCH a REST request to URL "/shipping/${id}/invalidEvent" with payload
    """json
    {
        "comment": "This should fail"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422
