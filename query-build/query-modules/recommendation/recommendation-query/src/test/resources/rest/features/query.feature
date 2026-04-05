Feature: Tests the Recommendation Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all user recommendations with pagination
  When I POST a REST request to URL "/q/userRecommendations" with payload
  """
  {
    "sortCriteria" :[
      {"name":"id","ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"

Scenario: Filter recommendations by userId
  When I POST a REST request to URL "/q/userRecommendations" with payload
  """
  {
    "filters" :{
      "userId": "user-001"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"

Scenario: Filter recommendations by type
  When I POST a REST request to URL "/q/userRecommendations" with payload
  """
  {
    "filters" :{
      "recommendationType": "SIMILAR"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter recommendations by userId and type
  When I POST a REST request to URL "/q/userRecommendations" with payload
  """
  {
    "filters" :{
      "userId": "user-001",
      "recommendationType": "PERSONALIZED"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.productId" is "prod-3"

Scenario: List all trending products
  When I POST a REST request to URL "/q/trendingProducts" with payload
  """
  {
    "sortCriteria" :[
      {"name":"id","ascendingOrder": true}
    ],
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "5"

Scenario: Filter trending products by category
  When I POST a REST request to URL "/q/trendingProducts" with payload
  """
  {
    "filters" :{
      "category": "Home Decor"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter trending products by time window
  When I POST a REST request to URL "/q/trendingProducts" with payload
  """
  {
    "filters" :{
      "timeWindow": "DAILY"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "3"

Scenario: Filter trending products by category and time window
  When I POST a REST request to URL "/q/trendingProducts" with payload
  """
  {
    "filters" :{
      "category": "Handmade Textiles",
      "timeWindow": "WEEKLY"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.productId" is "prod-1"
