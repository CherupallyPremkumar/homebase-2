Feature: HomeBase Platform Configuration (CConfig)
  Tests real HomeBase configuration keys used across all bounded contexts.
  Covers: cart limits, shipping thresholds, payment settings, promo rules,
  order policies, supplier policies, and feature flags.

  Background:
    When I construct a REST request with header "x-chenile-tenant-id" and value "homebase"

  # ================================================================
  # CART CONFIGURATION
  # ================================================================

  Scenario: Set cart max quantity per item
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "cart",
      "keyName": "maxQuantityPerItem",
      "avalue": "10",
      "customAttribute": "homebase"
    }
    """
    Then success is true
    And store "$.payload.id" from response to "cartConfigId1"

  Scenario: Set cart max items per cart
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "cart",
      "keyName": "maxItemsPerCart",
      "avalue": "50",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve cart max quantity
    When I GET a REST request to URL "/config/cart/maxQuantityPerItem"
    Then success is true
    And the REST response key "maxQuantityPerItem" is "10"

  Scenario: Retrieve cart max items
    When I GET a REST request to URL "/config/cart/maxItemsPerCart"
    Then success is true
    And the REST response key "maxItemsPerCart" is "50"

  # ================================================================
  # SHIPPING CONFIGURATION
  # ================================================================

  Scenario: Set free shipping threshold
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "shipping",
      "keyName": "freeShippingThreshold",
      "avalue": "499",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set default shipping cost
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "shipping",
      "keyName": "defaultShippingCost",
      "avalue": "49",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve free shipping threshold
    When I GET a REST request to URL "/config/shipping/freeShippingThreshold"
    Then success is true
    And the REST response key "freeShippingThreshold" is "499"

  Scenario: Retrieve default shipping cost
    When I GET a REST request to URL "/config/shipping/defaultShippingCost"
    Then success is true
    And the REST response key "defaultShippingCost" is "49"

  # ================================================================
  # PAYMENT CONFIGURATION
  # ================================================================

  Scenario: Set COD enabled flag
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "payment",
      "keyName": "codEnabled",
      "avalue": "true",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set COD max amount
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "payment",
      "keyName": "codMaxAmount",
      "avalue": "10000",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set payment retry max attempts
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "payment",
      "keyName": "retryMaxAttempts",
      "avalue": "3",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve COD enabled
    When I GET a REST request to URL "/config/payment/codEnabled"
    Then success is true
    And the REST response key "codEnabled" is "true"

  Scenario: Retrieve COD max amount
    When I GET a REST request to URL "/config/payment/codMaxAmount"
    Then success is true
    And the REST response key "codMaxAmount" is "10000"

  # ================================================================
  # ORDER CONFIGURATION
  # ================================================================

  Scenario: Set cancellation window hours
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "order",
      "keyName": "cancellationWindowHours",
      "avalue": "24",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set return window days
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "order",
      "keyName": "returnWindowDays",
      "avalue": "7",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve cancellation window
    When I GET a REST request to URL "/config/order/cancellationWindowHours"
    Then success is true
    And the REST response key "cancellationWindowHours" is "24"

  Scenario: Retrieve return window
    When I GET a REST request to URL "/config/order/returnWindowDays"
    Then success is true
    And the REST response key "returnWindowDays" is "7"

  # ================================================================
  # SUPPLIER CONFIGURATION
  # ================================================================

  Scenario: Set min rating for supplier approval
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "supplier",
      "keyName": "minRatingForApproval",
      "avalue": "3.5",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set max products per supplier
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "supplier",
      "keyName": "maxProductsPerSupplier",
      "avalue": "1000",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set supplier commission rate
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "supplier",
      "keyName": "defaultCommissionRate",
      "avalue": "15",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve supplier min rating
    When I GET a REST request to URL "/config/supplier/minRatingForApproval"
    Then success is true
    And the REST response key "minRatingForApproval" is "3.5"

  Scenario: Retrieve supplier max products
    When I GET a REST request to URL "/config/supplier/maxProductsPerSupplier"
    Then success is true
    And the REST response key "maxProductsPerSupplier" is "1000"

  Scenario: Retrieve supplier commission rate
    When I GET a REST request to URL "/config/supplier/defaultCommissionRate"
    Then success is true
    And the REST response key "defaultCommissionRate" is "15"

  # ================================================================
  # PROMO CONFIGURATION
  # ================================================================

  Scenario: Set max coupons per order
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "promo",
      "keyName": "maxCouponsPerOrder",
      "avalue": "1",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve max coupons per order
    When I GET a REST request to URL "/config/promo/maxCouponsPerOrder"
    Then success is true
    And the REST response key "maxCouponsPerOrder" is "1"

  # ================================================================
  # FEATURE FLAGS
  # ================================================================

  Scenario: Set feature flag - reviews enabled
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "feature",
      "keyName": "reviewsEnabled",
      "avalue": "true",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set feature flag - wishlist enabled
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "feature",
      "keyName": "wishlistEnabled",
      "avalue": "true",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Set feature flag - COD disabled for high value orders
    When I POST a REST request to URL "/cconfig" with payload
    """json
    {
      "moduleName": "feature",
      "keyName": "codDisabledAbove",
      "avalue": "50000",
      "customAttribute": "homebase"
    }
    """
    Then success is true

  Scenario: Retrieve feature flag reviews
    When I GET a REST request to URL "/config/feature/reviewsEnabled"
    Then success is true
    And the REST response key "reviewsEnabled" is "true"

  Scenario: Retrieve feature flag wishlist
    When I GET a REST request to URL "/config/feature/wishlistEnabled"
    Then success is true
    And the REST response key "wishlistEnabled" is "true"

  # ================================================================
  # RETRIEVE NON-EXISTENT KEY (should return empty)
  # ================================================================

  Scenario: Retrieve non-existent config key returns empty
    When I GET a REST request to URL "/config/cart/nonexistentKey"
    Then success is true
    And the REST response is empty

  # ================================================================
  # RETRIEVE ALL KEYS FOR A MODULE
  # ================================================================

  Scenario: Retrieve all cart config keys
    When I GET a REST request to URL "/config/cart"
    Then success is true
    And the REST response key "maxQuantityPerItem" is "10"
    And the REST response key "maxItemsPerCart" is "50"
