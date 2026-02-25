Feature: Testcase ID 4
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


Scenario: Send the deleteProduct event to the product with comments
Given that "comment" equals "Comment for deleteProduct"
And that "event" equals "deleteProduct"
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
