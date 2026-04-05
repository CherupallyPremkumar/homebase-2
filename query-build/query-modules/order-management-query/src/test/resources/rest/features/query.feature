Feature: Order Management System Query - cross-BC joined queries for OMS dashboard

  Background:
    When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
    And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

  Scenario: Get OMS overview stats
    When I POST a REST request to URL "/q/oms-overview-stats" with payload
    """
    { "pageNum": 1, "numRowsInPage": 1 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.totalActiveOrders"
    And the REST response contains key "list[0].row.pendingOrders"
    And the REST response contains key "list[0].row.openReturns"
    And the REST response contains key "list[0].row.openTickets"

  Scenario: List orders with customer info
    When I POST a REST request to URL "/q/orders-with-customer" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.orderId"
    And the REST response contains key "list[0].row.customerFirstName"
    And the REST response contains key "list[0].row.orderState"

  Scenario: Get order full detail
    When I POST a REST request to URL "/q/order-full-detail" with payload
    """
    {
      "filters": {
        "orderId": "ord-1"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "list[0].row.orderId" is "ord-1"
    And the REST response contains key "list[0].row.customerEmail"
    And the REST response contains key "list[0].row.paymentStatus"

  Scenario: List order items with product details
    When I POST a REST request to URL "/q/order-items-with-product" with payload
    """
    {
      "filters": {
        "orderId": "ord-1"
      },
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.productName"
    And the REST response contains key "list[0].row.supplierName"

  Scenario: List returns with order detail
    When I POST a REST request to URL "/q/returns-with-order" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.returnId"
    And the REST response contains key "list[0].row.orderNumber"

  Scenario: List tickets with order context
    When I POST a REST request to URL "/q/tickets-with-order" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.ticketId"
    And the REST response contains key "list[0].row.priority"

  Scenario: Settlement with breakdown
    When I POST a REST request to URL "/q/settlement-with-breakdown" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.settlementId"
    And the REST response contains key "list[0].row.supplierName"

  Scenario: Order fulfillment status
    When I POST a REST request to URL "/q/order-fulfillment-status" with payload
    """
    { "pageNum": 1, "numRowsInPage": 10 }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.fulfillmentId"
    And the REST response contains key "list[0].row.warehouseName"

  Scenario: Order status timeline
    When I POST a REST request to URL "/q/order-status-timeline" with payload
    """
    {
      "filters": {
        "orderId": "ord-1"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.activityName"
    And the REST response contains key "list[0].row.activityTime"
