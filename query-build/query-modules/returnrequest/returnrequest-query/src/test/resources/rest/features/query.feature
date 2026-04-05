Feature: Tests the Returnrequest Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all return requests with pagination
  When I POST a REST request to URL "/q/returnrequests" with payload
  """
  {
    "sortCriteria" :[
      {"name":"createdTime","ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "4"
  And the REST response key "list[0].row.id" is "rr-4"

Scenario: Filter by orderId and stateId
  When I POST a REST request to URL "/q/returnrequests" with payload
  """
  {
    "filters" :{
      "orderId": "ord-5",
      "stateId": "APPROVED"
    }
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "rr-1"
  And the REST response key "list[0].row.orderId" is "ord-5"
  And the REST response key "list[0].row.stateId" is "APPROVED"
