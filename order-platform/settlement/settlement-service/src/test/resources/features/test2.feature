Feature: Testcase ID 2
Tests the settlement happy path to COMPLETED state.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new settlement
Given that "flowName" equals "settlement-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/settlement" with payload
"""json
{
    "description": "Test2 Settlement",
    "supplierId": "SUP-TEST2",
    "orderId": "ORD-TEST2",
    "orderAmount": {"amount": 5000, "currency": "INR"},
    "currency": "INR",
    "settlementPeriodStart": "2026-04-01",
    "settlementPeriodEnd": "2026-04-15"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Retrieve the settlement
When I GET a REST request to URL "/settlement/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Calculate step 1 (PENDING -> CALCULATING)
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Calculate step 1" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATING"

Scenario: Calculate step 2 (CALCULATING -> CALCULATED)
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Calculate step 2" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATED"

Scenario: Approve (CALCULATED -> APPROVED)
Given that "event" equals "approve"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Approved" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Disburse (APPROVED -> DISBURSED)
Given that "event" equals "disburse"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Disbursed" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DISBURSED"

Scenario: Complete (DISBURSED -> COMPLETED)
Given that "event" equals "approve"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Completed" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
