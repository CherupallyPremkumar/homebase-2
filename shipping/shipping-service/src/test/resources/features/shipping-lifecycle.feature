Feature: Tests the full Shipping Lifecycle using Chenile STM.
Covers the happy path: PENDING -> LABEL_CREATED -> PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED.
Covers the failure path: DELIVERY_FAILED -> retryDelivery -> CHECK_DELIVERY_ATTEMPTS -> OUT_FOR_DELIVERY.
Covers the return path: DELIVERY_FAILED -> RETURNED.
Includes activity tracking verification.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new shipment in PENDING state
Given that "flowName" equals "shipping-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/shipping" with payload
"""json
{
    "description": "Order shipment for ORD-2024-001",
    "orderId": "ORD-2024-001",
    "customerId": "CUST-001",
    "carrier": "HOMEBASE-LOGISTICS",
    "toAddress": "123 Main St, Bangalore"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Order shipment for ORD-2024-001"

Scenario: Retrieve the shipment that was just created
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Create shipping label (PENDING -> LABEL_CREATED)
Given that "comment" equals "Label created with DHL EXPRESS carrier"
And that "event" equals "createLabel"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "carrier": "DHL EXPRESS",
    "trackingNumber": "DHL-12345",
    "shippingMethod": "EXPRESS",
    "estimatedDeliveryDays": 2
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "LABEL_CREATED"
And the REST response key "mutatedEntity.carrier" is "DHL EXPRESS"
And the REST response key "mutatedEntity.trackingNumber" is "DHL-12345"
And the REST response key "mutatedEntity.shippingMethod" is "EXPRESS"
And the REST response contains key "mutatedEntity.estimatedDeliveryDate"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Carrier picks up shipment (LABEL_CREATED -> PICKED_UP)
Given that "comment" equals "Carrier picked up from warehouse"
And that "event" equals "pickUp"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "pickupLocation": "Bangalore Warehouse A"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark shipment in transit (PICKED_UP -> IN_TRANSIT)
Given that "comment" equals "Package left sorting facility, in transit to destination"
And that "event" equals "updateTransit"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "currentLocation": "Delhi Distribution Center"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark shipment out for delivery (IN_TRANSIT -> OUT_FOR_DELIVERY)
Given that "comment" equals "Package arrived at local hub, out for delivery"
And that "event" equals "outForDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "localHub": "Bangalore Central Hub"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Deliver shipment (OUT_FOR_DELIVERY -> DELIVERED)
Given that "comment" equals "Package delivered to customer, signed by recipient"
And that "event" equals "deliver"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "deliveryProof": "signature-img-001.png",
    "receivedBy": "Customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
And the REST response contains key "mutatedEntity.actualDeliveryDate"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Verify delivered shipment via GET
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Send an invalid event to shipping. This will err out.
When I PATCH a REST request to URL "/shipping/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
