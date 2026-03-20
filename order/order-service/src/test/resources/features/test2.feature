Feature: Testcase ID 2 — Happy path through the new order state machine.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new order
Given that "flowName" equals "order-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Description",
    "customerId": "cust-test2",
    "totalAmount": 1000,
    "currency": "INR",
    "items": [
        { "productId": "prod-t2", "productName": "Test Item", "quantity": 2, "unitPrice": 500 }
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Description"

Scenario: Retrieve the order
When I GET a REST request to URL "/order/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Payment succeeded (CREATED -> PAID)
Given that "event" equals "paymentSucceeded"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Payment received",
    "paymentId": "pay_002"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PAID"

Scenario: Start processing (PAID -> PROCESSING)
Given that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Processing started"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Mark shipped (PROCESSING -> SHIPPED)
Given that "event" equals "markShipped"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Shipped",
    "trackingNumber": "TRACK-002"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Mark delivered (SHIPPED -> DELIVERED)
Given that "event" equals "markDelivered"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Delivered"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Request refund (DELIVERED -> REFUND_REQUESTED)
Given that "event" equals "requestRefund"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Want refund",
    "reason": "DAMAGED_IN_TRANSIT"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUND_REQUESTED"

Scenario: Complete refund (REFUND_REQUESTED -> REFUNDED)
Given that "event" equals "completeRefund"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Refund processed",
    "refundId": "ref_002"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
