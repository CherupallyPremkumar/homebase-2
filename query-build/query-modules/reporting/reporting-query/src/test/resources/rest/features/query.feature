Feature: Reporting Query -- admin views report definitions, schedules, and history

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all report definitions
  When I POST a REST request to URL "/q/reportDefinitions" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "4"

Scenario: Filter report definitions by category
  When I POST a REST request to URL "/q/reportDefinitions" with payload
  """
  { "filters": { "category": "SALES" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter report definitions by active status
  When I POST a REST request to URL "/q/reportDefinitions" with payload
  """
  { "filters": { "isActive": true }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "4"

Scenario: List all scheduled reports
  When I POST a REST request to URL "/q/scheduledReports" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter scheduled reports by status
  When I POST a REST request to URL "/q/scheduledReports" with payload
  """
  { "filters": { "status": "ACTIVE" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: List all report history
  When I POST a REST request to URL "/q/reportHistory" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "6"

Scenario: Filter report history by status
  When I POST a REST request to URL "/q/reportHistory" with payload
  """
  { "filters": { "status": "COMPLETED" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "4"

Scenario: Filter report history by generatedBy
  When I POST a REST request to URL "/q/reportHistory" with payload
  """
  { "filters": { "generatedBy": "system" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "3"
