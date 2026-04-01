Feature: Cart Validation — negative test cases for policy enforcement and input validation.
  Tests quantity limits, invalid variants, inventory checks, empty cart checkout,
  and state transition violations.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

# ═══════════════════════════════════════════════════════════════════════════════
# SETUP: Create cart and add a valid item
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a cart for validation tests
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Validation Test Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "vCartId"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add a valid item to cart for later tests
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Adding headphones",
    "productId": "prod-1",
    "variantId": "var-1a",
    "productName": "Wireless Headphones",
    "quantity": 2,
    "unitPrice": 2499
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# QUANTITY LIMIT: maxQuantityPerItem = 10 (from cart.json)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Add item with quantity exceeding max (11 > 10) — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Trying to add 11 items",
    "productId": "prod-5",
    "variantId": "var-5a",
    "productName": "Bluetooth Speaker",
    "quantity": 11,
    "unitPrice": 1999
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Update quantity to exceed max (11 > 10) — should fail
Given that "event" equals "updateQuantity"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Trying to set quantity to 11",
    "variantId": "var-1a",
    "quantity": 11
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Update quantity to zero — should fail
Given that "event" equals "updateQuantity"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Trying to set quantity to 0",
    "variantId": "var-1a",
    "quantity": 0
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Update quantity for non-existent variant — should fail
Given that "event" equals "updateQuantity"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Variant not in cart",
    "variantId": "var-nonexistent",
    "quantity": 2
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# VARIANT VALIDATION: product must be sellable, variant must exist
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Add item with missing variantId — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "No variantId",
    "productId": "prod-1",
    "productName": "No Variant",
    "quantity": 1,
    "unitPrice": 100
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Add DRAFT product (prod-4) — should fail (not sellable)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Draft product",
    "productId": "prod-4",
    "variantId": "var-4a",
    "productName": "Smartphone X200",
    "quantity": 1,
    "unitPrice": 15999
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Add non-existent variant — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Fake variant",
    "productId": "prod-1",
    "variantId": "var-fake-999",
    "productName": "Fake",
    "quantity": 1,
    "unitPrice": 100
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# REMOVE: non-existent variant
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Remove variant not in cart — should fail
Given that "event" equals "removeItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Not in cart",
    "variantId": "var-not-in-cart"
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# CHECKOUT: empty cart, min amount
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create empty cart for checkout test
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Empty Cart for Checkout"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "emptyCartId"

Scenario: Initiate checkout on empty cart — should fail
Given that "event" equals "initiateCheckout"
When I PATCH a REST request to URL "/cart/${emptyCartId}/${event}" with payload
"""json
{
    "comment": "Checkout empty cart"
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# STATE VIOLATIONS: wrong event on wrong state
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Cancel checkout on ACTIVE cart — should fail (not in CHECKOUT_INITIATED)
Given that "event" equals "cancelCheckout"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Cancel without initiating"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Complete checkout on ACTIVE cart — should fail
Given that "event" equals "completeCheckout"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Complete without initiating",
    "orderId": "order-fake"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Reactivate an ACTIVE cart — should fail (only ABANDONED can reactivate)
Given that "event" equals "reactivate"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Reactivate active cart"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

# ═══════════════════════════════════════════════════════════════════════════════
# NEGATIVE QUANTITY / PRICE on addItem
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Add item with zero quantity — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Zero quantity",
    "productId": "prod-1",
    "variantId": "var-1b",
    "productName": "Wireless Headphones White",
    "quantity": 0,
    "unitPrice": 2499
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Add item with negative quantity — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Negative quantity",
    "productId": "prod-1",
    "variantId": "var-1b",
    "productName": "Wireless Headphones White",
    "quantity": -3,
    "unitPrice": 2499
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Add item with zero unit price — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Zero price",
    "productId": "prod-1",
    "variantId": "var-1b",
    "productName": "Wireless Headphones White",
    "quantity": 1,
    "unitPrice": 0
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Add item with negative unit price — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Negative price",
    "productId": "prod-1",
    "variantId": "var-1b",
    "productName": "Wireless Headphones White",
    "quantity": 1,
    "unitPrice": -500
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Update quantity to negative — should fail
Given that "event" equals "updateQuantity"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Negative quantity",
    "variantId": "var-1a",
    "quantity": -5
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# COUPON RULES: duplicate, max coupons
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Apply coupon to validation cart
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Apply first coupon",
    "couponCode": "SAVE10"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Apply duplicate coupon — should fail
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Duplicate coupon",
    "couponCode": "SAVE10"
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Apply second coupon
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Second coupon",
    "couponCode": "WELCOME"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Apply third coupon
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Third coupon",
    "couponCode": "SUMMER20"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Apply fourth coupon — should fail (maxCouponsPerCart = 3)
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Fourth coupon exceeds limit",
    "couponCode": "EXTRA5"
}
"""
Then the REST response does not contain key "mutatedEntity"

Scenario: Apply coupon with empty code — should fail
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${vCartId}/${event}" with payload
"""json
{
    "comment": "Empty coupon code",
    "couponCode": ""
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# CHECKOUT: minimum amount
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create cart with very cheap item for min-amount test
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Cheap Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "cheapCartId"

Scenario: Add item below min checkout threshold (1 paisa)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${cheapCartId}/${event}" with payload
"""json
{
    "comment": "Dirt cheap item",
    "productId": "prod-1",
    "variantId": "var-1a",
    "productName": "Wireless Headphones",
    "quantity": 1,
    "unitPrice": 1
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Initiate checkout on cart below minimum amount (1 < 100) — should fail
Given that "event" equals "initiateCheckout"
When I PATCH a REST request to URL "/cart/${cheapCartId}/${event}" with payload
"""json
{
    "comment": "Cart total too low"
}
"""
Then the REST response does not contain key "mutatedEntity"
