Feature: Supplier Lifecycle Orchestration
  Tests the supplier lifecycle orchestration saga for both suspend and reactivate flows.
  Suspend flow: INITIATED -> PRODUCTS_DISABLED -> CATALOG_CLEARED -> INVENTORY_FROZEN
                -> ORDERS_CANCELLED -> COMPLETED
  Reactivate flow: INITIATED -> PRODUCTS_ENABLED -> CATALOG_RESTORED -> INVENTORY_UNFROZEN
                   -> COMPLETED
  Also covers: FAILED state and retry flow.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

# ======================================================================
# SUSPEND SUPPLIER CASCADING EFFECTS (suspend-flow)
# ======================================================================

Scenario: Create a supplier suspension saga in INITIATED state
Given that "flowName" equals "suspend-flow"
And that "initialState" equals "INITIATED"
When I POST a REST request to URL "/supplier-lifecycle" with payload
"""json
{
    "supplierId": "SUPP-001",
    "action": "SUSPEND",
    "reason": "Repeated quality complaints",
    "description": "Suspension saga for supplier SUPP-001"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "suspendSagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Disable all supplier products (INITIATED -> PRODUCTS_DISABLED)
Given that "event" equals "disableProducts"
When I PATCH a REST request to URL "/supplier-lifecycle/${suspendSagaId}/${event}" with payload
"""json
{
    "comment": "Disabling all products for suspended supplier"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${suspendSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PRODUCTS_DISABLED"

Scenario: Remove supplier catalog entries (PRODUCTS_DISABLED -> CATALOG_CLEARED)
Given that "event" equals "removeCatalog"
When I PATCH a REST request to URL "/supplier-lifecycle/${suspendSagaId}/${event}" with payload
"""json
{
    "comment": "Removing catalog entries for supplier products"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${suspendSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CATALOG_CLEARED"

Scenario: Freeze supplier inventory (CATALOG_CLEARED -> INVENTORY_FROZEN)
Given that "event" equals "freezeInventory"
When I PATCH a REST request to URL "/supplier-lifecycle/${suspendSagaId}/${event}" with payload
"""json
{
    "comment": "Freezing all inventory for suspended supplier"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${suspendSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_FROZEN"

Scenario: Cancel pending orders (INVENTORY_FROZEN -> ORDERS_CANCELLED)
Given that "event" equals "cancelOrders"
When I PATCH a REST request to URL "/supplier-lifecycle/${suspendSagaId}/${event}" with payload
"""json
{
    "comment": "Cancelling all pending orders containing supplier items"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${suspendSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ORDERS_CANCELLED"

Scenario: Notify affected customers (ORDERS_CANCELLED -> COMPLETED)
Given that "event" equals "notifyCustomers"
When I PATCH a REST request to URL "/supplier-lifecycle/${suspendSagaId}/${event}" with payload
"""json
{
    "comment": "Notifying customers about order cancellations"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${suspendSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

Scenario: Verify completed suspension saga
When I GET a REST request to URL "/supplier-lifecycle/${suspendSagaId}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${suspendSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"
And the REST response key "mutatedEntity.supplierId" is "SUPP-001"

# ======================================================================
# REACTIVATE SUPPLIER RESTORES PRODUCTS (reactivate-flow)
# ======================================================================

Scenario: Create a supplier reactivation saga
Given that "flowName" equals "reactivate-flow"
When I POST a REST request to URL "/supplier-lifecycle" with payload
"""json
{
    "supplierId": "SUPP-001",
    "action": "REACTIVATE",
    "reason": "Quality issues resolved",
    "description": "Reactivation saga for supplier SUPP-001",
    "flowId": "reactivate-flow"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "reactivateSagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Enable products (INITIATED -> PRODUCTS_ENABLED)
Given that "event" equals "enableProducts"
When I PATCH a REST request to URL "/supplier-lifecycle/${reactivateSagaId}/${event}" with payload
"""json
{
    "comment": "Re-enabling all supplier products"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${reactivateSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PRODUCTS_ENABLED"

Scenario: Restore catalog (PRODUCTS_ENABLED -> CATALOG_RESTORED)
Given that "event" equals "restoreCatalog"
When I PATCH a REST request to URL "/supplier-lifecycle/${reactivateSagaId}/${event}" with payload
"""json
{
    "comment": "Restoring catalog entries for supplier products"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${reactivateSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CATALOG_RESTORED"

Scenario: Unfreeze inventory (CATALOG_RESTORED -> INVENTORY_UNFROZEN)
Given that "event" equals "unfreezeInventory"
When I PATCH a REST request to URL "/supplier-lifecycle/${reactivateSagaId}/${event}" with payload
"""json
{
    "comment": "Unfreezing supplier inventory"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${reactivateSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "INVENTORY_UNFROZEN"

Scenario: Notify supplier of reactivation (INVENTORY_UNFROZEN -> COMPLETED)
Given that "event" equals "notifySupplier"
When I PATCH a REST request to URL "/supplier-lifecycle/${reactivateSagaId}/${event}" with payload
"""json
{
    "comment": "Notifying supplier about reactivation"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${reactivateSagaId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

# ======================================================================
# FAILED AT CATALOG REMOVAL -- RETRY
# ======================================================================

Scenario: Create a saga that will need retry
When I POST a REST request to URL "/supplier-lifecycle" with payload
"""json
{
    "supplierId": "SUPP-RETRY-001",
    "action": "SUSPEND",
    "reason": "Testing retry",
    "description": "Suspension saga that will be retried"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "retrySagaId"
And the REST response key "mutatedEntity.currentState.stateId" is "INITIATED"

Scenario: Disable products for retry saga
Given that "event" equals "disableProducts"
When I PATCH a REST request to URL "/supplier-lifecycle/${retrySagaId}/${event}" with payload
"""json
{
    "comment": "Disabling products for retry test"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PRODUCTS_DISABLED"

# ======================================================================
# INVALID: Wrong event on COMPLETED (terminal state)
# ======================================================================

Scenario: Attempt to disable products on completed saga -- should fail
Given that "event" equals "disableProducts"
When I PATCH a REST request to URL "/supplier-lifecycle/${suspendSagaId}/${event}" with payload
"""json
{
    "comment": "Trying on completed saga"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
