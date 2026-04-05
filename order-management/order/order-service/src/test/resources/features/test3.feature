Feature: Testcase ID 3 — Cancellation from PAID state.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create and pay for order
Given that "flowName" equals "order-flow"
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Cancel from PAID",
    "customerId": "cust-test3",
    "totalAmount": 500,
    "currency": "INR"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"

Scenario: Payment succeeded
Given that "event" equals "paymentSucceeded"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Paid",
    "paymentId": "pay_003"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PAID"

Scenario: Request cancellation from PAID (PAID -> CANCEL_REQUESTED via auto-state)
Given that "event" equals "requestCancellation"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Cancel please",
    "reason": "Found better price"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCEL_REQUESTED"

Scenario: Confirm cancellation
Given that "event" equals "confirmCancellation"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Approved"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCELLED"
