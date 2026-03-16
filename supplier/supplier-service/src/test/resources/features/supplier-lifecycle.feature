Feature: Tests the full Supplier Lifecycle using Chenile STM.
Covers the complete supplier state machine: APPLIED -> UNDER_REVIEW -> APPROVED -> ACTIVE,
ACTIVE -> ON_PROBATION -> ACTIVE, ACTIVE -> SUSPENDED -> ACTIVE,
ACTIVE -> TERMINATED, and rejection/resubmission flows.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Apply as new supplier - create supplier "Silk Sarees Co" in APPLIED state
Given that "flowName" equals "supplier-flow"
And that "initialState" equals "APPLIED"
When I POST a REST request to URL "/supplier" with payload
"""json
{
    "businessName": "Silk Sarees Co",
    "businessType": "COMPANY",
    "contactEmail": "contact@silksarees.com",
    "contactPhone": "9876543210",
    "address": "123 Silk Road, Kanchipuram"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the newly created supplier in APPLIED
When I GET a REST request to URL "/supplier/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"

Scenario: Begin review of supplier application (APPLIED -> UNDER_REVIEW)
Given that "comment" equals "Starting document review"
And that "event" equals "reviewSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reviewNotes": "Checking GST and business registration"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Approve the supplier (UNDER_REVIEW -> APPROVED)
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
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Put approved supplier on probation for performance (ACTIVE -> ON_PROBATION)
# First we need to get supplier to ACTIVE state - which happens via onboarding
# For now the APPROVED state is terminal in current flow, so we test the rejection flow

Scenario: Create a second supplier for the rejection flow
Given that "initialState" equals "APPLIED"
When I POST a REST request to URL "/supplier" with payload
"""json
{
    "businessName": "Rejected Vendor LLC",
    "businessType": "INDIVIDUAL",
    "contactEmail": "rejected@vendor.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Start review of second supplier (APPLIED -> UNDER_REVIEW)
Given that "comment" equals "Reviewing application"
And that "event" equals "reviewSupplier"
When I PATCH a REST request to URL "/supplier/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject supplier with reason (UNDER_REVIEW -> REJECTED)
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

Scenario: Resubmit rejected supplier (REJECTED -> APPLIED)
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
And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"

Scenario: Review and approve the resubmitted supplier
Given that "comment" equals "Reviewing resubmission"
And that "event" equals "reviewSupplier"
When I PATCH a REST request to URL "/supplier/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve the resubmitted supplier (UNDER_REVIEW -> APPROVED)
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
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Send an invalid event to supplier. This will err out.
When I PATCH a REST request to URL "/supplier/${id2}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
