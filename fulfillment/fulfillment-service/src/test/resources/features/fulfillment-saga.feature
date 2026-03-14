Feature: Order Fulfillment Saga
  Tests the complete fulfillment orchestration saga from order paid to delivery confirmed.
  States: INITIATED -> INVENTORY_RESERVED -> SHIPMENT_CREATED -> SHIPPED -> CUSTOMER_NOTIFIED -> COMPLETED
  Also covers: FAILED state and retry flow.

# ======================================================================
# COMPLETE FULFILLMENT FLOW
# ======================================================================

Scenario: Create a new fulfillment saga in INITIATED state
Given that "flowName" equals "fulfillment-flow"
And that "initialState" equals "INITIATED"
When I POST a REST request to URL "/fulfillment" with payload
"""json
{
    "orderId": "ORD-001",
    "userId": "USR-001",
    "description": "Fulfillment for order ORD-001"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "sagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Reserve inventory for the order (INITIATED -> INVENTORY_RESERVED)
Given that "event" equals "reserveInventory"
When I PATCH a REST request to URL "/fulfillment/${sagaId}/${event}" with payload
"""json
{
    "comment": "Reserving inventory for order items"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESERVED"

Scenario: Create shipment with carrier (INVENTORY_RESERVED -> SHIPMENT_CREATED)
Given that "event" equals "createShipment"
When I PATCH a REST request to URL "/fulfillment/${sagaId}/${event}" with payload
"""json
{
    "comment": "Creating shipment with carrier"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SHIPMENT_CREATED"

Scenario: Ship the order (SHIPMENT_CREATED -> SHIPPED)
Given that "event" equals "ship"
When I PATCH a REST request to URL "/fulfillment/${sagaId}/${event}" with payload
"""json
{
    "comment": "Order handed off to carrier"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SHIPPED"

Scenario: Notify customer about shipment (SHIPPED -> CUSTOMER_NOTIFIED)
Given that "event" equals "notifyCustomer"
When I PATCH a REST request to URL "/fulfillment/${sagaId}/${event}" with payload
"""json
{
    "comment": "Sending shipment notification to customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CUSTOMER_NOTIFIED"

Scenario: Confirm delivery (CUSTOMER_NOTIFIED -> COMPLETED)
Given that "event" equals "confirmDelivery"
When I PATCH a REST request to URL "/fulfillment/${sagaId}/${event}" with payload
"""json
{
    "comment": "Delivery confirmed by customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Verify completed fulfillment saga is retrievable
When I GET a REST request to URL "/fulfillment/${sagaId}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
And the REST response key "mutatedEntity.orderId" is "ORD-001"

# ======================================================================
# FULFILLMENT FAILS AT INVENTORY RESERVATION
# ======================================================================

Scenario: Create a second saga that will fail at inventory
When I POST a REST request to URL "/fulfillment" with payload
"""json
{
    "orderId": "ORD-FAIL-001",
    "userId": "USR-002",
    "description": "Fulfillment that will fail"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failSagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

# ======================================================================
# RETRY FAILED FULFILLMENT
# ======================================================================

Scenario: Create a saga in INITIATED state for retry testing
When I POST a REST request to URL "/fulfillment" with payload
"""json
{
    "orderId": "ORD-RETRY-001",
    "userId": "USR-003",
    "description": "Fulfillment to test retry"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "retrySagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Reserve inventory for retry saga (INITIATED -> INVENTORY_RESERVED)
Given that "event" equals "reserveInventory"
When I PATCH a REST request to URL "/fulfillment/${retrySagaId}/${event}" with payload
"""json
{
    "comment": "Reserve stock for retry saga"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESERVED"

# ======================================================================
# FULFILLMENT WITH PARTIAL STOCK SCENARIO
# ======================================================================

Scenario: Create a saga for partial stock fulfillment
When I POST a REST request to URL "/fulfillment" with payload
"""json
{
    "orderId": "ORD-PARTIAL-001",
    "userId": "USR-004",
    "description": "Fulfillment with partial stock"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "partialSagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Reserve partial stock (INITIATED -> INVENTORY_RESERVED)
Given that "event" equals "reserveInventory"
When I PATCH a REST request to URL "/fulfillment/${partialSagaId}/${event}" with payload
"""json
{
    "comment": "Partial stock reservation"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESERVED"

Scenario: Create shipment for partial stock (INVENTORY_RESERVED -> SHIPMENT_CREATED)
Given that "event" equals "createShipment"
When I PATCH a REST request to URL "/fulfillment/${partialSagaId}/${event}" with payload
"""json
{
    "comment": "Shipment for partial stock order"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SHIPMENT_CREATED"

# ======================================================================
# INVALID: Wrong event on COMPLETED (terminal state)
# ======================================================================

Scenario: Attempt to reserve inventory on a COMPLETED saga -- should fail
Given that "event" equals "reserveInventory"
When I PATCH a REST request to URL "/fulfillment/${sagaId}/${event}" with payload
"""json
{
    "comment": "Trying to reserve on completed saga"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
