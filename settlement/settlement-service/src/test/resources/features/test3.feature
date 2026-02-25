Feature: Testcase ID 3
Tests the settlement Workflow Service using a REST client. Settlement service exists and is under test.
It helps to create a settlement and manages the state of the settlement as documented in states xml

Scenario: Create a new settlement
Given that "flowName" equals "settlement-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/settlement" with payload
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

Scenario: Retrieve the settlement that just got created
When I GET a REST request to URL "/settlement/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the monthEnd event to the settlement with comments
Given that "comment" equals "Comment for monthEnd"
And that "event" equals "monthEnd"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the calculateSettlement event to the settlement with comments
Given that "comment" equals "Comment for calculateSettlement"
And that "event" equals "calculateSettlement"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SETTLED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
