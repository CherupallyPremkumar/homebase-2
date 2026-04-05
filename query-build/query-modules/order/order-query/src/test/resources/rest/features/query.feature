Feature: Tests the Order Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Tests out pagination capability
  When I POST a REST request to URL "/q/orders" with payload
  """
  {
    "sortCriteria" :[
      {"name":"createdAt","ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 5
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"
  And the REST response key "list[0].row.id" is "ord-5"
  And the REST response key "list[1].row.id" is "ord-8"

Scenario: Test filter by customerId and stateId
  When I POST a REST request to URL "/q/orders" with payload
  """
  {
    "filters" :{
      "customerId": "user-3",
      "stateId": "PAID"
    }
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "ord-3"
  And the REST response key "list[0].row.customerId" is "user-3"
  And the REST response key "list[0].row.stateId" is "PAID"

Scenario: Get order by id
  When I POST a REST request to URL "/q/order" with payload
  """
  {
    "filters" :{
      "id": "ord-1"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "list[0].row.id" is "ord-1"
  And the REST response key "list[0].row.customerId" is "user-1"
  And the REST response key "list[0].row.stateId" is "DELIVERED"

Scenario: Filter by stateId CANCELLED
  When I POST a REST request to URL "/q/orders" with payload
  """
  {
    "filters" :{
      "stateId": "CANCELLED"
    }
  }
  """
  Then the http status code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.id" is "ord-4"
  And the REST response key "list[0].row.stateId" is "CANCELLED"
