Feature: Testcase ID 3
Tests the settlement dispute and adjustment flow.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a settlement for dispute test
Given that "flowName" equals "settlement-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/settlement" with payload
"""json
{
    "description": "Settlement for dispute test",
    "supplierId": "SUP-TEST3",
    "orderId": "ORD-TEST3",
    "orderAmount": {"amount": 8000, "currency": "INR"},
    "currency": "INR",
    "settlementPeriodStart": "2026-05-01",
    "settlementPeriodEnd": "2026-05-15"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Calculate step 1
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Calculate step 1" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATING"

Scenario: Calculate step 2
Given that "event" equals "calculate"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Calculate step 2" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CALCULATED"

Scenario: Approve
Given that "event" equals "approve"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{ "comment": "Approved" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Dispute (APPROVED -> DISPUTED)
Given that "event" equals "dispute"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "disputeReason": "Incorrect commission calculation",
    "comment": "Supplier disputes"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DISPUTED"

Scenario: Adjust dispute (DISPUTED -> APPROVED with adjustment)
Given that "event" equals "adjust"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "adjustmentAmount": 200,
    "adjustmentReason": "Corrected commission overage",
    "comment": "Adjustment applied"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
