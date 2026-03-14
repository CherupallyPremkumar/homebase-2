Feature: Order Lifecycle — tests the full order state machine through all paths.
  Covers the happy path (CREATED -> PAYMENT_CONFIRMED -> PROCESSING -> PICKED -> SHIPPED -> DELIVERED -> COMPLETED),
  cancellation from CREATED state, and invalid cancellation after delivery.

# ═══════════════════════════════════════════════════════════════════════════════
# HAPPY PATH: Create -> Pay -> Process -> Pick -> Ship -> Deliver -> Complete
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a new order in CREATED state
Given that "flowName" equals "order-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Test Order",
    "customerName": "Prem Kumar",
    "shippingAddress": "123 Main St, Chennai",
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

Scenario: Process payment (CREATED -> PAYMENT_CONFIRMED)
Given that "comment" equals "Payment received via Stripe"
And that "event" equals "processPayment"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "paymentId": "pay_stripe_abc123",
    "paymentMethod": "CARD",
    "amountPaid": 3800
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_CONFIRMED"

Scenario: Start processing the order (PAYMENT_CONFIRMED -> PROCESSING)
Given that "comment" equals "Warehouse started picking items"
And that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Items picked (PROCESSING -> PICKED)
Given that "comment" equals "All items picked and packed"
And that "event" equals "itemsPicked"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED"

Scenario: Courier pickup — ship the order (PICKED -> SHIPPED)
Given that "comment" equals "Courier picked up the package"
And that "event" equals "courierPickup"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "trackingNumber": "TRACK-2024-00123",
    "courierPartner": "BlueDart"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Deliver the order (SHIPPED -> DELIVERED)
Given that "comment" equals "Package delivered to customer"
And that "event" equals "deliverOrder"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "deliveredAt": "2026-03-14T10:30:00Z"
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

# ═══════════════════════════════════════════════════════════════════════════════
# CANCELLATION FLOW: Create -> Cancel (from CREATED state)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create an order for cancellation test
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Order to cancel",
    "customerName": "Test User",
    "shippingAddress": "456 Cancel St",
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

Scenario: Cancel the order from CREATED state (CREATED -> CANCELLED)
Given that "comment" equals "Customer changed their mind"
And that "event" equals "cancelOrder"
When I PATCH a REST request to URL "/order/${cancelId}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "cancellationReason": "Changed mind"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${cancelId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CANCELLED"

# ═══════════════════════════════════════════════════════════════════════════════
# PAYMENT FAILURE FLOW: Create -> paymentFailed -> FAILED
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create an order for payment failure test
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Order with payment failure",
    "customerName": "Test User",
    "shippingAddress": "789 Fail Ave",
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

Scenario: Payment fails (CREATED -> FAILED)
Given that "event" equals "paymentFailed"
When I PATCH a REST request to URL "/order/${failId}/${event}" with payload
"""json
{
    "comment": "Card declined by bank"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failId}"
And the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

# ═══════════════════════════════════════════════════════════════════════════════
# RETURN FLOW: Delivered -> initiateReturn -> approveReturn -> refundComplete
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create an order for return flow
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Order for return test",
    "customerName": "Return Customer",
    "shippingAddress": "Return St",
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
And store "$.payload.mutatedEntity.id" from response to "returnId"

Scenario: Move return order through to DELIVERED
Given that "event" equals "processPayment"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Payment done",
    "paymentId": "pay_ret_001",
    "amountPaid": 1200
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_CONFIRMED"

Scenario: Start processing return order
Given that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Processing"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"

Scenario: Pick items for return order
Given that "event" equals "itemsPicked"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Picked"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PICKED"

Scenario: Ship return order
Given that "event" equals "courierPickup"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Shipped",
    "trackingNumber": "TRACK-RET-001"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Deliver return order
Given that "event" equals "deliverOrder"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Delivered"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Customer initiates return (DELIVERED -> RETURN_INITIATED)
Given that "event" equals "initiateReturn"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Item arrived damaged",
    "returnReason": "DAMAGED_IN_TRANSIT"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${returnId}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_INITIATED"

Scenario: Admin approves return (RETURN_INITIATED -> REFUND_INITIATED)
Given that "event" equals "approveReturn"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Return approved, initiating refund"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${returnId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUND_INITIATED"

Scenario: Refund completed (REFUND_INITIATED -> REFUNDED)
Given that "event" equals "refundComplete"
When I PATCH a REST request to URL "/order/${returnId}/${event}" with payload
"""json
{
    "comment": "Refund processed to original payment method",
    "refundId": "ref_001",
    "refundAmount": 1200
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${returnId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

# ═══════════════════════════════════════════════════════════════════════════════
# INVALID: Cancel after delivery should fail (no cancelOrder event on COMPLETED)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Attempt to cancel a COMPLETED order — should fail
Given that "event" equals "cancelOrder"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{
    "comment": "Trying to cancel a completed order"
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
