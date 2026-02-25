Feature: Testcase ID 2
Tests the shipping Workflow Service using a REST client. Shipping service exists and is under test.
It helps to create a shipping and manages the state of the shipping as documented in states xml

Scenario: Create a new shipping
Given that "flowName" equals "shipping-flow"
And that "initialState" equals "AWAITING_PICKUP"
When I POST a REST request to URL "/shipping" with payload
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

Scenario: Retrieve the shipping that just got created
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the courierAssigned event to the shipping with comments
Given that "comment" equals "Comment for courierAssigned"
And that "event" equals "courierAssigned"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the inTransit event to the shipping with comments
Given that "comment" equals "Comment for inTransit"
And that "event" equals "inTransit"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the outForDelivery event to the shipping with comments
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
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the markDelivered event to the shipping with comments
Given that "comment" equals "Comment for markDelivered"
And that "event" equals "markDelivered"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the returnRequested event to the shipping with comments
Given that "comment" equals "Comment for returnRequested"
And that "event" equals "returnRequested"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_REQUESTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
