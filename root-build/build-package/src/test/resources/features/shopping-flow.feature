Feature: End-to-end shopping flow
  As a customer on HomeBase
  I want to browse products, add to cart, checkout, and manage orders
  So that I can purchase handcrafted products

  Background:
    Given the system is running with tenant "homebase"
    And a product "Handmade Candle" exists with price 450.00 and status "ACTIVE"
    And inventory for "Handmade Candle" has quantity 10

  # --- Product Browse ---
  Scenario: Browse products via query API
    When I POST a REST request to URL "/q/products" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true

  Scenario: View product detail
    When I GET a REST request to URL "/product/{productId}"
    Then the http status code is 200
    And the REST response key "name" is "Handmade Candle"

  # --- Cart Flow ---
  Scenario: Create cart and add items
    When I POST a REST request to URL "/cart" with payload
    """
    {}
    """
    Then the http status code is 200
    And store "id" from response to "cartId"

    When I PATCH a REST request to URL "/cart/${cartId}/addItem" with payload
    """
    { "productId": "${productId}", "quantity": 2 }
    """
    Then the http status code is 200
    And the REST expression "items" size is 1

  Scenario: Update quantity in cart
    Given a cart exists with "Handmade Candle" quantity 2
    When I PATCH a REST request to URL "/cart/${cartId}/updateQuantity" with payload
    """
    { "itemId": "${itemId}", "quantity": 3 }
    """
    Then the http status code is 200

  Scenario: Remove item from cart
    Given a cart exists with "Handmade Candle" quantity 2
    When I PATCH a REST request to URL "/cart/${cartId}/removeItem" with payload
    """
    { "itemId": "${itemId}" }
    """
    Then the http status code is 200
    And the REST expression "items" size is 0

  Scenario: Apply promo code to cart
    Given a cart exists with "Handmade Candle" quantity 1
    And a promo code "WELCOME10" exists with 10 percent discount
    When I PATCH a REST request to URL "/cart/${cartId}/applyPromoCode" with payload
    """
    { "promoCode": "WELCOME10" }
    """
    Then the http status code is 200
    And the REST response key "appliedPromoCode" is "WELCOME10"

  # --- Checkout Flow ---
  Scenario: Add delivery address and initiate checkout
    Given a cart exists with "Handmade Candle" quantity 2
    When I PATCH a REST request to URL "/cart/${cartId}/addDeliveryAddress" with payload
    """
    {
      "line1": "123 Main St",
      "city": "Chennai",
      "state": "Tamil Nadu",
      "postalCode": "600001",
      "country": "IN",
      "phone": "9876543210"
    }
    """
    Then the http status code is 200

    When I PATCH a REST request to URL "/cart/${cartId}/initiateCheckout" with payload
    """
    {}
    """
    Then the http status code is 200
    And the REST response key "stateId" is "CHECKOUT_IN_PROGRESS"

  # --- Order Flow ---
  Scenario: View order after payment confirmation
    Given an order exists for "Handmade Candle" with status "PAYMENT_CONFIRMED"
    When I GET a REST request to URL "/order/${orderId}"
    Then the http status code is 200
    And the REST response key "status" is "PAYMENT_CONFIRMED"

  Scenario: Cancel order within cancellation window
    Given an order exists for "Handmade Candle" with status "CREATED"
    When I PATCH a REST request to URL "/order/${orderId}/cancelOrder" with payload
    """
    { "reason": "Changed my mind" }
    """
    Then the http status code is 200
    And the REST response key "status" is "CANCELLED"

  # --- Event Chain Verification ---
  Scenario: Product published triggers inventory initialization
    When a product is published via event
    Then inventory record is created with quantity 0

  Scenario: Cart checkout triggers inventory reservation
    Given a cart with "Handmade Candle" quantity 2
    When checkout is initiated
    Then inventory reserved quantity increases by 2

  Scenario: Payment success triggers order confirmation and shipping
    Given an order in CREATED state
    When payment succeeded event is received
    Then order transitions to PAYMENT_CONFIRMED
    And a shipment record is created

  Scenario: Order cancellation releases inventory
    Given an order with reserved inventory
    When order is cancelled
    Then inventory reserved quantity decreases
    And inventory available quantity increases

  # --- Query Verification ---
  Scenario: Query orders with pagination
    Given 5 orders exist for the current user
    When I POST a REST request to URL "/q/orders" with payload
    """
    { "pageNum": 0, "numRowsInPage": 3 }
    """
    Then the http status code is 200
