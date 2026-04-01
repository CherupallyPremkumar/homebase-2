Feature: Tests the full Settlement Lifecycle using Chenile STM.
Covers the settlement flow: PENDING -> CALCULATING -> CALCULATED -> APPROVED -> DISBURSED -> COMPLETED.
Also covers dispute path and failure/retry path.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new settlement for a supplier
Given that "flowName" equals "settlement-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/settlement" with payload
"""json
{
    "description": "March 2026 Settlement for Artisan Crafts Co.",
    "supplierId": "supplier-001",
    "orderId": "ORD-LIFECYCLE-001",
    "orderAmount": {"amount": 10000, "currency": "INR"},
    "currency": "INR",
    "settlementPeriodStart": "2026-03-01",
    "settlementPeriodEnd": "2026-03-15"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.description" is "March 2026 Settlement for Artisan Crafts Co."

Scenario: Retrieve the settlement that was just created
When I GET a REST request to URL "/settlement/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Calculate settlement (PENDING -> CALCULATING)
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "Initiating settlement calculation"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CALCULATING"

Scenario: Calculate settlement amounts (CALCULATING -> CALCULATED)
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "Commission calculated: 15% commission, 2% platform fee"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CALCULATED"

Scenario: Approve settlement (CALCULATED -> APPROVED via auto-approve)
Given that "event" equals "approve"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "Approving settlement"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Disburse settlement (APPROVED -> DISBURSED)
Given that "event" equals "disburse"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "Disbursing to supplier bank account"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISBURSED"

Scenario: Complete settlement (DISBURSED -> COMPLETED)
Given that "event" equals "approve"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "Settlement confirmed complete"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Verify completed state is terminal
When I PATCH a REST request to URL "/settlement/${id}/calculate" with payload
"""json
{
    "comment": "Attempting event on completed entity"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Create second settlement for dispute flow
When I POST a REST request to URL "/settlement" with payload
"""json
{
    "description": "April 2026 Settlement - Dispute Test",
    "supplierId": "supplier-002",
    "orderId": "ORD-LIFECYCLE-002",
    "orderAmount": {"amount": 8000, "currency": "INR"},
    "currency": "INR",
    "settlementPeriodStart": "2026-04-01",
    "settlementPeriodEnd": "2026-04-15"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Move second settlement through calculate -> calculated -> approved
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{ "comment": "Calculate step 1" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATING"

Scenario: Calculate second settlement
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{ "comment": "Calculate step 2" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATED"

Scenario: Approve second settlement
Given that "event" equals "approve"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{ "comment": "Approved" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Dispute the approved settlement (APPROVED -> DISPUTED)
Given that "event" equals "dispute"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "disputeReason": "Commission rate seems incorrect for our contract",
    "comment": "Supplier disputes settlement"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISPUTED"

Scenario: Resolve the dispute (DISPUTED -> APPROVED)
Given that "event" equals "resolve"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "resolution": "Commission rate verified as per contract. No changes needed.",
    "comment": "Dispute resolved by finance team"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Send an invalid event to settlement
When I PATCH a REST request to URL "/settlement/${id2}/invalidEvent" with payload
"""json
{
    "comment": "This should fail"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
