Feature: Checkout cancellation and compensation flows

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create checkout for cancel test
When I POST a REST request to URL "/checkout" with payload
"""json
{
    "description": "Cancel test checkout",
    "cartId": "cart-cancel-001",
    "shippingAddressId": "addr-002",
    "billingAddressId": "addr-002",
    "shippingMethod": "EXPRESS",
    "paymentMethodId": "card"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "cancelId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Cancel checkout from INITIATED state
When I PATCH a REST request to URL "/checkout/${cancelId}/cancel" with payload
"""json
{
    "comment": "Customer changed mind"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCELLED"

Scenario: Compensate cancelled checkout
When I PATCH a REST request to URL "/checkout/${cancelId}/compensate" with payload
"""json
{
    "comment": "Releasing resources"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPENSATED"

Scenario: Create and process checkout, then payment fails
When I POST a REST request to URL "/checkout" with payload
"""json
{
    "description": "Payment fail test",
    "cartId": "cart-fail-001",
    "shippingAddressId": "addr-003",
    "billingAddressId": "addr-003",
    "shippingMethod": "STANDARD",
    "paymentMethodId": "upi"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failId"

Scenario: Process the checkout saga
When I PATCH a REST request to URL "/checkout/${failId}/process" with payload
"""json
{
    "comment": "Process saga"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "AWAITING_PAYMENT"

Scenario: Payment fails
When I PATCH a REST request to URL "/checkout/${failId}/paymentFailed" with payload
"""json
{
    "comment": "Razorpay declined",
    "failureReason": "Insufficient funds"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_FAILED"

Scenario: Compensate after payment failure
When I PATCH a REST request to URL "/checkout/${failId}/compensate" with payload
"""json
{
    "comment": "Rolling back all saga steps"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPENSATED"
