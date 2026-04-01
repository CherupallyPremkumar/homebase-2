Feature: Inventory Business Logic Validation — tests policy enforcement and edge cases.
  Covers rejection comment validation, reserve stock validation, and quantity enforcement.

# ===============================================================================
# REJECTION WITHOUT COMMENT: Should fail with policy violation
# ===============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create inventory for rejection validation
Given that "flowName" equals "inventory-flow"
And that "initialState" equals "STOCK_PENDING"
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Rejection Validation Batch",
    "productId": "prod-val-001",
    "quantity": 30
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "valId"

Scenario: Move to inspection state for validation tests
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${valId}/${event}" with payload
"""json
{
    "comment": "Starting inspection"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Reject stock without comment — should fail with policy violation
Given that "event" equals "rejectStock"
When I PATCH a REST request to URL "/inventory/${valId}/${event}" with payload
"""json
{
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false

Scenario: Verify inventory remains in STOCK_INSPECTION after failed rejection
When I GET a REST request to URL "/inventory/${valId}"
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Reject stock with valid comment — should succeed
Given that "event" equals "rejectStock"
When I PATCH a REST request to URL "/inventory/${valId}/${event}" with payload
"""json
{
    "comment": "Defective stitching found on all units"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_REJECTED"

# ===============================================================================
# APPROVE WITH ZERO QUANTITY: Should fail with policy violation
# ===============================================================================

Scenario: Create inventory for approval validation
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Approval Validation Batch",
    "productId": "prod-val-002",
    "quantity": 50
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "approvalValId"

Scenario: Move to inspection for approval validation
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${approvalValId}/${event}" with payload
"""json
{
    "comment": "Inspecting"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve with zero quantity — should fail
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${approvalValId}/${event}" with payload
"""json
{
    "comment": "Trying to approve zero units",
    "quantity": 0
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false

Scenario: Verify inventory remains in STOCK_INSPECTION after failed approval
When I GET a REST request to URL "/inventory/${approvalValId}"
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve with valid quantity — should succeed
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${approvalValId}/${event}" with payload
"""json
{
    "comment": "All 50 units pass QC",
    "quantity": 50
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

# ===============================================================================
# DAMAGE FOUND WITH QUANTITY TRACKING
# ===============================================================================

Scenario: Create inventory for damage quantity tracking
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Damage Tracking Batch",
    "productId": "prod-val-003",
    "quantity": 100
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "damageTrackId"

Scenario: Inspect damage tracking batch
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${damageTrackId}/${event}" with payload
"""json
{
    "comment": "Inspecting"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Report damage with specific quantity
Given that "event" equals "damageFound"
When I PATCH a REST request to URL "/inventory/${damageTrackId}/${event}" with payload
"""json
{
    "comment": "Transit damage to 10 units",
    "damagedQuantity": 10
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PARTIAL_DAMAGE"

Scenario: Repair specific quantity and return to approved
Given that "event" equals "repairDamageds"
When I PATCH a REST request to URL "/inventory/${damageTrackId}/${event}" with payload
"""json
{
    "comment": "Fixed 10 damaged units",
    "repairedQuantity": 10
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

# ===============================================================================
# FULL CYCLE: Create -> Inspect -> Approve -> Allocate -> Reserve -> Sold
# ===============================================================================

Scenario: Create inventory for full lifecycle test
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Full Lifecycle Product",
    "productId": "prod-lifecycle-001",
    "quantity": 20,
    "lowStockThreshold": 5
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "lcId"

Scenario: Full lifecycle - inspect
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Inspection"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Full lifecycle - approve
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Approved",
    "quantity": 20
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

Scenario: Full lifecycle - allocate
Given that "event" equals "allocateToWarehouse"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Warehouse A",
    "quantity": 20,
    "warehouseId": "WH-A"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Full lifecycle - reserve
Given that "event" equals "reserveStock"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Reserve for order",
    "quantity": 8,
    "orderId": "ORD-LC-001"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Full lifecycle - sell all reserved
Given that "event" equals "soldAllReserved"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Order fulfilled",
    "orderId": "ORD-LC-001"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Full lifecycle - sell all remaining stock
Given that "event" equals "sellAllStock"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Clearing out remaining stock"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "OUT_OF_STOCK"

Scenario: Full lifecycle - restock
Given that "event" equals "restockArrive"
When I PATCH a REST request to URL "/inventory/${lcId}/${event}" with payload
"""json
{
    "comment": "Restock",
    "quantity": 100
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_PENDING"
