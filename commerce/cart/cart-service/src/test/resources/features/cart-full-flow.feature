Feature: Cart Full Flow — comprehensive tests for the refactored cart state machine.
  States: ACTIVE -> CHECKOUT_INITIATED -> CHECKOUT_COMPLETED
  Also: ACTIVE -> ABANDONED, ACTIVE -> EXPIRED, merge flow
  Uses REAL product-query (variant-exists) and inventory-query (check-availability) integrations.
  Test data: prod-1 Wireless Headphones (PUBLISHED, var-1a/var-1b), prod-2 Cotton T-Shirt (PUBLISHED, var-2a),
             prod-5 Bluetooth Speaker (PUBLISHED, var-5a), prod-4 Smartphone (DRAFT — NOT sellable)

# ═══════════════════════════════════════════════════════════════════════════════
# FULL FLOW: Create -> Add Items -> Coupon -> Checkout -> Complete
# ═══════════════════════════════════════════════════════════════════════════════

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new cart in ACTIVE state
Given that "flowName" equals "cart-flow"
And that "initialState" equals "ACTIVE"
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Shopping Cart for Prem"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.description" is "Shopping Cart for Prem"

Scenario: Retrieve the empty cart
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add first item to cart — prod-1 var-1a (PUBLISHED, inventory available)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding wireless headphones black",
    "productId": "prod-1",
    "variantId": "var-1a",
    "productName": "Wireless Headphones",
    "quantity": 2,
    "unitPrice": 2499
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add second item to cart — prod-2 var-2a (PUBLISHED, inventory available)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding cotton t-shirt",
    "productId": "prod-2",
    "variantId": "var-2a",
    "productName": "Cotton T-Shirt",
    "quantity": 1,
    "unitPrice": 599
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add third item — same product different variant (prod-1 var-1b)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding wireless headphones white",
    "productId": "prod-1",
    "variantId": "var-1b",
    "productName": "Wireless Headphones",
    "quantity": 1,
    "unitPrice": 2499
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Update quantity of first item (ACTIVE -> ACTIVE)
Given that "event" equals "updateQuantity"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Increasing headphones quantity",
    "variantId": "var-1a",
    "quantity": 3
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Remove second item (ACTIVE -> ACTIVE)
Given that "event" equals "removeItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Changed mind about t-shirt",
    "variantId": "var-2a"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Apply coupon (ACTIVE -> ACTIVE)
Given that "event" equals "applyCoupon"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Applying welcome discount",
    "couponCode": "WELCOME10"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Remove coupon (ACTIVE -> ACTIVE)
Given that "event" equals "removeCoupon"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Removing coupon",
    "couponCode": "WELCOME10"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Initiate checkout (ACTIVE -> CHECKOUT_INITIATED)
Given that "event" equals "initiateCheckout"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Proceeding to checkout"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CHECKOUT_INITIATED"

Scenario: Complete checkout (CHECKOUT_INITIATED -> CHECKOUT_COMPLETED)
Given that "event" equals "completeCheckout"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Order created from cart",
    "orderId": "order-abc-123"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CHECKOUT_COMPLETED"

Scenario: Verify completed cart is retrievable
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CHECKOUT_COMPLETED"

# ═══════════════════════════════════════════════════════════════════════════════
# VALIDATION: Add item for DRAFT product — variant-exists should reject
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a cart for validation tests
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Validation Test Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "valCartId"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add DRAFT product to cart — should fail (product not sellable)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${valCartId}/${event}" with payload
"""json
{
    "comment": "Trying to add draft product",
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
When I PATCH a REST request to URL "/cart/${valCartId}/${event}" with payload
"""json
{
    "comment": "Trying to add non-existent variant",
    "productId": "prod-1",
    "variantId": "var-nonexistent",
    "productName": "Fake Variant",
    "quantity": 1,
    "unitPrice": 100
}
"""
Then the REST response does not contain key "mutatedEntity"

# ═══════════════════════════════════════════════════════════════════════════════
# ABANDON FLOW: Create -> Add -> Abandon -> Reactivate
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a second cart for abandon flow
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Abandon Test Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "abandonId"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add item to abandon cart — prod-5 var-5a (PUBLISHED, available)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${abandonId}/${event}" with payload
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

Scenario: System abandons cart (ACTIVE -> ABANDONED)
Given that "event" equals "abandon"
When I PATCH a REST request to URL "/cart/${abandonId}/${event}" with payload
"""json
{
    "comment": "User inactive for 24 hours"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${abandonId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ABANDONED"

Scenario: Reactivate abandoned cart (ABANDONED -> ACTIVE)
Given that "event" equals "reactivate"
When I PATCH a REST request to URL "/cart/${abandonId}/${event}" with payload
"""json
{
    "comment": "User returned and wants to continue"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${abandonId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# MERGE FLOW: ACTIVE -> merge items from guest cart
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a guest cart to be merged
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Guest browsing cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "guestCartId"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add item to guest cart — prod-1 var-1a
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${guestCartId}/${event}" with payload
"""json
{
    "comment": "Guest adding headphones",
    "productId": "prod-1",
    "variantId": "var-1a",
    "productName": "Wireless Headphones",
    "quantity": 2,
    "unitPrice": 2499
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add second item to guest cart — prod-5 var-5a
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${guestCartId}/${event}" with payload
"""json
{
    "comment": "Guest adding speaker",
    "productId": "prod-5",
    "variantId": "var-5a",
    "productName": "Bluetooth Speaker",
    "quantity": 1,
    "unitPrice": 1999
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Merge guest cart into user cart by sourceCartId (ACTIVE -> ACTIVE)
Given that "event" equals "merge"
When I PATCH a REST request to URL "/cart/${abandonId}/${event}" with payload
"""json
{
    "comment": "Merging guest cart after login",
    "sourceCartId": "${guestCartId}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${abandonId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# INVALID: Wrong event on terminal state
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Attempt to add item to a CHECKOUT_COMPLETED cart — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Trying to add to completed cart",
    "productId": "prod-1",
    "variantId": "var-1a",
    "productName": "Invalid Item",
    "quantity": 1,
    "unitPrice": 100
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to cart — should fail
When I PATCH a REST request to URL "/cart/${id}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
