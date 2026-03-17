Feature: Checkout saga — step by step
  Tests the checkout workflow: create → process (runs 7-step saga) → payment callbacks.
  Each saga step uses hexagonal ports (stub adapters in SpringTestConfig).

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new checkout
Given that "flowName" equals "checkout-flow"
And that "initialState" equals "INITIATED"
When I POST a REST request to URL "/checkout" with payload
"""json
{
    "description": "Test checkout",
    "cartId": "cart-001",
    "shippingAddressId": "addr-001",
    "billingAddressId": "addr-001",
    "shippingMethod": "STANDARD",
    "paymentMethodId": "upi"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Retrieve the checkout
When I GET a REST request to URL "/checkout/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Process checkout — runs the 7-step saga (lockCart → lockPrice → reserveInventory → validateShipping → createOrder → commitPromo → initiatePayment)
Given that "event" equals "process"
When I PATCH a REST request to URL "/checkout/${id}/${event}" with payload
"""json
{
    "comment": "Running checkout saga"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "AWAITING_PAYMENT"

Scenario: Payment success — completes checkout
Given that "event" equals "paymentSuccess"
When I PATCH a REST request to URL "/checkout/${id}/${event}" with payload
"""json
{
    "comment": "Payment captured",
    "paymentId": "pay-razorpay-001",
    "transactionId": "txn-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
