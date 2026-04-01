Feature: Rules Engine Query Service — admin dashboard queries

  Background:
    When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
    And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

  Scenario: List all rule sets
    When I POST a REST request to URL "/q/rulesets" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 20
    }
    """
    Then the http status code is 200
    And success is true

  Scenario: Filter rule sets by target module
    When I POST a REST request to URL "/q/rulesets" with payload
    """
    {
      "filters": {
        "targetModule": "cart"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "1"

  Scenario: Filter rule sets by name (like query)
    When I POST a REST request to URL "/q/rulesets" with payload
    """
    {
      "filters": {
        "name": "Fraud"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "1"

  Scenario: Get rules for a specific rule set
    When I POST a REST request to URL "/q/rulesByRuleset" with payload
    """
    {
      "filters": {
        "policyId": "policy-cart-limits"
      }
    }
    """
    Then the http status code is 200
    And success is true

  Scenario: List decision audit log
    When I POST a REST request to URL "/q/decisionAudit" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
