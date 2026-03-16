Feature: Inventory Lifecycle — tests the full inventory state machine through all paths.
  Covers the happy path (STOCK_PENDING -> STOCK_INSPECTION -> STOCK_APPROVED -> IN_WAREHOUSE),
  the rejection flow, the damage flow, allocation/reservation, sell-all, restock, and error handling.

# ===============================================================================
# HAPPY PATH: Pending -> Inspect -> Approve -> Allocate -> Reserve -> Release
# ===============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new inventory record in STOCK_PENDING state
Given that "flowName" equals "inventory-flow"
And that "initialState" equals "STOCK_PENDING"
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Silk Scarf Batch",
    "productId": "prod-001",
    "quantity": 100,
    "lowStockThreshold": 10
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.description" is "Silk Scarf Batch"

Scenario: Retrieve the inventory record
When I GET a REST request to URL "/inventory/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_PENDING"

Scenario: Inspect stock (STOCK_PENDING -> STOCK_INSPECTION)
Given that "comment" equals "Shipment received, beginning inspection"
And that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve stock with quantity (STOCK_INSPECTION -> STOCK_APPROVED)
Given that "comment" equals "Quality check passed, all 100 units good"
And that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "quantity": 100
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

Scenario: Allocate to warehouse with fulfillment center (STOCK_APPROVED -> IN_WAREHOUSE)
Given that "comment" equals "Stock placed in warehouse zone A3"
And that "event" equals "allocateToWarehouse"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "quantity": 100,
    "warehouseId": "WH-CHENNAI-01",
    "zone": "A3"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Reserve stock for an order (IN_WAREHOUSE -> IN_WAREHOUSE)
Given that "comment" equals "Reserving 5 units for order ORD-001"
And that "event" equals "reserveStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "quantity": 5,
    "orderId": "ORD-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Reserve more stock for another order (IN_WAREHOUSE -> IN_WAREHOUSE)
Given that "comment" equals "Reserving 3 more units for order ORD-002"
And that "event" equals "reserveStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "quantity": 3,
    "orderId": "ORD-002"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Release reserved stock for cancelled order (IN_WAREHOUSE -> IN_WAREHOUSE)
Given that "comment" equals "Order ORD-001 cancelled, releasing reserved stock"
And that "event" equals "releaseReservedStock"
When I PATCH a REST request to URL "/inventory/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "orderId": "ORD-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Verify inventory record after release
When I GET a REST request to URL "/inventory/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

# ===============================================================================
# SELL ALL STOCK: IN_WAREHOUSE -> OUT_OF_STOCK -> Restock -> STOCK_PENDING
# ===============================================================================

Scenario: Create inventory for sell-all flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Popular T-Shirt Batch",
    "productId": "prod-sell-01",
    "quantity": 50,
    "lowStockThreshold": 5
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "sellId"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_PENDING"

Scenario: Move sell-all batch through inspection
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${sellId}/${event}" with payload
"""json
{
    "comment": "Inspecting T-shirt batch"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve sell-all batch
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${sellId}/${event}" with payload
"""json
{
    "comment": "All units passed QC",
    "quantity": 50
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

Scenario: Allocate sell-all batch to warehouse
Given that "event" equals "allocateToWarehouse"
When I PATCH a REST request to URL "/inventory/${sellId}/${event}" with payload
"""json
{
    "comment": "Placing in warehouse",
    "quantity": 50,
    "warehouseId": "WH-MUMBAI-01"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Sell all stock (IN_WAREHOUSE -> OUT_OF_STOCK)
Given that "event" equals "sellAllStock"
When I PATCH a REST request to URL "/inventory/${sellId}/${event}" with payload
"""json
{
    "comment": "Flash sale - all stock sold"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sellId}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_OF_STOCK"

Scenario: Restock after out of stock (OUT_OF_STOCK -> STOCK_PENDING)
Given that "event" equals "restockArrive"
When I PATCH a REST request to URL "/inventory/${sellId}/${event}" with payload
"""json
{
    "comment": "New shipment of 200 units arrived",
    "quantity": 200,
    "supplierId": "SUP-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${sellId}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_PENDING"

# ===============================================================================
# SOLD ALL RESERVED: Reserve -> Sold All -> OUT_OF_STOCK
# ===============================================================================

Scenario: Create inventory for sold-all-reserved flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Limited Edition Item",
    "productId": "prod-limited-01",
    "quantity": 10,
    "lowStockThreshold": 3
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "limitedId"

Scenario: Fast-track limited edition through to warehouse
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${limitedId}/${event}" with payload
"""json
{
    "comment": "Quick inspection"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve limited edition
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${limitedId}/${event}" with payload
"""json
{
    "comment": "Approved",
    "quantity": 10
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

Scenario: Allocate limited edition to warehouse
Given that "event" equals "allocateToWarehouse"
When I PATCH a REST request to URL "/inventory/${limitedId}/${event}" with payload
"""json
{
    "comment": "Stored in premium section",
    "quantity": 10,
    "warehouseId": "WH-PREMIUM-01"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Reserve all limited stock
Given that "event" equals "reserveStock"
When I PATCH a REST request to URL "/inventory/${limitedId}/${event}" with payload
"""json
{
    "comment": "Reserving all 10 for bulk order",
    "quantity": 10,
    "orderId": "ORD-BULK-001"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Mark all reserved as sold (IN_WAREHOUSE -> OUT_OF_STOCK)
Given that "event" equals "soldAllReserved"
When I PATCH a REST request to URL "/inventory/${limitedId}/${event}" with payload
"""json
{
    "comment": "All reserved stock fulfilled",
    "orderId": "ORD-BULK-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${limitedId}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_OF_STOCK"

# ===============================================================================
# REJECT STOCK FLOW: Pending -> Inspect -> Reject -> Return to Supplier
# ===============================================================================

Scenario: Create inventory for rejection flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Low Quality Batch",
    "productId": "prod-002",
    "quantity": 50
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejectId"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_PENDING"

Scenario: Inspect rejected stock
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${rejectId}/${event}" with payload
"""json
{
    "comment": "Beginning inspection of low quality batch"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Reject stock with comment (STOCK_INSPECTION -> STOCK_REJECTED)
Given that "event" equals "rejectStock"
When I PATCH a REST request to URL "/inventory/${rejectId}/${event}" with payload
"""json
{
    "comment": "Quality below acceptable threshold, rejecting entire batch"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectId}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_REJECTED"

Scenario: Return rejected stock to supplier (STOCK_REJECTED -> RETURN_TO_SUPPLIER)
Given that "event" equals "returnRejectedStock"
When I PATCH a REST request to URL "/inventory/${rejectId}/${event}" with payload
"""json
{
    "comment": "Arranging return shipment to supplier"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectId}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_TO_SUPPLIER"

Scenario: Return completed (RETURN_TO_SUPPLIER -> RETURNED_TO_SUPPLIER)
Given that "event" equals "returnCompleted"
When I PATCH a REST request to URL "/inventory/${rejectId}/${event}" with payload
"""json
{
    "comment": "Supplier acknowledged receipt of returned stock"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectId}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURNED_TO_SUPPLIER"

# ===============================================================================
# DAMAGE FLOW: Inspect -> damageFound -> PARTIAL_DAMAGE -> repair or discard
# ===============================================================================

Scenario: Create inventory for damage flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Fragile Glass Batch",
    "productId": "prod-003",
    "quantity": 30
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "damageId"

Scenario: Inspect fragile batch
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${damageId}/${event}" with payload
"""json
{
    "comment": "Inspecting glass batch"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Report damage found during inspection (STOCK_INSPECTION -> PARTIAL_DAMAGE)
Given that "event" equals "damageFound"
When I PATCH a REST request to URL "/inventory/${damageId}/${event}" with payload
"""json
{
    "comment": "5 out of 30 units cracked during transit",
    "damagedQuantity": 5
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${damageId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PARTIAL_DAMAGE"

Scenario: Repair damaged items (PARTIAL_DAMAGE -> STOCK_APPROVED)
Given that "event" equals "repairDamageds"
When I PATCH a REST request to URL "/inventory/${damageId}/${event}" with payload
"""json
{
    "comment": "Damaged units repaired and re-inspected",
    "repairedQuantity": 5
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${damageId}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

# ===============================================================================
# PARTIAL_DAMAGE -> returnToSupplier -> RETURN_TO_SUPPLIER -> RETURNED_TO_SUPPLIER
# ===============================================================================

Scenario: Create inventory for damage-return flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Defective Electronics Batch",
    "productId": "prod-004",
    "quantity": 20
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "damageReturnId"

Scenario: Inspect defective batch
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${damageReturnId}/${event}" with payload
"""json
{
    "comment": "Inspecting electronics"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Report severe damage on electronics
Given that "event" equals "damageFound"
When I PATCH a REST request to URL "/inventory/${damageReturnId}/${event}" with payload
"""json
{
    "comment": "15 of 20 units have circuit board damage",
    "damagedQuantity": 15
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PARTIAL_DAMAGE"

Scenario: Return damaged stock to supplier (PARTIAL_DAMAGE -> RETURN_TO_SUPPLIER)
Given that "event" equals "returnToSupplier"
When I PATCH a REST request to URL "/inventory/${damageReturnId}/${event}" with payload
"""json
{
    "comment": "Returning severely damaged batch to supplier",
    "returnQuantity": 15,
    "returnReason": "Manufacturing defect"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${damageReturnId}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_TO_SUPPLIER"

Scenario: Complete return of damaged electronics
Given that "event" equals "returnCompleted"
When I PATCH a REST request to URL "/inventory/${damageReturnId}/${event}" with payload
"""json
{
    "comment": "Supplier acknowledged defective batch",
    "supplierAcknowledgementRef": "SUP-ACK-20260314"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${damageReturnId}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURNED_TO_SUPPLIER"

# ===============================================================================
# PARTIAL_DAMAGE -> discardDamaged -> STOCK_APPROVED
# ===============================================================================

Scenario: Create inventory for discard-from-partial-damage flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Ceramic Tiles Batch",
    "productId": "prod-005",
    "quantity": 40
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "discardPartialId"

Scenario: Inspect ceramic tiles
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${discardPartialId}/${event}" with payload
"""json
{
    "comment": "Inspecting tile batch"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Report minor damage on tiles
Given that "event" equals "damageFound"
When I PATCH a REST request to URL "/inventory/${discardPartialId}/${event}" with payload
"""json
{
    "comment": "3 tiles chipped",
    "damagedQuantity": 3
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PARTIAL_DAMAGE"

Scenario: Discard damaged tiles and move rest to approved (PARTIAL_DAMAGE -> STOCK_APPROVED)
Given that "event" equals "discardDamaged"
When I PATCH a REST request to URL "/inventory/${discardPartialId}/${event}" with payload
"""json
{
    "comment": "Chipped tiles beyond repair, discarding"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${discardPartialId}"
And the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

# ===============================================================================
# WAREHOUSE DAMAGE FLOW: IN_WAREHOUSE -> returnDamaged -> discard
# ===============================================================================

Scenario: Create inventory for warehouse damage flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Warehouse Stored Batch",
    "productId": "prod-006",
    "quantity": 60
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "whDamageId"

Scenario: Fast-track to warehouse
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${whDamageId}/${event}" with payload
"""json
{
    "comment": "Quick inspection"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve warehouse batch
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${whDamageId}/${event}" with payload
"""json
{
    "comment": "Approved",
    "quantity": 60
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

Scenario: Allocate warehouse batch
Given that "event" equals "allocateToWarehouse"
When I PATCH a REST request to URL "/inventory/${whDamageId}/${event}" with payload
"""json
{
    "comment": "Allocating to warehouse",
    "quantity": 60,
    "warehouseId": "WH-DELHI-01"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Report partial damage at warehouse — good stock remains, stays IN_WAREHOUSE
Given that "event" equals "returnDamaged"
When I PATCH a REST request to URL "/inventory/${whDamageId}/${event}" with payload
"""json
{
    "comment": "Water leak damaged some items",
    "damagedQuantity": 8
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${whDamageId}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Report all remaining stock damaged — available=0, moves to DAMAGED_AT_WAREHOUSE
Given that "event" equals "returnDamaged"
When I PATCH a REST request to URL "/inventory/${whDamageId}/${event}" with payload
"""json
{
    "comment": "Water leak spread, all remaining stock damaged",
    "damagedQuantity": 52
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${whDamageId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DAMAGED_AT_WAREHOUSE"

Scenario: Repair all warehouse damaged items (DAMAGED_AT_WAREHOUSE -> IN_WAREHOUSE)
Given that "event" equals "repairDamaged"
When I PATCH a REST request to URL "/inventory/${whDamageId}/${event}" with payload
"""json
{
    "comment": "All water-damaged items dried and restored",
    "repairedQuantity": 60
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${whDamageId}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

# ===============================================================================
# WAREHOUSE DAMAGE -> DISCARD FLOW
# ===============================================================================

Scenario: Create inventory for warehouse discard flow
When I POST a REST request to URL "/inventory" with payload
"""json
{
    "description": "Perishable Goods Batch",
    "productId": "prod-007",
    "quantity": 25
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "whDiscardId"

Scenario: Move perishable goods through to warehouse
Given that "event" equals "inspectStock"
When I PATCH a REST request to URL "/inventory/${whDiscardId}/${event}" with payload
"""json
{
    "comment": "Inspecting"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_INSPECTION"

Scenario: Approve perishable batch
Given that "event" equals "approveStock"
When I PATCH a REST request to URL "/inventory/${whDiscardId}/${event}" with payload
"""json
{
    "comment": "OK",
    "quantity": 25
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "STOCK_APPROVED"

Scenario: Allocate perishable batch
Given that "event" equals "allocateToWarehouse"
When I PATCH a REST request to URL "/inventory/${whDiscardId}/${event}" with payload
"""json
{
    "comment": "Cold storage",
    "quantity": 25,
    "warehouseId": "WH-COLD-01"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Spoilage discovered — partial damage, stays IN_WAREHOUSE (15 good units still sellable)
Given that "event" equals "returnDamaged"
When I PATCH a REST request to URL "/inventory/${whDiscardId}/${event}" with payload
"""json
{
    "comment": "Cooling failure, 10 units spoiled",
    "damagedQuantity": 10
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

Scenario: Discard spoiled items from warehouse (IN_WAREHOUSE -> IN_WAREHOUSE, good stock remains)
Given that "event" equals "discardDamaged"
When I PATCH a REST request to URL "/inventory/${whDiscardId}/${event}" with payload
"""json
{
    "comment": "Spoiled items cannot be recovered, discarding"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${whDiscardId}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_WAREHOUSE"

# ===============================================================================
# INVALID EVENT: Wrong event on wrong state
# ===============================================================================

Scenario: Attempt to reserve stock on RETURNED_TO_SUPPLIER — should fail
Given that "event" equals "reserveStock"
When I PATCH a REST request to URL "/inventory/${rejectId}/${event}" with payload
"""json
{
    "comment": "Cannot reserve stock that is returned"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to inventory — should fail
When I PATCH a REST request to URL "/inventory/${id}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
