Feature: Offer Management
Tests the Offer Workflow Service using a REST client.
Manages offer states: PENDING_REVIEW -> ACTIVE/REJECTED/INACTIVE/RETURNED as documented in offer-states.xml.

Scenario: Create a new supplier offer in PENDING_REVIEW state
Given that "flowName" equals "offer-flow"
And that "initialState" equals "PENDING_REVIEW"
When I POST a REST request to URL "/offer" with payload
"""json
{
    "description": "Handmade ceramic mug",
    "variantId": "variant-001",
    "supplierId": "supplier-maker-1"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the offer that just got created
When I GET a REST request to URL "/offer/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Approve supplier offer
Given that "comment" equals "Quality and price approved by Hub Manager"
And that "event" equals "approve"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Deactivate and reactivate offer
Given that "comment" equals "Temporarily deactivating for quality review"
And that "event" equals "deactivate"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Reactivate the deactivated offer
Given that "comment" equals "Quality review passed, reactivating"
And that "event" equals "activate"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Supplier withdraws offer
Given that "comment" equals "Supplier can no longer fulfill orders for this product"
And that "event" equals "returnOffer"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURNED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Create a second offer to test rejection flow
Given that "flowName" equals "offer-flow"
When I POST a REST request to URL "/offer" with payload
"""json
{
    "description": "Handmade wooden bowl",
    "variantId": "variant-002",
    "supplierId": "supplier-maker-2"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_REVIEW"

Scenario: Reject offer with reason
Given that "comment" equals "Quality does not meet platform standards - rough finish on rim"
And that "event" equals "reject"
When I PATCH a REST request to URL "/offer/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Resubmit rejected offer for review
Given that "comment" equals "Quality improved, resubmitting for review"
And that "event" equals "resubmit"
When I PATCH a REST request to URL "/offer/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_REVIEW"

Scenario: Send an invalid event to offer. This will err out.
When I PATCH a REST request to URL "/offer/${id2}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
