Feature: Tests the user Workflow Service using a REST client.
  User service exists and is under test.
  It helps to create a user and manages the state of the user as documented in user-states.xml.
  States: REGISTERED -> EMAIL_VERIFIED -> ACTIVE -> SUSPENDED -> DEACTIVATED

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new user
Given that "flowName" equals "user-flow"
And that "initialState" equals "REGISTERED"
When I POST a REST request to URL "/user" with payload
"""json
{
    "description": "Description",
    "email": "first-test@homebase.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the user that just got created
When I GET a REST request to URL "/user/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Verify email (REGISTERED -> EMAIL_VERIFIED)
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

Scenario: Activate user (EMAIL_VERIFIED -> ACTIVE)
Given that "comment" equals "Comment for activate"
And that "event" equals "activate"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Deactivate the active user (ACTIVE -> DEACTIVATED)
Given that "comment" equals "Comment for deactivate"
And that "event" equals "deactivate"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DEACTIVATED"

Scenario: Send an invalid event to user. This will err out.
When I PATCH a REST request to URL "/user/${id}/invalid" with payload
"""json
{
    "comment": "invalid stuff"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
