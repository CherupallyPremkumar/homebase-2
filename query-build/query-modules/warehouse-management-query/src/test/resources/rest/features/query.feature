Feature: Warehouse Management System Query - cross-BC joined queries for WMS dashboard

  Background:
    When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
    And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

  Scenario: Get WMS overview stats
    When I POST a REST request to URL "/q/wms-overview-stats" with payload
    """
    { "pageNum": 1, "numRowsInPage": 1 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.activeWarehouses"
    And the REST response contains key "list[0].row.totalSkus"
    And the REST response contains key "list[0].row.lowStockItems"
    And the REST response contains key "list[0].row.pendingFulfillments"

  Scenario: List inventory with product info
    When I POST a REST request to URL "/q/inventory-with-product" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.inventoryId"
    And the REST response contains key "list[0].row.productName"
    And the REST response contains key "list[0].row.warehouseName"

  Scenario: Fulfillment with order detail
    When I POST a REST request to URL "/q/fulfillment-with-order" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.fulfillmentId"
    And the REST response contains key "list[0].row.orderNumber"
    And the REST response contains key "list[0].row.warehouseName"

  Scenario: Pick list with product details
    When I POST a REST request to URL "/q/pick-list-with-product" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.pickListId"
    And the REST response contains key "list[0].row.warehouseName"

  Scenario: Warehouse utilization overview
    When I POST a REST request to URL "/q/warehouse-utilization" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.warehouseId"
    And the REST response contains key "list[0].row.warehouseName"
    And the REST response contains key "list[0].row.totalSkus"

  Scenario: Inventory by warehouse location
    When I POST a REST request to URL "/q/inventory-by-location" with payload
    """
    {
      "filters": {
        "warehouseId": "wh-1"
      },
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.locationCode"
    And the REST response contains key "list[0].row.productName"

  Scenario: Fulfillment line items for an order
    When I POST a REST request to URL "/q/fulfillment-line-items" with payload
    """
    {
      "filters": {
        "fulfillmentOrderId": "fo-1"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.sku"
    And the REST response contains key "list[0].row.productName"

  Scenario: Inbound shipments
    When I POST a REST request to URL "/q/inbound-shipments" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.sku"
    And the REST response contains key "list[0].row.movementType"
