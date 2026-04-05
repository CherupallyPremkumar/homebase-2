Feature: Organisation Query — admin views organisation data

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all organisations
  When I POST a REST request to URL "/q/organisations" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: Filter by company name
  When I POST a REST request to URL "/q/organisations" with payload
  """
  { "filters": { "companyName": "HomeBase" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: Get organisation profile with all joins
  When I POST a REST request to URL "/q/organisationProfile" with payload
  """
  { "filters": { "id": "org-001" }, "pageNum": 1, "numRowsInPage": 1 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"
