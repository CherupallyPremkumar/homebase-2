Feature: Wishlist Query -- customer and admin view wishlist items

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all wishlist items
  When I POST a REST request to URL "/q/wishlistItems" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter wishlist items by userId
  When I POST a REST request to URL "/q/wishlistItems" with payload
  """
  { "filters": { "userId": "user-1" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter wishlist items by productId
  When I POST a REST request to URL "/q/wishlistItems" with payload
  """
  { "filters": { "productId": "prod-2" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"
