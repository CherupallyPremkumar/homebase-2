Feature: Promotion Lifecycle
Tests the Promo Lifecycle using Chenile STM.
Full flow: DRAFT -> SCHEDULED -> ACTIVE -> PAUSED -> ACTIVE -> EXPIRED -> ARCHIVED
Plus cancellation path: DRAFT -> CANCELLED -> ARCHIVED
The promo state machine is defined in promo-states.xml.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a promo code in DRAFT state
Given that "flowName" equals "promo-flow"
And that "initialState" equals "DRAFT"
When I POST a REST request to URL "/promo" with payload
"""json
{
    "name": "Summer Sale",
    "description": "Summer Sale 20% Off",
    "code": "SUMMER20",
    "discountType": "PERCENTAGE",
    "discountValue": 20,
    "usageLimit": 100,
    "usagePerCustomer": 3,
    "minOrderValue": 500,
    "maxDiscountAmount": 2000,
    "startDate": "2026-06-01T00:00:00",
    "endDate": "2026-08-31T23:59:59"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.name" is "Summer Sale"
And the REST response key "mutatedEntity.discountType" is "PERCENTAGE"

Scenario: Schedule the DRAFT promo
Given that "comment" equals "Scheduling promo for summer campaign"
And that "event" equals "schedule"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SCHEDULED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Activate the SCHEDULED promo
Given that "comment" equals "Activating promo for summer campaign launch"
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

Scenario: Pause the ACTIVE promo
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

Scenario: Resume the PAUSED promo
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

Scenario: Expire the ACTIVE promo
Given that "comment" equals "Promo campaign ended, expiring code"
And that "event" equals "expire"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "End date reached"
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

Scenario: Archive the EXPIRED promo
Given that "comment" equals "Archiving expired promo for records"
And that "event" equals "archive"
When I PATCH a REST request to URL "/promo/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Create a second promo to test cancellation path
Given that "flowName" equals "promo-flow"
When I POST a REST request to URL "/promo" with payload
"""json
{
    "name": "Flash Sale",
    "description": "Flash Sale 10% Off",
    "code": "FLASH10",
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "usageLimit": 50,
    "minOrderValue": 200,
    "startDate": "2026-07-01T00:00:00",
    "endDate": "2026-07-02T23:59:59"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Cancel the DRAFT promo directly
Given that "comment" equals "Cancelling flash sale promo"
And that "event" equals "cancel"
When I PATCH a REST request to URL "/promo/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Marketing decided against this campaign"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCELLED"

Scenario: Archive the cancelled promo
Given that "comment" equals "Archiving cancelled promo"
And that "event" equals "archive"
When I PATCH a REST request to URL "/promo/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"

Scenario: Create a flat discount promo
Given that "flowName" equals "promo-flow"
When I POST a REST request to URL "/promo" with payload
"""json
{
    "name": "Flat Rs500 Off",
    "description": "Flat 500 off on orders above 2000",
    "code": "FLAT500",
    "discountType": "FLAT",
    "discountValue": 500,
    "usageLimit": 200,
    "usagePerCustomer": 1,
    "minOrderValue": 2000,
    "startDate": "2026-06-15T00:00:00",
    "endDate": "2026-09-30T23:59:59"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id3"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
And the REST response key "mutatedEntity.discountType" is "FLAT"
And the REST response key "mutatedEntity.discountValue" is "500.0"

Scenario: Send an invalid event to promo. This will err out.
When I PATCH a REST request to URL "/promo/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
