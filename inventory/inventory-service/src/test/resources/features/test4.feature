Feature: Testcase ID 4
Tests the inventory Workflow Service using a REST client. Inventory service exists and is under test.
It helps to create a inventory and manages the state of the inventory as documented in states xml

Scenario: Create a new inventory
Given that "flowName" equals "inventory-flow"
And that "initialState" equals "STOCK_PENDING"
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Description",
    "quantity": 100
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Description"

Scenario: Retrieve the inventory that just got created
When I GET a REST request to URL "/inventory/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the inspectStock event to the inventory with comments
Given that "comment" equals "Comment for inspectStock"
And that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the rejectStock event to the inventory with comments
Given that "comment" equals "Comment for rejectStock"
And that "event" equals "rejectStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_REJECTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the returnRejectedStock event to the inventory with comments
Given that "comment" equals "Comment for returnRejectedStock"
And that "event" equals "returnRejectedStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_TO_SUPPLIER"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the returnCompleted event to the inventory with comments
Given that "comment" equals "Comment for returnCompleted"
And that "event" equals "returnCompleted"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURNED_TO_SUPPLIER"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
