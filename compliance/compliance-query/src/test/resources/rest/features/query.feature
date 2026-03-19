Feature: Compliance Query Service — admin dashboard queries

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all platform policies
  When I POST a REST request to URL "/q/platformPolicies" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "6"

Scenario: Filter platform policies by state — PUBLISHED only
  When I POST a REST request to URL "/q/platformPolicies" with payload
  """
  {
    "filters": {
      "stateId": ["PUBLISHED"]
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"

Scenario: Filter platform policies by title like query
  When I POST a REST request to URL "/q/platformPolicies" with payload
  """
  {
    "filters": {
      "title": "%Return%"
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.title" is "Return & Refund Policy"

Scenario: List all agreements
  When I POST a REST request to URL "/q/agreements" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"

Scenario: Filter agreements by type — SELLER_AGREEMENT
  When I POST a REST request to URL "/q/agreements" with payload
  """
  {
    "filters": {
      "agreementType": "SELLER_AGREEMENT"
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter agreements by state — PUBLISHED only
  When I POST a REST request to URL "/q/agreements" with payload
  """
  {
    "filters": {
      "stateId": ["PUBLISHED"]
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"

Scenario: List all regulations
  When I POST a REST request to URL "/q/regulations" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "6"

Scenario: Filter regulations by category — DATA_PRIVACY
  When I POST a REST request to URL "/q/regulations" with payload
  """
  {
    "filters": {
      "category": "DATA_PRIVACY"
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter regulations by jurisdiction
  When I POST a REST request to URL "/q/regulations" with payload
  """
  {
    "filters": {
      "jurisdiction": "INDIA"
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "6"

Scenario: Filter active regulations only
  When I POST a REST request to URL "/q/regulations" with payload
  """
  {
    "filters": {
      "isActive": true
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "6"

Scenario: List all agreement acceptances
  When I POST a REST request to URL "/q/agreementAcceptances" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "4"

Scenario: Filter acceptances by user type — SUPPLIER
  When I POST a REST request to URL "/q/agreementAcceptances" with payload
  """
  {
    "filters": {
      "userType": "SUPPLIER"
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter acceptances by agreement ID
  When I POST a REST request to URL "/q/agreementAcceptances" with payload
  """
  {
    "filters": {
      "agreementId": "agree-seller-v3"
    },
    "pageNum": 1,
    "numRowsInPage": 20
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"
