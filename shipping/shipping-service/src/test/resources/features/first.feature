Feature: Tests the shipping Workflow Service using a REST client.
Shipping service exists and is under test. Includes delivery failure and retry path.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new shipping
Given that "flowName" equals "shipping-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/shipping" with payload
"""json
{
    "description": "Description",
    "orderId": "ORD-FIRST-001",
    "carrier": "HOMEBASE-LOGISTICS"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Description"

Scenario: Retrieve the shipping that just got created
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Create label (PENDING -> LABEL_CREATED)
Given that "comment" equals "Comment for createLabel"
And that "event" equals "createLabel"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "carrier": "HOMEBASE-LOGISTICS"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "LABEL_CREATED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Pick up (LABEL_CREATED -> PICKED_UP)
Given that "comment" equals "Comment for pickUp"
And that "event" equals "pickUp"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Update transit (PICKED_UP -> IN_TRANSIT)
Given that "comment" equals "Comment for updateTransit"
And that "event" equals "updateTransit"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "currentLocation": "Sorting Facility"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Out for delivery (IN_TRANSIT -> OUT_FOR_DELIVERY)
Given that "comment" equals "Comment for outForDelivery"
And that "event" equals "outForDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Fail delivery (OUT_FOR_DELIVERY -> DELIVERY_FAILED)
Given that "comment" equals "Customer not home"
And that "event" equals "failDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "failureReason": "Customer not home"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERY_FAILED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Retry delivery (DELIVERY_FAILED -> CHECK_DELIVERY_ATTEMPTS -> OUT_FOR_DELIVERY)
Given that "comment" equals "Retrying delivery with updated instructions"
And that "event" equals "retryDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "newDeliveryInstructions": "Leave at front desk"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Deliver on second attempt (OUT_FOR_DELIVERY -> DELIVERED)
Given that "comment" equals "Delivered on second attempt"
And that "event" equals "deliver"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "receivedBy": "Front Desk"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send an invalid event to shipping. This will err out.
When I PATCH a REST request to URL "/shipping/${id}/invalid" with payload
"""json
{
    "comment": "invalid stuff"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
