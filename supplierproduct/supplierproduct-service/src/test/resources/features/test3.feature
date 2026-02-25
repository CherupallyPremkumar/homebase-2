Feature: Testcase ID 3
Tests the supplierproduct Workflow Service using a REST client. Supplierproduct service exists and is under test.
It helps to create a supplierproduct and manages the state of the supplierproduct as documented in states xml

Scenario: Create a new supplierproduct
Given that "flowName" equals "supplier-product-flow"
And that "initialState" equals "PENDING_DELIVERY"
When I POST a REST request to URL "/supplierproduct" with payload
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

Scenario: Retrieve the supplierproduct that just got created
When I GET a REST request to URL "/supplierproduct/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"


Scenario: Send the cancelProduct event to the supplierproduct with comments
Given that "comment" equals "Comment for cancelProduct"
And that "event" equals "cancelProduct"
When I PATCH a REST request to URL "/supplierproduct/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
