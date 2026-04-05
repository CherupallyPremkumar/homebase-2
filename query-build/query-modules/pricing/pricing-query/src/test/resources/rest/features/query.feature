Feature: Pricing Query

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List pricing rules
  When I POST a REST request to URL "/q/pricingRules" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
