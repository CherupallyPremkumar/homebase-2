Feature: Testcase ID 2
Tests the shipping failure-to-return flow and cancellation flow.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new shipping for return test
Given that "flowName" equals "shipping-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/shipping" with payload
"""json
{
    "description": "Description",
    "orderId": "ORD-TEST2-001",
    "carrier": "HOMEBASE-LOGISTICS"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Create label
Given that "comment" equals "Comment for createLabel"
And that "event" equals "createLabel"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "carrier": "DELHIVERY",
    "trackingNumber": "DEL-TEST2-001",
    "shippingMethod": "STANDARD",
    "estimatedDeliveryDays": 5
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "LABEL_CREATED"
And the REST response key "mutatedEntity.carrier" is "DELHIVERY"
And the REST response key "mutatedEntity.trackingNumber" is "DEL-TEST2-001"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Pick up
Given that "comment" equals "Comment for pickUp"
And that "event" equals "pickUp"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"

Scenario: Transit
Given that "comment" equals "Comment for updateTransit"
And that "event" equals "updateTransit"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "currentLocation": "Mumbai Sorting Hub"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"

Scenario: Out for delivery
Given that "comment" equals "Comment for outForDelivery"
And that "event" equals "outForDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "localHub": "Bangalore Koramangala Hub"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"

Scenario: Fail delivery
Given that "comment" equals "Wrong address"
And that "event" equals "failDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "failureReason": "Wrong address"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERY_FAILED"

Scenario: Return shipment directly (DELIVERY_FAILED -> RETURNED)
Given that "comment" equals "Returning to warehouse"
And that "event" equals "returnShipment"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "returnReason": "Customer unreachable"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURNED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
