Feature: Promotion Lifecycle
Tests the Promo Code Lifecycle using Chenile STM.
Manages promo code (coupon) states: DRAFT -> ACTIVE -> PAUSED/EXPIRED/CLOSED with reactivation paths.
The promo state machine is defined in promo-states.xml.

Scenario: Activate promo code from DRAFT
Given that "flowName" equals "promo-flow"
And that "initialState" equals "DRAFT"
When I POST a REST request to URL "/promo" with payload
"""json
{
    "description": "Summer Sale 20% Off",
    "code": "SUMMER20",
    "discountType": "PERCENTAGE",
    "discountValue": 20,
    "maxUsageCount": 100,
    "minOrderAmount": 500
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Summer Sale 20% Off"

Scenario: Activate the DRAFT promo code
Given that "comment" equals "Activating promo for summer campaign"
And that "event" equals "activate"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Pause and resume promo
Given that "comment" equals "Temporarily pausing promo for inventory check"
And that "event" equals "pause"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PAUSED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Resume paused promo
Given that "comment" equals "Inventory restocked, resuming promo"
And that "event" equals "resume"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Auto-expire past expiry date
Given that "comment" equals "Promo campaign ended, expiring code"
And that "event" equals "expire"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Expiry date reached"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "EXPIRED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Verify promo is in EXPIRED state
When I GET a REST request to URL "/promo/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "EXPIRED"

Scenario: Reactivate the expired promo code
Given that "comment" equals "Extended campaign, reactivating promo"
And that "event" equals "reactivate"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Campaign extended by 2 weeks"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Close the reactivated promo code permanently
Given that "comment" equals "Campaign fully over, closing promo"
And that "event" equals "close"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Campaign concluded"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CLOSED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Create a second promo to test max usage rejection
Given that "flowName" equals "promo-flow"
When I POST a REST request to URL "/promo" with payload
"""json
{
    "description": "Flash Sale 10% Off",
    "code": "FLASH10",
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "maxUsageCount": 1,
    "currentUsage": 1,
    "minOrderAmount": 100
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Send an invalid event to promo. This will err out.
When I PATCH a REST request to URL "/promo/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
