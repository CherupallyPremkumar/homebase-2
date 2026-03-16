Feature: Testcase ID 4 — Cancellation from PROCESSING state.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create, pay, start processing
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Cancel from PROCESSING",
    "customerId": "cust-test4",
    "totalAmount": 700,
    "currency": "INR"
}
"""
Then store "$.payload.mutatedEntity.id" from response to "id"

Scenario: Payment succeeded
Given that "event" equals "paymentSucceeded"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Paid",
    "paymentId": "pay_004"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PAID"

Scenario: Start processing
Given that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Processing"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Request cancellation from PROCESSING
Given that "event" equals "requestCancellation"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Cancel from processing",
    "reason": "Order placed by mistake"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCEL_REQUESTED"
