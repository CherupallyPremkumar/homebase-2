Feature: Testcase ID 5 — Refund from COMPLETED state.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create an order
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Refund from completed",
    "customerId": "cust-test5",
    "totalAmount": 900,
    "currency": "INR"
}
"""
Then store "$.payload.mutatedEntity.id" from response to "id"

Scenario: Pay -> Process -> Ship -> Deliver -> Complete
Given that "event" equals "paymentSucceeded"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Paid", "paymentId": "pay_005" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PAID"

Scenario: Start processing
Given that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Processing" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Ship
Given that "event" equals "markShipped"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Shipped" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Deliver
Given that "event" equals "markDelivered"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Delivered" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Confirm delivery
Given that "event" equals "confirmDelivery"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Confirmed" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Request refund from COMPLETED
Given that "event" equals "requestRefund"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Item defective",
    "reason": "DEFECTIVE"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUND_REQUESTED"

Scenario: Complete refund
Given that "event" equals "completeRefund"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Refund done",
    "refundId": "ref_005"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"
