Feature: Testcase ID 2
Tests the user Workflow Service using a REST client. User service exists and is under test.
It helps to create a user and manages the state of the user as documented in states xml

Scenario: Create a new user
Given that "flowName" equals "user-flow"
And that "initialState" equals "REGISTERED"
When I POST a REST request to URL "/user" with payload
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

Scenario: Retrieve the user that just got created
When I GET a REST request to URL "/user/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the sendVerificationEmail event to the user with comments
Given that "comment" equals "Comment for sendVerificationEmail"
And that "event" equals "sendVerificationEmail"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "EMAIL_VERIFICATION_PENDING"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the verificationExpired event to the user with comments
Given that "comment" equals "Comment for verificationExpired"
And that "event" equals "verificationExpired"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "VERIFICATION_EXPIRED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
