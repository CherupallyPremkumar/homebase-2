Feature: Testcase ID 3
Tests the supplier Workflow Service using a REST client. Supplier service exists and is under test.
It helps to create a supplier and manages the state of the supplier as documented in states xml

Scenario: Create a new supplier
Given that "flowName" equals "supplier-flow"
And that "initialState" equals "ACTIVE"
When I POST a REST request to URL "/supplier" with payload
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

Scenario: Retrieve the supplier that just got created
When I GET a REST request to URL "/supplier/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the pauseSupplier event to the supplier with comments
Given that "comment" equals "Comment for pauseSupplier"
And that "event" equals "pauseSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the blacklistSupplier event to the supplier with comments
Given that "comment" equals "Comment for blacklistSupplier"
And that "event" equals "blacklistSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "BLACKLISTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
