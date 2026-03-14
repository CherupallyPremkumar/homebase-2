Feature: Shipping Management
  Tests the complete shipping lifecycle including forward shipping and return flow.
  Validates state transitions, tracking number generation, delivery confirmation,
  and return request handling.

  Scenario: Create shipment when order is paid
    Given that "flowName" equals "shipping-flow"
    And that "initialState" equals "AWAITING_PICKUP"
    When I POST a REST request to URL "/shipping" with payload
    """json
    {
        "description": "Shipment for order ORD-001",
        "orderId": "ORD-001",
        "shippingAddress": "42 Handmade Lane, Bangalore 560001"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "id"
    And the REST response key "mutatedEntity.currentState.stateId" is "AWAITING_PICKUP"
    And the REST response key "mutatedEntity.description" is "Shipment for order ORD-001"

  Scenario: Verify shipment has tracking number generated
    When I GET a REST request to URL "/shipping/${id}"
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "AWAITING_PICKUP"
    And the REST response contains key "mutatedEntity.trackingNumber"
    And the REST response contains key "mutatedEntity.carrier"

  Scenario: Courier picks up shipment (AWAITING_PICKUP -> PICKED_UP)
    Given that "comment" equals "Courier DELHIVERY assigned and picked up"
    And that "event" equals "courierAssigned"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "carrier": "DELHIVERY",
        "trackingNumber": "DEL-TRACK-001",
        "estimatedDeliveryDays": 3
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"
    And the REST response key "mutatedEntity.carrier" is "DELHIVERY"
    And the REST response key "mutatedEntity.trackingNumber" is "DEL-TRACK-001"
    And the REST response contains key "mutatedEntity.shippedAt"
    And the REST response contains key "mutatedEntity.estimatedDelivery"
    And the REST response contains key "mutatedEntity.trackingUrl"

  Scenario: Track shipment in transit (PICKED_UP -> IN_TRANSIT)
    Given that "comment" equals "Package left sorting facility, in transit"
    And that "event" equals "inTransit"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "currentLocation": "Mumbai Sorting Facility"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"

  Scenario: Shipment out for delivery (IN_TRANSIT -> OUT_FOR_DELIVERY)
    Given that "comment" equals "Package at local hub, out for delivery"
    And that "event" equals "outForDelivery"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "localHub": "Bangalore Koramangala Hub"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"

  Scenario: Deliver shipment (OUT_FOR_DELIVERY -> DELIVERED)
    Given that "comment" equals "Package delivered, signed by recipient"
    And that "event" equals "markDelivered"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "deliveryProof": "signature-img-001.png",
        "receivedBy": "Ramesh Kumar"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
    And the REST response contains key "mutatedEntity.deliveredAt"
    And the REST response key "mutatedEntity.deliveryProof" is "signature-img-001.png"

  Scenario: Verify delivered shipment via GET
    When I GET a REST request to URL "/shipping/${id}"
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
    And the REST response contains key "mutatedEntity.deliveredAt"

  Scenario: Return requested after delivery (DELIVERED -> RETURN_REQUESTED)
    Given that "comment" equals "Customer requested return - item damaged"
    And that "event" equals "returnRequested"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "returnReason": "Damaged on arrival"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_REQUESTED"
    And the REST response key "mutatedEntity.returnReason" is "Damaged on arrival"
    And the REST response contains key "mutatedEntity.returnTrackingNumber"

  Scenario: Return pickup (RETURN_REQUESTED -> IN_TRANSIT)
    Given that "comment" equals "Return pickup by courier"
    And that "event" equals "returnPickup"
    When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
    """json
    {
        "comment": "${comment}",
        "returnCarrier": "BLUEDART",
        "returnTrackingNumber": "BD-RTN-001"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${id}"
    And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
    And the REST response key "mutatedEntity.returnTrackingNumber" is "BD-RTN-001"

  Scenario: Send an invalid event to shipping - should fail
    When I PATCH a REST request to URL "/shipping/${id}/invalidEvent" with payload
    """json
    {
        "comment": "This should fail"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422
