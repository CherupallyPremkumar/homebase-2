Feature: Tests the Dashboard Query Service using a REST client.

  Scenario: Dashboard overview stats returns KPIs
    When I POST a REST request to URL "/q/overview-stats" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.totalOrders"
    And the REST response contains key "list[0].row.totalRevenue"
    And the REST response contains key "list[0].row.publishedProducts"
    And the REST response contains key "list[0].row.activeSuppliers"

  Scenario: Dashboard orders by state returns grouped counts
    When I POST a REST request to URL "/q/orders-by-state" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.state"
    And the REST response contains key "list[0].row.count"

  Scenario: Dashboard recent orders returns paginated results
    When I POST a REST request to URL "/q/recent-orders" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.orderId"
    And the REST response contains key "list[0].row.orderState"
    And the REST response contains key "list[0].row.totalAmount"

  Scenario: Dashboard low stock alerts returns inventory warnings
    When I POST a REST request to URL "/q/low-stock-alerts" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "numRowsReturned"

  Scenario: Dashboard open support tickets ordered by priority
    When I POST a REST request to URL "/q/open-tickets" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "list[0].row.priority" is "CRITICAL"

  Scenario: Dashboard returns by state
    When I POST a REST request to URL "/q/returns-by-state" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.state"
    And the REST response contains key "list[0].row.count"

  Scenario: Dashboard supplier performance with pagination
    When I POST a REST request to URL "/q/supplier-performance" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.supplierId"
    And the REST response contains key "list[0].row.businessName"

  Scenario: Dashboard out of stock products
    When I POST a REST request to URL "/q/out-of-stock" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "numRowsReturned"
