Feature: Payment Lifecycle — tests the full payment state machine through all paths.
  Covers the happy path (INITIATED -> PROCESSING -> SUCCEEDED -> SETTLED),
  the failure flow (FAILED -> retry -> CHECK_RETRY -> RETRY/ABANDONED),
  the refund flow (SUCCEEDED -> REFUND_INITIATED -> REFUND_PROCESSING -> REFUNDED),
  and error handling.

# ===============================================================================
# HAPPY PATH: Initiate -> Process -> Succeed -> Settle
# ===============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new payment in INITIATED state
Given that "flowName" equals "payment-flow"
And that "initialState" equals "INITIATED"
When I POST a REST request to URL "/payment" with payload
"""json
{
    "orderId": "ORD-001",
    "customerId": "CUST-001",
    "amount": 2500.00,
    "currency": "INR",
    "paymentMethod": "UPI"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.orderId" is "ORD-001"
And the REST response key "mutatedEntity.paymentMethod" is "UPI"

Scenario: Retrieve the payment record
When I GET a REST request to URL "/payment/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Process payment (INITIATED -> PROCESSING)
Given that "event" equals "process"
When I PATCH a REST request to URL "/payment/${id}/${event}" with payload
"""json
{
    "comment": "Submitting to gateway",
    "gatewayTransactionId": "gw-txn-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Payment succeeds (PROCESSING -> SUCCEEDED)
Given that "event" equals "succeed"
When I PATCH a REST request to URL "/payment/${id}/${event}" with payload
"""json
{
    "comment": "Gateway confirmed payment",
    "gatewayTransactionId": "gw-txn-001",
    "gatewayResponse": "{\"status\":\"captured\"}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "SUCCEEDED"
And the REST response key "mutatedEntity.gatewayTransactionId" is "gw-txn-001"

Scenario: Settle payment (SUCCEEDED -> SETTLED)
Given that "event" equals "settle"
When I PATCH a REST request to URL "/payment/${id}/${event}" with payload
"""json
{
    "comment": "Auto-settlement after 7 days"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "SETTLED"

# ===============================================================================
# FAILURE + RETRY PATH
# ===============================================================================

Scenario: Create a second payment for failure testing
When I POST a REST request to URL "/payment" with payload
"""json
{
    "orderId": "ORD-002",
    "customerId": "CUST-002",
    "amount": 1500.00,
    "currency": "INR",
    "paymentMethod": "CARD"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Process second payment
Given that "event" equals "process"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Submitting to gateway"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Payment fails (PROCESSING -> FAILED)
Given that "event" equals "fail"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Gateway declined",
    "failureReason": "Insufficient funds",
    "gatewayResponse": "{\"error\":\"declined\"}"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"
And the REST response key "mutatedEntity.failureReason" is "Insufficient funds"

Scenario: Retry failed payment (FAILED -> CHECK_RETRY -> RETRY)
Given that "event" equals "retry"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Retry attempt 1"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "RETRY"

Scenario: Re-process after retry (RETRY -> PROCESSING)
Given that "event" equals "process"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Re-submitting to gateway"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Payment succeeds on retry (PROCESSING -> SUCCEEDED)
Given that "event" equals "succeed"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Gateway confirmed on retry",
    "gatewayTransactionId": "gw-txn-002",
    "gatewayResponse": "{\"status\":\"captured\"}"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SUCCEEDED"

# ===============================================================================
# REFUND PATH
# ===============================================================================

Scenario: Initiate refund on succeeded payment (SUCCEEDED -> REFUND_INITIATED)
Given that "event" equals "initiateRefund"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Customer requested refund",
    "refundReason": "Product not as described"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUND_INITIATED"

Scenario: Process refund (REFUND_INITIATED -> REFUND_PROCESSING)
Given that "event" equals "processRefund"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Submitting refund to gateway"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUND_PROCESSING"

Scenario: Complete refund (REFUND_PROCESSING -> REFUNDED)
Given that "event" equals "completeRefund"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Refund confirmed by gateway",
    "gatewayTransactionId": "gw-refund-001",
    "gatewayResponse": "{\"status\":\"refunded\"}"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

# ===============================================================================
# VALIDATION: Amount limits
# ===============================================================================

Scenario: Reject payment with amount below minimum
When I POST a REST request to URL "/payment" with payload
"""json
{
    "orderId": "ORD-003",
    "customerId": "CUST-003",
    "amount": 0.50,
    "currency": "INR",
    "paymentMethod": "UPI"
}
"""
Then the REST response status is 400

Scenario: Reject payment with unsupported method
When I POST a REST request to URL "/payment" with payload
"""json
{
    "orderId": "ORD-004",
    "customerId": "CUST-004",
    "amount": 100.00,
    "currency": "INR",
    "paymentMethod": "BITCOIN"
}
"""
Then the REST response status is 400
