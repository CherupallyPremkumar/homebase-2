Feature: Offer Lifecycle Management
Tests the full Offer Workflow Service using a REST client with security.
Manages offer states: DRAFT -> (submit) -> CHECK_AUTO_APPROVE -> PENDING_APPROVAL/APPROVED -> LIVE -> EXPIRED -> ARCHIVED
Also: REJECTED, SUSPENDED states as documented in offer-states.xml.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

# ================================================================
# Happy Path: DRAFT -> submit -> PENDING_APPROVAL -> approve -> APPROVED -> goLive -> LIVE -> expire -> EXPIRED -> archive -> ARCHIVED
# ================================================================

Scenario: Create a new offer in DRAFT state
Given that "flowName" equals "offer-flow"
And that "initialState" equals "DRAFT"
When I POST a REST request to URL "/offer" with payload
"""json
{
    "productId": "product-001",
    "supplierId": "seller-001",
    "offerType": "DEAL",
    "title": "Summer Sale on Ceramic Mugs",
    "description": "20% off handmade ceramic mugs",
    "originalPrice": 100.00,
    "offerPrice": 80.00,
    "discountPercent": 20.00,
    "maxQuantity": 500,
    "sellerRating": 3.5
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the offer that was just created
When I GET a REST request to URL "/offer/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Submit offer for approval (low seller rating -> PENDING_APPROVAL)
Given that "event" equals "submit"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "Submitting offer for review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_APPROVAL"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Approve the pending offer
Given that "event" equals "approve"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "Quality and pricing approved"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Go live with approved offer
Given that "event" equals "goLive"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "Taking offer live"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "LIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Expire the live offer
Given that "event" equals "expire"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "Offer end date has passed"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "EXPIRED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Archive the expired offer
Given that "event" equals "archive"
When I PATCH a REST request to URL "/offer/${id}/${event}" with payload
"""json
{
    "comment": "Archiving expired offer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

# ================================================================
# Rejection Flow: DRAFT -> submit -> PENDING_APPROVAL -> reject -> REJECTED -> resubmit -> PENDING_APPROVAL
# ================================================================

Scenario: Create a second offer for rejection flow
Given that "flowName" equals "offer-flow"
When I POST a REST request to URL "/offer" with payload
"""json
{
    "productId": "product-002",
    "supplierId": "seller-002",
    "offerType": "CLEARANCE",
    "title": "Clearance Bowl Sale",
    "description": "50% off wooden bowls",
    "originalPrice": 200.00,
    "offerPrice": 100.00,
    "discountPercent": 50.00,
    "maxQuantity": 100,
    "sellerRating": 3.0
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Submit second offer
When I PATCH a REST request to URL "/offer/${id2}/submit" with payload
"""json
{
    "comment": "Submitting for review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_APPROVAL"

Scenario: Reject offer with reason
When I PATCH a REST request to URL "/offer/${id2}/reject" with payload
"""json
{
    "comment": "Quality does not meet standards",
    "reason": "Rough finish, inconsistent glazing"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

Scenario: Resubmit rejected offer
When I PATCH a REST request to URL "/offer/${id2}/resubmit" with payload
"""json
{
    "comment": "Quality improved, resubmitting"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_APPROVAL"

# ================================================================
# Suspend Flow: LIVE -> suspend -> SUSPENDED -> resume -> LIVE
# ================================================================

Scenario: Create a third offer for suspend flow
Given that "flowName" equals "offer-flow"
When I POST a REST request to URL "/offer" with payload
"""json
{
    "productId": "product-003",
    "supplierId": "seller-003",
    "offerType": "LIGHTNING",
    "title": "Flash Sale Plates",
    "description": "Lightning deal on dinner plates",
    "originalPrice": 150.00,
    "offerPrice": 90.00,
    "discountPercent": 40.00,
    "maxQuantity": 50,
    "sellerRating": 3.8
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id3"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Submit third offer
When I PATCH a REST request to URL "/offer/${id3}/submit" with payload
"""json
{ "comment": "Submit" }
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_APPROVAL"

Scenario: Approve third offer
When I PATCH a REST request to URL "/offer/${id3}/approve" with payload
"""json
{ "comment": "Approved" }
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Go live third offer
When I PATCH a REST request to URL "/offer/${id3}/goLive" with payload
"""json
{ "comment": "Going live" }
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "LIVE"

Scenario: Suspend the live offer
When I PATCH a REST request to URL "/offer/${id3}/suspend" with payload
"""json
{
    "comment": "Suspending due to stock issues",
    "reason": "Out of stock reported by warehouse"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id3}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Resume the suspended offer
When I PATCH a REST request to URL "/offer/${id3}/resume" with payload
"""json
{
    "comment": "Stock replenished, resuming offer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id3}"
And the REST response key "mutatedEntity.currentState.stateId" is "LIVE"

# ================================================================
# Invalid Event Test
# ================================================================

Scenario: Send an invalid event to offer. This will err out.
When I PATCH a REST request to URL "/offer/${id3}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
