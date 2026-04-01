Feature: Tax Query — admin views tax rates, categories, regions, exemptions

  Test data: 5 GST rates (0%, 5%, 12%, 18%, 28%), 4 category mappings,
  3 regions (Karnataka, Maharashtra, Delhi), 2 exemptions

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all tax rates
  When I POST a REST request to URL "/q/taxRates" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "5"

Scenario: Filter tax rates by HSN code
  When I POST a REST request to URL "/q/taxRates" with payload
  """
  { "filters": { "hsnCode": "8471" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: Filter active tax rates only
  When I POST a REST request to URL "/q/taxRates" with payload
  """
  { "filters": { "active": true }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "5"

Scenario: List category mappings
  When I POST a REST request to URL "/q/taxCategoryMappings" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "4"

Scenario: Filter category by product category
  When I POST a REST request to URL "/q/taxCategoryMappings" with payload
  """
  { "filters": { "productCategory": "ELECTRONICS" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: List all tax regions
  When I POST a REST request to URL "/q/taxRegions" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "3"

Scenario: Filter union territories
  When I POST a REST request to URL "/q/taxRegions" with payload
  """
  { "filters": { "unionTerritory": true }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: List exemptions
  When I POST a REST request to URL "/q/taxExemptions" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"
