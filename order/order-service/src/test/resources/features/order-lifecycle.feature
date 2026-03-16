Feature: Order Lifecycle — tests the full order state machine through all paths.
  Covers the happy path (CREATED -> PAID -> PROCESSING -> SHIPPED -> DELIVERED -> COMPLETED),
  cancellation from CREATED state, payment failure, and refund flow.

# =============================================================================
# HAPPY PATH: Create -> PaymentSucceeded -> StartProcessing -> MarkShipped
#             -> MarkDelivered -> ConfirmDelivery -> COMPLETED
# =============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new order in CREATED state
Given that "flowName" equals "order-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Test Order",
    "customerId": "cust-001",
    "shippingAddressId": "addr-001",
    "billingAddressId": "addr-002",
    "currency": "INR",
    "totalAmount": 3800,
    "items": [
        {
            "productId": "prod-001",
            "productName": "Handmade Silk Scarf",
            "quantity": 2,
            "unitPrice": 1500
        },
        {
            "productId": "prod-002",
            "productName": "Cotton Tote Bag",
            "quantity": 1,
            "unitPrice": 800
        }
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.description" is "Test Order"

Scenario: Retrieve the order that was just created
When I GET a REST request to URL "/order/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Payment succeeded (CREATED -> PAID)
Given that "comment" equals "Payment received via Stripe"
And that "event" equals "paymentSucceeded"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "paymentId": "pay_stripe_abc123",
    "transactionId": "txn_abc123"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PAID"

Scenario: Start processing the order (PAID -> PROCESSING)
Given that "comment" equals "Warehouse started picking items"
And that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "warehouseId": "WH-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Mark shipped (PROCESSING -> SHIPPED)
Given that "comment" equals "Courier picked up the package"
And that "event" equals "markShipped"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "trackingNumber": "TRACK-2026-00123",
    "carrier": "BlueDart"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Mark delivered (SHIPPED -> DELIVERED)
Given that "comment" equals "Package delivered to customer"
And that "event" equals "markDelivered"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "deliveryConfirmation": "Left at door"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Customer confirms delivery (DELIVERED -> COMPLETED)
Given that "comment" equals "Customer confirmed receipt"
And that "event" equals "confirmDelivery"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Verify completed order is retrievable
When I GET a REST request to URL "/order/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

# =============================================================================
# CANCELLATION FLOW: Create -> requestCancellation -> CHECK_CANCELLATION_WINDOW
#                    -> CANCEL_REQUESTED -> confirmCancellation -> CANCELLED
# =============================================================================

Scenario: Create an order for cancellation test
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Order to cancel",
    "customerId": "cust-cancel",
    "shippingAddressId": "addr-003",
    "currency": "INR",
    "totalAmount": 600,
    "items": [
        {
            "productId": "prod-003",
            "productName": "Bamboo Basket",
            "quantity": 1,
            "unitPrice": 600
        }
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "cancelId"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Request cancellation (CREATED -> CANCEL_REQUESTED via auto-state)
Given that "comment" equals "Customer changed their mind"
And that "event" equals "requestCancellation"
When I PATCH a REST request to URL "/order/${cancelId}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Changed mind"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${cancelId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCEL_REQUESTED"

Scenario: Admin confirms cancellation (CANCEL_REQUESTED -> CANCELLED)
Given that "event" equals "confirmCancellation"
When I PATCH a REST request to URL "/order/${cancelId}/${event}" with payload
"""json
{
    "comment": "Approved by admin",
    "adminNote": "Customer request valid"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${cancelId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCELLED"

# =============================================================================
# PAYMENT FAILURE FLOW: Create -> paymentFailed -> PAYMENT_FAILED
# =============================================================================

Scenario: Create an order for payment failure test
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Order with payment failure",
    "customerId": "cust-fail",
    "shippingAddressId": "addr-004",
    "currency": "INR",
    "totalAmount": 350,
    "items": [
        {
            "productId": "prod-004",
            "productName": "Clay Pot",
            "quantity": 1,
            "unitPrice": 350
        }
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failId"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Payment fails (CREATED -> PAYMENT_FAILED)
Given that "event" equals "paymentFailed"
When I PATCH a REST request to URL "/order/${failId}/${event}" with payload
"""json
{
    "comment": "Card declined by bank",
    "errorCode": "DECLINED",
    "errorDetails": "Insufficient funds"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_FAILED"

# =============================================================================
# REFUND FLOW: Delivered -> requestRefund -> REFUND_REQUESTED -> completeRefund -> REFUNDED
# =============================================================================

Scenario: Create an order for refund flow
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Order for refund test",
    "customerId": "cust-refund",
    "shippingAddressId": "addr-005",
    "currency": "INR",
    "totalAmount": 1200,
    "items": [
        {
            "productId": "prod-005",
            "productName": "Ceramic Vase",
            "quantity": 1,
            "unitPrice": 1200
        }
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "refundId"

Scenario: Move refund order through to DELIVERED
Given that "event" equals "paymentSucceeded"
When I PATCH a REST request to URL "/order/${refundId}/${event}" with payload
"""json
{
    "comment": "Payment done",
    "paymentId": "pay_ref_001"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PAID"

Scenario: Start processing refund order
Given that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${refundId}/${event}" with payload
"""json
{
    "comment": "Processing"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Ship refund order
Given that "event" equals "markShipped"
When I PATCH a REST request to URL "/order/${refundId}/${event}" with payload
"""json
{
    "comment": "Shipped",
    "trackingNumber": "TRACK-REF-001"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Deliver refund order
Given that "event" equals "markDelivered"
When I PATCH a REST request to URL "/order/${refundId}/${event}" with payload
"""json
{
    "comment": "Delivered"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Customer requests refund (DELIVERED -> REFUND_REQUESTED)
Given that "event" equals "requestRefund"
When I PATCH a REST request to URL "/order/${refundId}/${event}" with payload
"""json
{
    "comment": "Item arrived damaged",
    "reason": "DAMAGED_IN_TRANSIT"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${refundId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUND_REQUESTED"

Scenario: Refund completed (REFUND_REQUESTED -> REFUNDED)
Given that "event" equals "completeRefund"
When I PATCH a REST request to URL "/order/${refundId}/${event}" with payload
"""json
{
    "comment": "Refund processed to original payment method",
    "refundId": "ref_001",
    "refundAmount": 1200
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${refundId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

# =============================================================================
# INVALID: Cancel after COMPLETED — should fail (no requestCancellation on COMPLETED)
# =============================================================================

Scenario: Attempt to cancel a COMPLETED order — should fail
Given that "event" equals "requestCancellation"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Trying to cancel a completed order",
    "reason": "Too late"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to order — should fail
When I PATCH a REST request to URL "/order/${id}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
