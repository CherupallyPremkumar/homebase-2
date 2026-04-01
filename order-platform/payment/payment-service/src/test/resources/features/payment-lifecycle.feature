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

Scenario: Complete full refund (REFUND_PROCESSING -> CHECK_REFUND_TYPE -> REFUNDED)
Given that "event" equals "completeRefund"
When I PATCH a REST request to URL "/payment/${failId}/${event}" with payload
"""json
{
    "comment": "Full refund confirmed by gateway",
    "gatewayTransactionId": "gw-refund-001",
    "gatewayResponse": "{\"status\":\"refunded\"}",
    "refundedAmount": 1500.00
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

# ===============================================================================
# COD PATH
# ===============================================================================

Scenario: Create COD payment
When I POST a REST request to URL "/payment" with payload
"""json
{
    "orderId": "ORD-COD-001",
    "customerId": "CUST-COD-001",
    "amount": 999.00,
    "currency": "INR",
    "paymentMethod": "COD"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "codId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Initiate COD (INITIATED -> COD_PENDING)
Given that "event" equals "initiateCod"
When I PATCH a REST request to URL "/payment/${codId}/${event}" with payload
"""json
{
    "comment": "Marked as Cash on Delivery"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "COD_PENDING"

Scenario: Collect COD (COD_PENDING -> SUCCEEDED)
Given that "event" equals "collectCod"
When I PATCH a REST request to URL "/payment/${codId}/${event}" with payload
"""json
{
    "comment": "Cash collected at door",
    "collectedAmount": 999.00,
    "collectedBy": "DEL-AGENT-42"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SUCCEEDED"

# ===============================================================================
# PARTIAL REFUND PATH
# ===============================================================================

Scenario: Create payment for partial refund testing
When I POST a REST request to URL "/payment" with payload
"""json
{
    "orderId": "ORD-PARTIAL-001",
    "customerId": "CUST-PARTIAL-001",
    "amount": 5000.00,
    "currency": "INR",
    "paymentMethod": "CARD"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "partialId"

Scenario: Process and succeed partial refund payment
Given that "event" equals "process"
When I PATCH a REST request to URL "/payment/${partialId}/${event}" with payload
"""json
{ "comment": "Submit to gateway" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Succeed partial refund payment
Given that "event" equals "succeed"
When I PATCH a REST request to URL "/payment/${partialId}/${event}" with payload
"""json
{
    "comment": "Gateway confirmed",
    "gatewayTransactionId": "gw-txn-partial",
    "gatewayResponse": "{\"status\":\"captured\"}"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SUCCEEDED"

Scenario: Initiate partial refund (SUCCEEDED -> REFUND_INITIATED)
Given that "event" equals "initiateRefund"
When I PATCH a REST request to URL "/payment/${partialId}/${event}" with payload
"""json
{
    "comment": "Partial refund for one item",
    "refundReason": "Item damaged"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUND_INITIATED"

Scenario: Process partial refund
Given that "event" equals "processRefund"
When I PATCH a REST request to URL "/payment/${partialId}/${event}" with payload
"""json
{ "comment": "Submitting partial refund to gateway" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUND_PROCESSING"

Scenario: Complete partial refund (REFUND_PROCESSING -> CHECK_REFUND_TYPE -> PARTIALLY_REFUNDED)
Given that "event" equals "completeRefund"
When I PATCH a REST request to URL "/payment/${partialId}/${event}" with payload
"""json
{
    "comment": "Partial refund confirmed",
    "gatewayTransactionId": "gw-refund-partial-001",
    "refundedAmount": 1500.00
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PARTIALLY_REFUNDED"

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
Then the http status code is 400

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
Then the http status code is 400
