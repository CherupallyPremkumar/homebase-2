Feature: Testcase ID 9
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

Scenario: Send the verifyEmail event to the user with comments
Given that "comment" equals "Comment for verifyEmail"
And that "event" equals "verifyEmail"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "EMAIL_VERIFIED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the skipProfile event to the user with comments
Given that "comment" equals "Comment for skipProfile"
And that "event" equals "skipProfile"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROFILE_INCOMPLETE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the completeProfile event to the user with comments
Given that "comment" equals "Comment for completeProfile"
And that "event" equals "completeProfile"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROFILE_COMPLETE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the upgradeToSupplier event to the user with comments
Given that "comment" equals "Comment for upgradeToSupplier"
And that "event" equals "upgradeToSupplier"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUPPLIER_APPROVAL_PENDING"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the approveSupplier event to the user with comments
Given that "comment" equals "Comment for approveSupplier"
And that "event" equals "approveSupplier"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUPPLIER_APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the suspendSupplier event to the user with comments
Given that "comment" equals "Comment for suspendSupplier"
And that "event" equals "suspendSupplier"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the banAccount event to the user with comments
Given that "comment" equals "Comment for banAccount"
And that "event" equals "banAccount"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "BANNED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
