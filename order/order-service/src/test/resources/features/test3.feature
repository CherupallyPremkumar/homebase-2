Feature: Testcase ID 3
Tests the order Workflow Service using a REST client. Order service exists and is under test.
It helps to create a order and manages the state of the order as documented in states xml

Scenario: Create a new order
Given that "flowName" equals "order-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Description"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Description"

Scenario: Retrieve the order that just got created
When I GET a REST request to URL "/order/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the processPayment event to the order with comments
Given that "comment" equals "Comment for processPayment"
And that "event" equals "processPayment"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_CONFIRMED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the startProcessing event to the order with comments
Given that "comment" equals "Comment for startProcessing"
And that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the itemsPicked event to the order with comments
Given that "comment" equals "Comment for itemsPicked"
And that "event" equals "itemsPicked"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the courierPickup event to the order with comments
Given that "comment" equals "Comment for courierPickup"
And that "event" equals "courierPickup"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the returnRequested event to the order with comments
Given that "comment" equals "Comment for returnRequested"
And that "event" equals "returnRequested"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_INITIATED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the rejectReturn event to the order with comments
Given that "comment" equals "Comment for rejectReturn"
And that "event" equals "rejectReturn"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
