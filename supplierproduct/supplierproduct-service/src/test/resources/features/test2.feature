Feature: Testcase ID 2
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


Scenario: Send the productDelivered event to the supplierproduct with comments
Given that "comment" equals "Comment for productDelivered"
And that "event" equals "productDelivered"
When I PATCH a REST request to URL "/supplierproduct/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "QUALITY_CHECK"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the approveQuality event to the supplierproduct with comments
Given that "comment" equals "Comment for approveQuality"
And that "event" equals "approveQuality"
When I PATCH a REST request to URL "/supplierproduct/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the publishProduct event to the supplierproduct with comments
Given that "comment" equals "Comment for publishProduct"
And that "event" equals "publishProduct"
When I PATCH a REST request to URL "/supplierproduct/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "AT_WAREHOUSE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the listProduct event to the supplierproduct with comments
Given that "comment" equals "Comment for listProduct"
And that "event" equals "listProduct"
When I PATCH a REST request to URL "/supplierproduct/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the stockSoldOut event to the supplierproduct with comments
Given that "comment" equals "Comment for stockSoldOut"
And that "event" equals "stockSoldOut"
When I PATCH a REST request to URL "/supplierproduct/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_OF_STOCK"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the markDiscontinued event to the supplierproduct with comments
Given that "comment" equals "Comment for markDiscontinued"
And that "event" equals "markDiscontinued"
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
