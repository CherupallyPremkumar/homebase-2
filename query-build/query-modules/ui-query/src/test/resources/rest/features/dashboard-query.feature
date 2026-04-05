Feature: Tests the Dashboard Query Service using a REST client.

  Background:
    When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
    And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

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
    And the REST response contains key "list[0].row.id"
    And the REST response contains key "list[0].row.stateId"
    And the REST response contains key "list[0].row.total"

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

  Scenario: Dashboard audit log returns recent audit entries
    When I POST a REST request to URL "/q/audit-log" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.entityType"
    And the REST response contains key "list[0].row.action"
    And the REST response contains key "list[0].row.changedBy"

  Scenario: Dashboard customer summary returns user counts by state
    When I POST a REST request to URL "/q/customer-summary" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.state"
    And the REST response contains key "list[0].row.count"

  Scenario: Dashboard revenue by category
    When I POST a REST request to URL "/q/revenue-by-category" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.categoryName"
    And the REST response contains key "list[0].row.totalRevenue"
