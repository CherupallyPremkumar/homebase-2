Feature: Tests the full Supplier Lifecycle using Chenile STM.
Covers the complete supplier state machine: PENDING_REVIEW -> ACTIVE -> SUSPENDED -> ACTIVE,
ACTIVE -> INACTIVE -> ACTIVE, ACTIVE -> BLACKLISTED, and rejection/resubmission flows.

Scenario: Approve new supplier - create supplier "Silk Sarees Co" in PENDING_REVIEW state
Given that "flowName" equals "supplier-flow"
And that "initialState" equals "PENDING_REVIEW"
When I POST a REST request to URL "/supplier" with payload
"""json
{
    "name": "Silk Sarees Co",
    "description": "Premium silk sarees from Kanchipuram",
    "email": "contact@silksarees.com",
    "phone": "9876543210"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the newly created supplier in PENDING_REVIEW
When I GET a REST request to URL "/supplier/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_REVIEW"

Scenario: Approve the supplier (PENDING_REVIEW -> ACTIVE) - supplier should be in ACTIVE state
Given that "comment" equals "Supplier documents verified, approved"
And that "event" equals "approveSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Pause the active supplier - supplier self-pauses (ACTIVE -> INACTIVE)
Given that "comment" equals "Supplier requested temporary pause for inventory restocking"
And that "event" equals "pauseSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Reactivate the inactive supplier (INACTIVE -> ACTIVE)
Given that "comment" equals "Supplier ready to resume operations"
And that "event" equals "reactivateSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Suspend supplier for policy violation (ACTIVE -> SUSPENDED)
Given that "comment" equals "Fake products reported by multiple customers"
And that "event" equals "suspendSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Fake products reported by multiple customers"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Reactivate suspended supplier (SUSPENDED -> ACTIVE)
Given that "comment" equals "Policy violation resolved after investigation, reactivating"
And that "event" equals "reactivateSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Blacklist supplier permanently (ACTIVE -> BLACKLISTED) - all products should be disabled
Given that "comment" equals "Confirmed fraudulent activity, permanent ban"
And that "event" equals "blacklistSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Confirmed fraudulent activity, permanent ban"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "BLACKLISTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Verify blacklisted supplier has no valid transitions (terminal state)
When I PATCH a REST request to URL "/supplier/${id}/reactivateSupplier" with payload
"""json
{
    "comment": "Attempting to reactivate a blacklisted supplier"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Create a second supplier for the rejection flow
Given that "initialState" equals "PENDING_REVIEW"
When I POST a REST request to URL "/supplier" with payload
"""json
{
    "name": "Rejected Vendor LLC",
    "description": "Vendor with incomplete documentation"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Reject supplier with reason (PENDING_REVIEW -> REJECTED)
Given that "comment" equals "Incomplete documentation - missing GST certificate"
And that "event" equals "rejectSupplier"
When I PATCH a REST request to URL "/supplier/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Incomplete documentation - missing GST certificate"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

Scenario: Resubmit rejected supplier (REJECTED -> PENDING_REVIEW)
Given that "comment" equals "Documentation updated with GST certificate, resubmitting"
And that "event" equals "resubmitSupplier"
When I PATCH a REST request to URL "/supplier/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_REVIEW"

Scenario: Approve the resubmitted supplier (PENDING_REVIEW -> ACTIVE)
Given that "comment" equals "Documentation complete, approved on second review"
And that "event" equals "approveSupplier"
When I PATCH a REST request to URL "/supplier/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Create a third supplier for suspended-to-blacklist flow
Given that "initialState" equals "PENDING_REVIEW"
When I POST a REST request to URL "/supplier" with payload
"""json
{
    "name": "Shady Goods Inc",
    "description": "Supplier to be suspended then blacklisted"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id3"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Approve third supplier
Given that "comment" equals "Initial approval"
And that "event" equals "approveSupplier"
When I PATCH a REST request to URL "/supplier/${id3}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Suspend third supplier
Given that "comment" equals "Customer complaints"
And that "event" equals "suspendSupplier"
When I PATCH a REST request to URL "/supplier/${id3}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Customer complaints"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Blacklist the suspended supplier (SUSPENDED -> BLACKLISTED)
Given that "comment" equals "Investigation confirmed fraud"
And that "event" equals "blacklistSupplier"
When I PATCH a REST request to URL "/supplier/${id3}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Investigation confirmed fraud"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id3}"
And the REST response key "mutatedEntity.currentState.stateId" is "BLACKLISTED"

Scenario: Send an invalid event to supplier. This will err out.
When I PATCH a REST request to URL "/supplier/${id2}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
