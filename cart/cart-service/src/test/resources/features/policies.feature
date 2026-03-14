Feature: Cart Policy Enforcement
  Tests that the cart correctly enforces enterprise policies like Multi-Seller restrictions,
  item limits, quantity limits, and currency consistency.

  Background:
    Given that "flowName" equals "cart-flow"

  Scenario: Disallow adding items from different sellers when multi-seller is disabled
    Given that "MultiSellerAllowed" is "false"
    And a cart exists for "user-123"
    And the cart contains an item from seller "seller-A"
    When I attempt to add an item from seller "seller-B" to the cart
    Then the request should fail with a "MultiSellerViolationException"
    And the message should be "Multi-seller cart is not allowed. Items must be from the same seller."

  Scenario: Allow adding items from the same seller when multi-seller is disabled
    Given that "MultiSellerAllowed" is "false"
    And a cart exists for "user-456"
    And the cart contains an item from seller "seller-A"
    When I attempt to add an item from seller "seller-A" to the cart
    Then the request should succeed

  Scenario: Enforce maximum unique items per cart
    Given that "MaxItemsPerCart" is "2"
    And a cart exists for "user-789"
    And the cart contains items:
      | productId | sellerId |
      | prod-1    | seller-A |
      | prod-2    | seller-A |
    When I attempt to add item "prod-3" from seller "seller-A" to the cart
    Then the request should fail with a "CartLimitExceededException"
    And the message should contain "Maximum allowed unique items: 2"

  Scenario: Enforce maximum quantity per item
    Given that "MaxQuantityPerItem" is "5"
    And a cart exists for "user-321"
    When I attempt to add item "prod-1" with quantity 10 to the cart
    Then the request should fail with a "QuantityLimitExceededException"
    And the message should contain "Maximum allowed: 5"
