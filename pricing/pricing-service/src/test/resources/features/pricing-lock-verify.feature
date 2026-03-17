Feature: Pricing Phase 2 & 3 — Lock Price and Verify Price
  Phase 2: lockPrice() calculates + stores in Redis with TTL, returns lockToken.
  Phase 3: verifyPrice() confirms lock is valid + hash matches before payment.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Lock price returns token and hash
When I POST a REST request to URL "/pricing/_lock-price" with payload
"""json
{
  "cart": {
    "cartId": "cart-lock-001",
    "userId": "user-001",
    "currency": "INR",
    "items": [
      {
        "variantId": "var-1a",
        "productId": "prod-1",
        "quantity": 2,
        "basePrice": {"amount": 25000, "currency": "INR"},
        "sellerId": "seller-1"
      }
    ]
  }
}
"""
Then success is true
And the REST response contains key "subtotal"
And the REST response key "subtotal.amount" is "50000"
And the REST response contains key "lockToken"
And the REST response contains key "breakdownHash"
And store "$.payload.lockToken" from response to "lockToken"
And store "$.payload.breakdownHash" from response to "breakdownHash"

Scenario: Verify locked price succeeds with valid token and hash
When I POST a REST request to URL "/pricing/_verify-price" with payload
"""json
{
  "lockToken": "${lockToken}",
  "breakdownHash": "${breakdownHash}"
}
"""
Then success is true
And the REST response key "valid" is "true"
And the REST response key "subtotal.amount" is "50000"

Scenario: Verify fails with non-existent lock token
When I POST a REST request to URL "/pricing/_verify-price" with payload
"""json
{
  "lockToken": "non-existent-token-xyz",
  "breakdownHash": "some-hash"
}
"""
Then success is true
And the REST response key "valid" is "false"
And the REST response key "reason" is "Price lock not found or expired"

Scenario: Verify fails with missing lock token
When I POST a REST request to URL "/pricing/_verify-price" with payload
"""json
{
  "breakdownHash": "some-hash"
}
"""
Then success is true
And the REST response key "valid" is "false"
And the REST response key "reason" is "Lock token is required"

Scenario: Verify fails with tampered hash
When I POST a REST request to URL "/pricing/_verify-price" with payload
"""json
{
  "lockToken": "${lockToken}",
  "breakdownHash": "tampered-hash-value"
}
"""
Then success is true
And the REST response key "valid" is "false"
And the REST response key "reason" is "Price integrity check failed — hash mismatch"

Scenario: Lock rejects invalid cart
When I POST a REST request to URL "/pricing/_lock-price" with payload
"""json
{
  "cart": {
    "cartId": "cart-lock-bad",
    "userId": "user-001",
    "currency": "INR",
    "items": []
  }
}
"""
Then success is true
And the REST response key "error" is "true"
And the REST response key "message" is "Cart must have at least one item"
