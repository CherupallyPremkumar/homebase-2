Feature: Tests the Inventory Query Service using a REST client with security.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

# ═══════════════════════════════════════════════════════════════════════════
# INVENTORIES — paginated admin listing with filters
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Paginated listing of all inventory items sorted by id
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "8"
  And the REST response key "list[0].row.id" is "inv-1"
  And the REST response key "list[0].row.sku" is "SKU-WH-001"
  And the REST response key "list[0].row.stateId" is "IN_WAREHOUSE"
  And the REST response key "list[1].row.id" is "inv-2"
  And the REST response key "list[2].row.id" is "inv-3"
  And the REST response key "list[3].row.id" is "inv-4"
  And the REST response key "list[4].row.id" is "inv-5"
  And the REST response key "list[5].row.id" is "inv-6"
  And the REST response key "list[6].row.id" is "inv-7"
  And the REST response key "list[7].row.id" is "inv-8"

Scenario: Paginated listing with page size 3 returns first page
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 3
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.id" is "inv-1"
  And the REST response key "list[1].row.id" is "inv-2"
  And the REST response key "list[2].row.id" is "inv-3"

Scenario: Paginated listing page 2 with page size 3
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ],
    "pageNum": 2,
    "numRowsInPage": 3
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.id" is "inv-4"
  And the REST response key "list[1].row.id" is "inv-5"
  And the REST response key "list[2].row.id" is "inv-6"

Scenario: Filter inventory by productId
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "productId": "prod-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"
  And the REST response key "list[0].row.productId" is "prod-1"
  And the REST response key "list[1].row.productId" is "prod-1"

Scenario: Filter inventory by sku
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "sku": "SKU-BL-001"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-3"
  And the REST response key "list[0].row.sku" is "SKU-BL-001"

Scenario: Filter inventory by variantId
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "variantId": "var-6"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-6"
  And the REST response key "list[0].row.variantId" is "var-6"
  And the REST response key "list[0].row.sku" is "SKU-BT-001"

Scenario: Filter inventory by stateId IN_WAREHOUSE
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "stateId": "IN_WAREHOUSE"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.stateId" is "IN_WAREHOUSE"
  And the REST response key "list[1].row.stateId" is "IN_WAREHOUSE"
  And the REST response key "list[2].row.stateId" is "IN_WAREHOUSE"

Scenario: Filter inventory by status OUT_OF_STOCK
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "status": "OUT_OF_STOCK"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-3"
  And the REST response key "list[0].row.status" is "OUT_OF_STOCK"

Scenario: Filter inventory by primaryFc FC-CHENNAI
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "primaryFc": "FC-CHENNAI"
    },
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ]
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.id" is "inv-1"
  And the REST response key "list[0].row.primaryFc" is "FC-CHENNAI"
  And the REST response key "list[1].row.id" is "inv-4"
  And the REST response key "list[1].row.primaryFc" is "FC-CHENNAI"
  And the REST response key "list[2].row.id" is "inv-7"
  And the REST response key "list[2].row.primaryFc" is "FC-CHENNAI"

Scenario: Filter inventory by primaryFc FC-MUMBAI
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "primaryFc": "FC-MUMBAI"
    },
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ]
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.id" is "inv-2"
  And the REST response key "list[1].row.id" is "inv-5"
  And the REST response key "list[2].row.id" is "inv-8"

Scenario: Filter inventory by primaryFc FC-BANGALORE
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "primaryFc": "FC-BANGALORE"
    },
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ]
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"
  And the REST response key "list[0].row.id" is "inv-3"
  And the REST response key "list[1].row.id" is "inv-6"

Scenario: Combine filters productId and primaryFc
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "productId": "prod-1",
      "primaryFc": "FC-CHENNAI"
    },
    "sortCriteria": [
      {"name": "id", "ascendingOrder": true}
    ]
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"
  And the REST response key "list[0].row.id" is "inv-1"
  And the REST response key "list[1].row.id" is "inv-4"

Scenario: Filter returns no results for non-existent sku
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "filters": {
      "sku": "SKU-NONEXISTENT"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "0"

Scenario: Sort inventory by availableQuantity descending
  When I POST a REST request to URL "/q/inventories" with payload
  """
  {
    "sortCriteria": [
      {"name": "available_quantity", "ascendingOrder": false}
    ],
    "pageNum": 1,
    "numRowsInPage": 3
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.id" is "inv-6"
  And the REST response key "list[0].row.availableQuantity" is "450"

# ═══════════════════════════════════════════════════════════════════════════
# INVENTORY — get by id
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Get inventory by id - full detail for inv-1
  When I POST a REST request to URL "/q/inventory" with payload
  """
  {
    "filters": {
      "id": "inv-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.id" is "inv-1"
  And the REST response key "list[0].row.sku" is "SKU-WH-001"
  And the REST response key "list[0].row.productId" is "prod-1"
  And the REST response key "list[0].row.variantId" is "var-1"
  And the REST response key "list[0].row.quantity" is "100"
  And the REST response key "list[0].row.availableQuantity" is "80"
  And the REST response key "list[0].row.reserved" is "15"
  And the REST response key "list[0].row.damagedQuantity" is "5"
  And the REST response key "list[0].row.primaryFc" is "FC-CHENNAI"
  And the REST response key "list[0].row.stateId" is "IN_WAREHOUSE"

Scenario: Get inventory by id - inv-2 with inbound quantity
  When I POST a REST request to URL "/q/inventory" with payload
  """
  {
    "filters": {
      "id": "inv-2"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.id" is "inv-2"
  And the REST response key "list[0].row.sku" is "SKU-TS-001"
  And the REST response key "list[0].row.quantity" is "50"
  And the REST response key "list[0].row.availableQuantity" is "50"
  And the REST response key "list[0].row.inboundQuantity" is "50"
  And the REST response key "list[0].row.stateId" is "STOCK_PENDING"

Scenario: Get inventory by id - inv-4 low stock item
  When I POST a REST request to URL "/q/inventory" with payload
  """
  {
    "filters": {
      "id": "inv-4"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.id" is "inv-4"
  And the REST response key "list[0].row.sku" is "SKU-WH-002"
  And the REST response key "list[0].row.quantity" is "200"
  And the REST response key "list[0].row.availableQuantity" is "5"
  And the REST response key "list[0].row.lowStockThreshold" is "20"
  And the REST response key "list[0].row.status" is "LOW_STOCK"
  And the REST response key "list[0].row.stateId" is "IN_WAREHOUSE"

Scenario: Get inventory by id - inv-6 high volume item
  When I POST a REST request to URL "/q/inventory" with payload
  """
  {
    "filters": {
      "id": "inv-6"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.id" is "inv-6"
  And the REST response key "list[0].row.sku" is "SKU-BT-001"
  And the REST response key "list[0].row.quantity" is "500"
  And the REST response key "list[0].row.availableQuantity" is "450"
  And the REST response key "list[0].row.reserved" is "50"
  And the REST response key "list[0].row.primaryFc" is "FC-BANGALORE"

Scenario: Get inventory by id - non-existent returns no rows
  When I POST a REST request to URL "/q/inventory" with payload
  """
  {
    "filters": {
      "id": "inv-999"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════════
# INVENTORY-BY-PRODUCT — filter by productId
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Get inventory by productId - prod-1 has two items
  When I POST a REST request to URL "/q/inventory-by-product" with payload
  """
  {
    "filters": {
      "productId": "prod-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"
  And the REST response key "list[0].row.productId" is "prod-1"
  And the REST response key "list[1].row.productId" is "prod-1"

Scenario: Get inventory by productId - prod-3 out of stock
  When I POST a REST request to URL "/q/inventory-by-product" with payload
  """
  {
    "filters": {
      "productId": "prod-3"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-3"
  And the REST response key "list[0].row.productId" is "prod-3"
  And the REST response key "list[0].row.stateId" is "OUT_OF_STOCK"

Scenario: Get inventory by productId - prod-5 high volume
  When I POST a REST request to URL "/q/inventory-by-product" with payload
  """
  {
    "filters": {
      "productId": "prod-5"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-6"
  And the REST response key "list[0].row.availableQuantity" is "450"

Scenario: Get inventory by productId - non-existent product
  When I POST a REST request to URL "/q/inventory-by-product" with payload
  """
  {
    "filters": {
      "productId": "prod-999"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════════
# CHECK-AVAILABILITY — storefront availability check by product + variant
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Check availability by productId and variantId - available item
  When I POST a REST request to URL "/q/check-availability" with payload
  """
  {
    "filters": {
      "productId": "prod-1",
      "variantId": "var-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-1"
  And the REST response key "list[0].row.availableQuantity" is "80"
  And the REST response key "list[0].row.reserved" is "15"
  And the REST response key "list[0].row.status" is "AVAILABLE"

Scenario: Check availability - out of stock item
  When I POST a REST request to URL "/q/check-availability" with payload
  """
  {
    "filters": {
      "productId": "prod-3",
      "variantId": "var-3"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.availableQuantity" is "0"
  And the REST response key "list[0].row.status" is "OUT_OF_STOCK"
  And the REST response key "list[0].row.stateId" is "OUT_OF_STOCK"

Scenario: Check availability by productId only - returns all variants for prod-1
  When I POST a REST request to URL "/q/check-availability" with payload
  """
  {
    "filters": {
      "productId": "prod-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Check availability - damaged item shows zero available
  When I POST a REST request to URL "/q/check-availability" with payload
  """
  {
    "filters": {
      "productId": "prod-4",
      "variantId": "var-5"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.availableQuantity" is "0"
  And the REST response key "list[0].row.stateId" is "DAMAGED_AT_WAREHOUSE"

Scenario: Check availability - non-existent product and variant
  When I POST a REST request to URL "/q/check-availability" with payload
  """
  {
    "filters": {
      "productId": "prod-999",
      "variantId": "var-999"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════════
# INVENTORY-STATE-COUNTS — dashboard state distribution
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Get inventory state counts - 6 distinct states
  When I POST a REST request to URL "/q/inventory-state-counts" with payload
  """
  {}
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "6"

# ═══════════════════════════════════════════════════════════════════════════
# LOW-STOCK — items where avail > 0 AND avail <= threshold AND IN_WAREHOUSE
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Low stock query returns only inv-4
  When I POST a REST request to URL "/q/low-stock" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-4"
  And the REST response key "list[0].row.sku" is "SKU-WH-002"
  And the REST response key "list[0].row.availableQuantity" is "5"
  And the REST response key "list[0].row.lowStockThreshold" is "20"
  And the REST response key "list[0].row.stateId" is "IN_WAREHOUSE"
  And the REST response key "list[0].row.primaryFc" is "FC-CHENNAI"

# ═══════════════════════════════════════════════════════════════════════════
# OUT-OF-STOCK — items where stateId = OUT_OF_STOCK
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Out of stock query returns only inv-3
  When I POST a REST request to URL "/q/out-of-stock" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "inv-3"
  And the REST response key "list[0].row.sku" is "SKU-BL-001"
  And the REST response key "list[0].row.availableQuantity" is "0"
  And the REST response key "list[0].row.status" is "OUT_OF_STOCK"
  And the REST response key "list[0].row.stateId" is "OUT_OF_STOCK"
  And the REST response key "list[0].row.primaryFc" is "FC-BANGALORE"

# ═══════════════════════════════════════════════════════════════════════════
# INVENTORY-BY-FC — grouped summary by fulfillment center
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Inventory by fulfillment center - 3 FCs ordered alphabetically
  When I POST a REST request to URL "/q/inventory-by-fc" with payload
  """
  {}
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.fulfillmentCenter" is "FC-BANGALORE"
  And the REST response key "list[0].row.itemCount" is "2"
  And the REST response key "list[1].row.fulfillmentCenter" is "FC-CHENNAI"
  And the REST response key "list[1].row.itemCount" is "3"
  And the REST response key "list[2].row.fulfillmentCenter" is "FC-MUMBAI"
  And the REST response key "list[2].row.itemCount" is "3"

# ═══════════════════════════════════════════════════════════════════════════
# DAMAGE-SUMMARY — damage records grouped by FC, type, status
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Damage summary - all FCs returns 2 groups
  When I POST a REST request to URL "/q/damage-summary" with payload
  """
  {}
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Damage summary filtered by primaryFc FC-CHENNAI
  When I POST a REST request to URL "/q/damage-summary" with payload
  """
  {
    "filters": {
      "primaryFc": "FC-CHENNAI"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.fulfillmentCenter" is "FC-CHENNAI"
  And the REST response key "list[0].row.damageType" is "DENT"
  And the REST response key "list[0].row.status" is "REPORTED"
  And the REST response key "list[0].row.count" is "1"

Scenario: Damage summary filtered by primaryFc FC-MUMBAI
  When I POST a REST request to URL "/q/damage-summary" with payload
  """
  {
    "filters": {
      "primaryFc": "FC-MUMBAI"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.fulfillmentCenter" is "FC-MUMBAI"
  And the REST response key "list[0].row.damageType" is "WATER_DAMAGE"
  And the REST response key "list[0].row.status" is "REPORTED"
  And the REST response key "list[0].row.count" is "2"

Scenario: Damage summary filtered by status REPORTED
  When I POST a REST request to URL "/q/damage-summary" with payload
  """
  {
    "filters": {
      "status": "REPORTED"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Damage summary filtered by FC with no damage records
  When I POST a REST request to URL "/q/damage-summary" with payload
  """
  {
    "filters": {
      "primaryFc": "FC-BANGALORE"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════════
# ACTIVE-RESERVATIONS — all currently reserved inventory
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Active reservations returns all 3 reserved entries
  When I POST a REST request to URL "/q/active-reservations" with payload
  """
  {}
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"

Scenario: Active reservations contain correct order and item details
  When I POST a REST request to URL "/q/active-reservations" with payload
  """
  {}
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.status" is "RESERVED"
  And the REST response key "list[1].row.status" is "RESERVED"
  And the REST response key "list[2].row.status" is "RESERVED"

# ═══════════════════════════════════════════════════════════════════════════
# RECENT-MOVEMENTS — stock movement history with filters
# ═══════════════════════════════════════════════════════════════════════════

Scenario: Recent movements returns all 3 movements
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {}
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"

Scenario: Recent movements filtered by type RECEIVE
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "type": "RECEIVE"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.type" is "RECEIVE"
  And the REST response key "list[0].row.inventoryItemId" is "inv-1"
  And the REST response key "list[0].row.quantity" is "100"
  And the REST response key "list[0].row.fulfillmentCenterId" is "FC-CHENNAI"
  And the REST response key "list[0].row.reason" is "Initial stock receipt"

Scenario: Recent movements filtered by type PICK
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "type": "PICK"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.type" is "PICK"
  And the REST response key "list[0].row.inventoryItemId" is "inv-1"
  And the REST response key "list[0].row.quantity" is "20"
  And the REST response key "list[0].row.referenceId" is "ord-1"

Scenario: Recent movements filtered by type DAMAGED
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "type": "DAMAGED"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.type" is "DAMAGED"
  And the REST response key "list[0].row.inventoryItemId" is "inv-5"
  And the REST response key "list[0].row.quantity" is "30"
  And the REST response key "list[0].row.fulfillmentCenterId" is "FC-MUMBAI"

Scenario: Recent movements filtered by inventoryItemId inv-1
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "inventoryItemId": "inv-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Recent movements filtered by fulfillmentCenterId FC-CHENNAI
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "fulfillmentCenterId": "FC-CHENNAI"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Recent movements filtered by fulfillmentCenterId FC-MUMBAI
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "fulfillmentCenterId": "FC-MUMBAI"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.type" is "DAMAGED"
  And the REST response key "list[0].row.inventoryItemId" is "inv-5"

Scenario: Recent movements combined filter type and inventoryItemId
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "type": "RECEIVE",
      "inventoryItemId": "inv-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.type" is "RECEIVE"
  And the REST response key "list[0].row.inventoryItemId" is "inv-1"

Scenario: Recent movements filter returns no results for non-existent type
  When I POST a REST request to URL "/q/recent-movements" with payload
  """
  {
    "filters": {
      "type": "TRANSFER"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "0"
