Feature: Cart Jobs Events — tests for SYSTEM events triggered by cart-jobs module.
  Events: flagUnavailable, refreshPricing, expireCoupon
  These are SYSTEM events published by cart-jobs translators/schedulers,
  consumed by cart-service @ChenilePubSub controller and routed through STM.

# ═══════════════════════════════════════════════════════════════════════════════
# SETUP: Create cart with items and coupon for all subsequent tests
# ═══════════════════════════════════════════════════════════════════════════════

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a cart for jobs event tests
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Cart Jobs Event Test Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "jobsCartId"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add first item — prod-1 var-1a
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Adding headphones black",
    "productId": "prod-1",
    "variantId": "var-1a",
    "productName": "Wireless Headphones",
    "quantity": 2,
    "unitPrice": 2499
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add second item — prod-5 var-5a
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Adding bluetooth speaker",
    "productId": "prod-5",
    "variantId": "var-5a",
    "productName": "Bluetooth Speaker",
    "quantity": 1,
    "unitPrice": 1999
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Apply coupon to cart
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Applying discount coupon",
    "couponCode": "SUMMER20"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# FLAG UNAVAILABLE: Mark a variant as out of stock (ACTIVE -> ACTIVE)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Flag variant as unavailable — stock depleted (ACTIVE -> ACTIVE)
Given that "event" equals "flagUnavailable"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Stock depleted for var-1a",
    "variantId": "var-1a"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${jobsCartId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# FLAG UNAVAILABLE BY PRODUCT: Discontinue all variants (ACTIVE -> ACTIVE)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Flag all variants of product as unavailable — product discontinued (ACTIVE -> ACTIVE)
Given that "event" equals "flagUnavailable"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Product prod-5 discontinued",
    "productId": "prod-5"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${jobsCartId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# REFRESH PRICING: External price change detected (ACTIVE -> ACTIVE)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Refresh pricing after external price change (ACTIVE -> ACTIVE)
Given that "event" equals "refreshPricing"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Price changed for variant in cart"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${jobsCartId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# EXPIRE COUPON: Coupon expired in Promo service (ACTIVE -> ACTIVE)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Expire coupon that exists in cart (ACTIVE -> ACTIVE)
Given that "event" equals "expireCoupon"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Coupon SUMMER20 expired",
    "couponCode": "SUMMER20"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${jobsCartId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Expire coupon that does NOT exist in cart — should still succeed (no-op)
Given that "event" equals "expireCoupon"
When I PATCH a REST request to URL "/cart/${jobsCartId}/${event}" with payload
"""json
{
    "comment": "Coupon NONEXISTENT expired",
    "couponCode": "NONEXISTENT"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${jobsCartId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# INVALID: Jobs events should NOT work on terminal states
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create and expire a cart for terminal state test
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Terminal State Test Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "terminalCartId"

Scenario: Expire the cart (ACTIVE -> EXPIRED)
Given that "event" equals "expire"
When I PATCH a REST request to URL "/cart/${terminalCartId}/${event}" with payload
"""json
{
    "comment": "Cart expired"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "EXPIRED"

Scenario: Flag unavailable on EXPIRED cart — should fail
Given that "event" equals "flagUnavailable"
When I PATCH a REST request to URL "/cart/${terminalCartId}/${event}" with payload
"""json
{
    "comment": "Stock depleted",
    "variantId": "var-1a"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
