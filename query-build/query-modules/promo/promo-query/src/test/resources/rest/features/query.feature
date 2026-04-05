Feature: Tests the Promo Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Search all promos with pagination
  When I POST a REST request to URL "/q/promos" with payload
  """
  {
    "sortCriteria" :[
      {"name":"code","ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "6"

Scenario: Filter promos by state
  When I POST a REST request to URL "/q/promos" with payload
  """
  {
    "filters" :{
      "stateId": "ACTIVE"
    }
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter promos by discount type
  When I POST a REST request to URL "/q/promos" with payload
  """
  {
    "filters" :{
      "discountType": "FLAT"
    }
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "4"

Scenario: Get promo by code
  When I POST a REST request to URL "/q/promo" with payload
  """
  {
    "filters" :{
      "code": "FLAT200"
    }
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "list[0].row.code" is "FLAT200"
  And the REST response key "list[0].row.discountType" is "FLAT"
