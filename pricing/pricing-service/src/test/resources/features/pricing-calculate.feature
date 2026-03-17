Feature: Pricing Phase 1 — Calculate Price
  Stateless price calculation. Cart snapshot in → PriceBreakdown out.
  Tests: base price resolution, discount cap, tax, validation errors.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Calculate price for a single item cart
When I POST a REST request to URL "/pricing/_calculate" with payload
"""json
{
  "cart": {
    "cartId": "cart-001",
    "userId": "user-001",
    "currency": "INR",
    "items": [
      {
        "variantId": "var-1a",
        "productId": "prod-1",
        "quantity": 1,
        "basePrice": {"amount": 49900, "currency": "INR"},
        "sellerId": "seller-1"
      }
    ]
  }
}
"""
Then success is true
And the REST response key "subtotal.amount" is "49900"
And the REST response contains key "finalTotal"
And the REST response contains key "breakdownHash"

Scenario: Calculate price for multi-item cart
When I POST a REST request to URL "/pricing/_calculate" with payload
"""json
{
  "cart": {
    "cartId": "cart-002",
    "userId": "user-001",
    "currency": "INR",
    "items": [
      {
        "variantId": "var-1a",
        "productId": "prod-1",
        "quantity": 2,
        "basePrice": {"amount": 10000, "currency": "INR"},
        "sellerId": "seller-1"
      },
      {
        "variantId": "var-2a",
        "productId": "prod-2",
        "quantity": 3,
        "basePrice": {"amount": 5000, "currency": "INR"},
        "sellerId": "seller-2"
      }
    ]
  }
}
"""
Then success is true
And the REST response key "subtotal.amount" is "35000"

Scenario: Reject request with no cart
When I POST a REST request to URL "/pricing/_calculate" with payload
"""json
{
  "couponCode": "SAVE10"
}
"""
Then success is true
And the REST response key "error" is "true"
And the REST response key "message" is "Cart snapshot must not be null"

Scenario: Reject request with empty items
When I POST a REST request to URL "/pricing/_calculate" with payload
"""json
{
  "cart": {
    "cartId": "cart-003",
    "userId": "user-001",
    "currency": "INR",
    "items": []
  }
}
"""
Then success is true
And the REST response key "error" is "true"
And the REST response key "message" is "Cart must have at least one item"

Scenario: Reject request with missing currency
When I POST a REST request to URL "/pricing/_calculate" with payload
"""json
{
  "cart": {
    "cartId": "cart-004",
    "userId": "user-001",
    "items": [
      {
        "variantId": "var-1a",
        "productId": "prod-1",
        "quantity": 1,
        "basePrice": {"amount": 10000, "currency": "INR"},
        "sellerId": "seller-1"
      }
    ]
  }
}
"""
Then success is true
And the REST response key "error" is "true"
And the REST response key "message" is "Currency must be specified"

Scenario: Reject item with zero quantity
When I POST a REST request to URL "/pricing/_calculate" with payload
"""json
{
  "cart": {
    "cartId": "cart-005",
    "userId": "user-001",
    "currency": "INR",
    "items": [
      {
        "variantId": "var-1a",
        "productId": "prod-1",
        "quantity": 0,
        "basePrice": {"amount": 10000, "currency": "INR"},
        "sellerId": "seller-1"
      }
    ]
  }
}
"""
Then success is true
And the REST response key "error" is "true"
And the REST response key "message" is "Item var-1a must have quantity > 0"
