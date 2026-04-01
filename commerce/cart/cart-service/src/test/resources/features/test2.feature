Feature: Testcase ID 2
Tests the cart Workflow Service using a REST client. Cart service exists and is under test.
It helps to create a cart and manages the state of the cart as documented in states xml

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new cart
Given that "flowName" equals "cart-flow"
And that "initialState" equals "ACTIVE"
When I POST a REST request to URL "/cart" with payload
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

Scenario: Retrieve the cart that just got created
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the addItem event to the cart with comments
Given that "comment" equals "Comment for addItem"
And that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "productId": "prod-test2-001",
    "variantId": "var-test2-001-default",
    "productName": "Test Product",
    "quantity": 1,
    "unitPrice": 100
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the abandon event to the cart with comments
Given that "comment" equals "Comment for abandon"
And that "event" equals "abandon"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ABANDONED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
