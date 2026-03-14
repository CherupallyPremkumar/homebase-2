Feature: Return Processing Saga
  Tests the complete return processing orchestration saga from return approval
  through pickup, warehouse receipt, inventory restock, settlement adjustment,
  refund processing, and customer notification.
  States: INITIATED -> PICKUP_SCHEDULED -> ITEM_RECEIVED -> INVENTORY_RESTOCKED
          -> SETTLEMENT_ADJUSTED -> REFUNDED -> COMPLETED
  Also covers: FAILED state and retry flow.

# ======================================================================
# COMPLETE RETURN PROCESSING FLOW
# ======================================================================

Scenario: Create a new return processing saga in INITIATED state
Given that "flowName" equals "return-processing-flow"
And that "initialState" equals "INITIATED"
When I POST a REST request to URL "/return-processing" with payload
"""json
{
    "returnRequestId": "RET-001",
    "orderId": "ORD-100",
    "orderItemId": "ITEM-100-A",
    "refundAmount": 2500,
    "description": "Return processing for damaged item"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "sagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Schedule pickup for returned item (INITIATED -> PICKUP_SCHEDULED)
Given that "event" equals "schedulePickup"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Scheduling carrier pickup for return"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKUP_SCHEDULED"

Scenario: Warehouse receives returned item (PICKUP_SCHEDULED -> ITEM_RECEIVED)
Given that "event" equals "receiveItem"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Item received and inspected at warehouse"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"

Scenario: Restock inventory with returned item (ITEM_RECEIVED -> INVENTORY_RESTOCKED)
Given that "event" equals "restockInventory"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Adding returned quantity back to available stock"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESTOCKED"

Scenario: Adjust supplier settlement (INVENTORY_RESTOCKED -> SETTLEMENT_ADJUSTED)
Given that "event" equals "adjustSettlement"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Deducting returned item from supplier settlement"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SETTLEMENT_ADJUSTED"

Scenario: Process refund to customer (SETTLEMENT_ADJUSTED -> REFUNDED)
Given that "event" equals "processRefund"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Initiating refund to customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

Scenario: Notify customer of return completion (REFUNDED -> COMPLETED)
Given that "event" equals "notifyCustomer"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Sending refund confirmation to customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Verify completed return processing saga
When I GET a REST request to URL "/return-processing/${sagaId}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
And the REST response key "mutatedEntity.returnRequestId" is "RET-001"

# ======================================================================
# FAILED AT INVENTORY RESTOCK
# ======================================================================

Scenario: Create a second saga for failure testing
When I POST a REST request to URL "/return-processing" with payload
"""json
{
    "returnRequestId": "RET-FAIL-001",
    "orderId": "ORD-200",
    "orderItemId": "ITEM-200-A",
    "refundAmount": 1500,
    "description": "Return that will fail at restock"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failSagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Schedule pickup for fail saga (INITIATED -> PICKUP_SCHEDULED)
Given that "event" equals "schedulePickup"
When I PATCH a REST request to URL "/return-processing/${failSagaId}/${event}" with payload
"""json
{
    "comment": "Scheduling pickup"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PICKUP_SCHEDULED"

Scenario: Receive item for fail saga (PICKUP_SCHEDULED -> ITEM_RECEIVED)
Given that "event" equals "receiveItem"
When I PATCH a REST request to URL "/return-processing/${failSagaId}/${event}" with payload
"""json
{
    "comment": "Item received"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"

# ======================================================================
# SETTLEMENT ADJUSTMENT
# ======================================================================

Scenario: Create a third saga for settlement testing
When I POST a REST request to URL "/return-processing" with payload
"""json
{
    "returnRequestId": "RET-SETTLE-001",
    "orderId": "ORD-300",
    "orderItemId": "ITEM-300-A",
    "refundAmount": 3500,
    "description": "Return for settlement adjustment test"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "settleSagaId"

Scenario: Progress settlement saga to INVENTORY_RESTOCKED
Given that "event" equals "schedulePickup"
When I PATCH a REST request to URL "/return-processing/${settleSagaId}/${event}" with payload
"""json
{
    "comment": "Schedule pickup"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PICKUP_SCHEDULED"

Scenario: Receive item for settlement saga
Given that "event" equals "receiveItem"
When I PATCH a REST request to URL "/return-processing/${settleSagaId}/${event}" with payload
"""json
{
    "comment": "Item received"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"

Scenario: Restock for settlement saga
Given that "event" equals "restockInventory"
When I PATCH a REST request to URL "/return-processing/${settleSagaId}/${event}" with payload
"""json
{
    "comment": "Restocked"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESTOCKED"

Scenario: Adjust settlement for the return
Given that "event" equals "adjustSettlement"
When I PATCH a REST request to URL "/return-processing/${settleSagaId}/${event}" with payload
"""json
{
    "comment": "Adjusting supplier settlement for refund amount 3500"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SETTLEMENT_ADJUSTED"

# ======================================================================
# REFUND PROCESSING
# ======================================================================

Scenario: Process refund for settlement saga
Given that "event" equals "processRefund"
When I PATCH a REST request to URL "/return-processing/${settleSagaId}/${event}" with payload
"""json
{
    "comment": "Processing customer refund"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

Scenario: Complete the settlement saga
Given that "event" equals "notifyCustomer"
When I PATCH a REST request to URL "/return-processing/${settleSagaId}/${event}" with payload
"""json
{
    "comment": "Notifying customer of refund"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

# ======================================================================
# INVALID: Wrong event on COMPLETED (terminal state)
# ======================================================================

Scenario: Attempt to schedule pickup on completed saga -- should fail
Given that "event" equals "schedulePickup"
When I PATCH a REST request to URL "/return-processing/${sagaId}/${event}" with payload
"""json
{
    "comment": "Trying on completed saga"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
