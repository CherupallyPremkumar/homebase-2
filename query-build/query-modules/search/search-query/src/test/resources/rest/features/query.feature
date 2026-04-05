Feature: Tests the Search Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Search all products with pagination
  When I POST a REST request to URL "/q/productSearch" with payload
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

Scenario: Search products by keyword
  When I POST a REST request to URL "/q/productSearch" with payload
  """
  {
    "filters" :{
      "keyword": "Silk"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.name" is "Banarasi Silk Saree"

Scenario: Search products by brand
  When I POST a REST request to URL "/q/productSearch" with payload
  """
  {
    "filters" :{
      "brand": "EcoThread"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.name" is "Organic Cotton Shirt"

Scenario: Search products by price range
  When I POST a REST request to URL "/q/productSearch" with payload
  """
  {
    "filters" :{
      "minPrice": "1000",
      "maxPrice": "3000"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "2"

Scenario: Search products in stock only
  When I POST a REST request to URL "/q/productSearch" with payload
  """
  {
    "filters" :{
      "inStock": "true"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "4"

Scenario: Search suggestions by keyword
  When I POST a REST request to URL "/q/searchSuggestions" with payload
  """
  {
    "filters" :{
      "keyword": "Cotton"
    },
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
  And the REST response key "numRowsReturned" is "1"
  And the REST response key "list[0].row.name" is "Organic Cotton Shirt"

Scenario: Search suggestions returns all when no keyword
  When I POST a REST request to URL "/q/searchSuggestions" with payload
  """
  {
    "pageNum": 1,
    "numRowsInPage": 10
  }
  """
  Then the http status code is 200
  And the top level code is 200
  And success is true
