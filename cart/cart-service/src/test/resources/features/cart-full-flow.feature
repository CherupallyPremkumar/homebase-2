Feature: Cart Full Flow — comprehensive tests for the cart state machine.
  Tests adding multiple items, removing items, updating quantities, promo codes,
  delivery addresses, and the full checkout-to-conversion flow.
  States: CREATED -> ACTIVE -> LOCKED -> RESERVED -> PAYMENT_PENDING -> CONVERTED

# ═══════════════════════════════════════════════════════════════════════════════
# FULL FLOW: Create -> Add Items -> Promo -> Address -> Checkout -> Pay
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a new cart in CREATED state
Given that "flowName" equals "cart-flow"
And that "initialState" equals "CREATED"
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
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Add first item to cart (CREATED -> ACTIVE)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding silk scarf",
    "productId": "prod-001",
    "productName": "Handmade Silk Scarf",
    "quantity": 2,
    "unitPrice": 1500
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add second item to cart (ACTIVE -> ACTIVE)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding tote bag",
    "productId": "prod-002",
    "productName": "Cotton Tote Bag",
    "quantity": 1,
    "unitPrice": 800
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add third item to cart (ACTIVE -> ACTIVE)
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding ceramic vase",
    "productId": "prod-003",
    "productName": "Ceramic Vase",
    "quantity": 1,
    "unitPrice": 1200
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
    "comment": "Increasing scarf quantity",
    "productId": "prod-001",
    "quantity": 3
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Remove the third item (ACTIVE -> ACTIVE)
Given that "event" equals "removeItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Changed mind about the vase",
    "productId": "prod-003"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Apply promo code (ACTIVE -> ACTIVE)
Given that "event" equals "applyPromoCode"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Applying welcome discount",
    "promoCode": "WELCOME10"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add delivery address (ACTIVE -> ACTIVE)
Given that "event" equals "addDeliveryAddress"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding shipping address",
    "addressLine1": "42 Marina Beach Road",
    "city": "Chennai",
    "state": "Tamil Nadu",
    "pinCode": "600001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Verify cart state after all modifications
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Initiate checkout (ACTIVE -> LOCKED)
Given that "event" equals "initiateCheckout"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Proceeding to checkout"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "LOCKED"

Scenario: Reserve inventory (LOCKED -> RESERVED)
Given that "event" equals "reserveInventory"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Inventory reserved for checkout"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RESERVED"

Scenario: Create payment session (RESERVED -> PAYMENT_PENDING)
Given that "event" equals "createPaymentSession"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Payment session created",
    "gatewaySessionId": "cs_stripe_abc123"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_PENDING"

Scenario: Payment succeeds (PAYMENT_PENDING -> CONVERTED)
Given that "event" equals "paymentSuccess"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Payment confirmed by Stripe webhook",
    "paymentId": "pi_stripe_xyz789",
    "amountPaid": 5300
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CONVERTED"

Scenario: Verify converted cart is retrievable
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CONVERTED"

# ═══════════════════════════════════════════════════════════════════════════════
# ABANDON FLOW: Create -> Add -> Session Timeout -> Recover -> Checkout
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
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Add item to abandon cart
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${abandonId}/${event}" with payload
"""json
{
    "comment": "Adding item",
    "productId": "prod-004",
    "quantity": 1,
    "unitPrice": 500
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Session timeout triggers abandonment (ACTIVE -> ABANDONED)
Given that "event" equals "sessionTimeout"
When I PATCH a REST request to URL "/cart/${abandonId}/${event}" with payload
"""json
{
    "comment": "User inactive for 30 minutes"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${abandonId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ABANDONED"

Scenario: Recover abandoned cart (ABANDONED -> ACTIVE)
Given that "event" equals "recoverCart"
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
# PAYMENT FAILURE FLOW: Active -> Checkout -> Reserve -> Pay -> Fail -> Active
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a third cart for payment failure flow
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Payment Fail Cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failId"

Scenario: Add item to payment fail cart
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Adding item",
    "productId": "prod-005",
    "quantity": 1,
    "unitPrice": 900
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Checkout and reserve payment fail cart
Given that "event" equals "initiateCheckout"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Checkout"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "LOCKED"

Scenario: Reserve inventory for payment fail cart
Given that "event" equals "reserveInventory"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Reserved"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "RESERVED"

Scenario: Create payment session for payment fail cart
Given that "event" equals "createPaymentSession"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Session created"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PAYMENT_PENDING"

Scenario: Payment fails (PAYMENT_PENDING -> ACTIVE)
Given that "event" equals "paymentFailed"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Card declined by issuing bank"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# WISHLIST FLOW: ACTIVE -> SAVED -> ACTIVE
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Move cart to wishlist (ACTIVE -> SAVED)
Given that "event" equals "moveToWishlist"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Saving for later"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SAVED"

Scenario: Move back from wishlist to cart (SAVED -> ACTIVE)
Given that "event" equals "moveToCart"
When I PATCH a REST request to URL "/cart/${failId}/${event}" with payload
"""json
{
    "comment": "Ready to buy now"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# INVALID: Wrong event on CONVERTED (terminal state)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Attempt to add item to a CONVERTED cart — should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Trying to add to converted cart",
    "productId": "prod-099",
    "quantity": 1
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
