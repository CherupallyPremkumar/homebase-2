Feature: Testcase ID 2
Tests the product Workflow Service using a REST client. Product service exists and is under test.
It helps to create a product and manages the state of the product as documented in states xml

Scenario: Create a new product
Given that "flowName" equals "product-flow"
And that "initialState" equals "DRAFT"
When I POST a REST request to URL "/product" with payload
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

Scenario: Retrieve the product that just got created
When I GET a REST request to URL "/product/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the submitForReview event to the product with comments
Given that "comment" equals "Comment for submitForReview"
And that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the approveProduct event to the product with comments
Given that "comment" equals "Comment for approveProduct"
And that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the disableProduct event to the product with comments
Given that "comment" equals "Comment for disableProduct"
And that "event" equals "disableProduct"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISABLED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the discontinueProduct event to the product with comments
Given that "comment" equals "Comment for discontinueProduct"
And that "event" equals "discontinueProduct"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
