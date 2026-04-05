Feature: Tests the Platform Dashboard Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Get latest platform KPI snapshot
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "platformKpi",
    "pageNum": 1,
    "numRowsInPage": 5,
    "sortCriteria": [{"name": "snapshotDate", "ascendingOrder": false}]
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"

Scenario: Get live order pipeline
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "liveOrderPipeline"
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.placedCount" is "45"
  And the REST response key "list[0].row.deliveredCount" is "20"

Scenario: Get top products today with pagination
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "topProductsToday",
    "pageNum": 1,
    "numRowsInPage": 5
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"
  And the REST response key "list[0].row.rank" is "1"

Scenario: Get top sellers today
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "topSellersToday",
    "pageNum": 1,
    "numRowsInPage": 5
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"
  And the REST response key "list[0].row.rank" is "1"
  And the REST response key "list[0].row.tier" is "PLATINUM"

Scenario: Get active platform alerts
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "platformAlerts",
    "filters": {"is_active": true}
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"

Scenario: Filter alerts by severity P0
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "platformAlerts",
    "filters": {"severity": "P0"}
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.severity" is "P0"
  And the REST response key "list[0].row.alertType" is "PAYMENT_GATEWAY"

Scenario: Get customer health cohort metrics
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "customerHealth",
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "9"

Scenario: Filter customer health by cohort month
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "customerHealth",
    "filters": {"cohortMonth": "2026-01-01"}
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"
  And the REST response key "list[0].row.cohortSize" is "1200"

Scenario: Get category performance for date range
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "categoryPerformance",
    "filters": {"startDate": "2026-03-25", "endDate": "2026-03-31"},
    "pageNum": 1,
    "numRowsInPage": 50
  }
  """
  Then the http status code is 200
  And success is true

Scenario: Get conversion funnel
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "conversionFunnel",
    "pageNum": 1,
    "numRowsInPage": 7
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "7"

Scenario: Get seller health summary
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "sellerHealthSummary",
    "pageNum": 1,
    "numRowsInPage": 7
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "7"

Scenario: Get revenue by region
  When I POST a REST request to URL "/q/platform-dashboard" with payload
  """
  {
    "queryName": "revenueByRegion",
    "pageNum": 1,
    "numRowsInPage": 50
  }
  """
  Then the http status code is 200
  And success is true
