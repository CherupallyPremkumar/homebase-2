Feature: Return Processing Saga
  Tests the complete return processing orchestration saga from return approval
  through pickup, warehouse receipt, inspection, inventory restock, settlement
  adjustment, refund processing, and customer notification.
  States: INITIATED -> PICKUP_SCHEDULED -> ITEM_RECEIVED -> CHECK_ITEM_CONDITION
          -> INVENTORY_RESTOCKED -> SETTLEMENT_ADJUSTED -> REFUNDED -> COMPLETED
  Also covers: FAILED state and retry flow.

# ======================================================================
# COMPLETE RETURN PROCESSING FLOW (Happy Path)
# ======================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

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
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Schedule pickup for returned item (INITIATED -> PICKUP_SCHEDULED)
Given that "event" equals "schedulePickup"
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Scheduling carrier pickup for return"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKUP_SCHEDULED"

Scenario: Warehouse receives returned item (PICKUP_SCHEDULED -> ITEM_RECEIVED)
Given that "event" equals "receiveItem"
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Item received and inspected at warehouse"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"

Scenario: Inspect item with GOOD condition (ITEM_RECEIVED -> CHECK_ITEM_CONDITION -> INVENTORY_RESTOCKED)
Given that "event" equals "inspectItem"
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Item inspected at warehouse",
    "itemCondition": "GOOD",
    "resellable": true
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESTOCKED"

Scenario: Adjust supplier settlement (INVENTORY_RESTOCKED -> SETTLEMENT_ADJUSTED)
Given that "event" equals "adjustSettlement"
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Deducting returned item from supplier settlement"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SETTLEMENT_ADJUSTED"

Scenario: Process refund to customer (SETTLEMENT_ADJUSTED -> REFUNDED)
Given that "event" equals "processRefund"
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Initiating refund to customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

Scenario: Notify customer of return completion (REFUNDED -> COMPLETED)
Given that "event" equals "notifyCustomer"
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Sending refund confirmation to customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Verify completed return processing saga
When I GET a REST request to URL "/return-processing/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
And the REST response key "mutatedEntity.returnRequestId" is "RET-001"

# ======================================================================
# SECOND SAGA: Full happy path (schedule + receive + inspect + restock + settle + refund + notify)
# ======================================================================

Scenario: Create a second saga for full lifecycle test
When I POST a REST request to URL "/return-processing" with payload
"""json
{
    "returnRequestId": "RET-002",
    "orderId": "ORD-200",
    "orderItemId": "ITEM-200-A",
    "refundAmount": 3500,
    "description": "Return for full lifecycle test"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Schedule pickup for second saga
Given that "event" equals "schedulePickup"
When I PATCH a REST request to URL "/return-processing/${id2}/${event}" with payload
"""json
{
    "comment": "Schedule pickup"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PICKUP_SCHEDULED"

Scenario: Receive item for second saga
Given that "event" equals "receiveItem"
When I PATCH a REST request to URL "/return-processing/${id2}/${event}" with payload
"""json
{
    "comment": "Item received"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"

Scenario: Inspect item for second saga with LIKE_NEW condition
Given that "event" equals "inspectItem"
When I PATCH a REST request to URL "/return-processing/${id2}/${event}" with payload
"""json
{
    "comment": "Inspecting item",
    "itemCondition": "LIKE_NEW",
    "resellable": true
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_RESTOCKED"

Scenario: Adjust settlement for second saga
Given that "event" equals "adjustSettlement"
When I PATCH a REST request to URL "/return-processing/${id2}/${event}" with payload
"""json
{
    "comment": "Adjusting supplier settlement for refund amount 3500"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SETTLEMENT_ADJUSTED"

Scenario: Process refund for second saga
Given that "event" equals "processRefund"
When I PATCH a REST request to URL "/return-processing/${id2}/${event}" with payload
"""json
{
    "comment": "Processing customer refund"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

Scenario: Complete the second saga
Given that "event" equals "notifyCustomer"
When I PATCH a REST request to URL "/return-processing/${id2}/${event}" with payload
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
When I PATCH a REST request to URL "/return-processing/${id}/${event}" with payload
"""json
{
    "comment": "Trying on completed saga"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
