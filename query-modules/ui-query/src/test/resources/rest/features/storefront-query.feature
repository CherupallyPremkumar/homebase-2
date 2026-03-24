Feature: Tests the Storefront Query Service using a REST client.

  Scenario: Storefront product cards returns published products with pagination
    When I POST a REST request to URL "/q/product-cards" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.productId"
    And the REST response contains key "list[0].row.name"
    And the REST response contains key "list[0].row.minPrice"
    And the REST response contains key "list[0].row.inStock"

  Scenario: Storefront product cards filtered by category
    When I POST a REST request to URL "/q/product-cards" with payload
    """
    {
      "filters": {
        "categoryId": "cat-audio"
      },
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "2"

  Scenario: Storefront product cards filtered by brand
    When I POST a REST request to URL "/q/product-cards" with payload
    """
    {
      "filters": {
        "brand": "SoundMax"
      },
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "2"

  Scenario: Storefront product detail by slug
    When I POST a REST request to URL "/q/product-detail" with payload
    """
    {
      "filters": {
        "slug": "wireless-headphones"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "list[0].row.productId" is "prod-1"
    And the REST response key "list[0].row.name" is "Wireless Headphones"
    And the REST response key "list[0].row.brand" is "SoundMax"

  Scenario: Storefront product variants with price
    When I POST a REST request to URL "/q/product-variants-with-price" with payload
    """
    {
      "filters": {
        "productId": "prod-1"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.variantId"
    And the REST response contains key "list[0].row.sku"
    And the REST response contains key "list[0].row.sellingPrice"

  Scenario: Storefront featured products
    When I POST a REST request to URL "/q/featured-products" with payload
    """
    {
      "pageNum": 1,
      "numRowsInPage": 5
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.productId"
    And the REST response contains key "list[0].row.name"

  Scenario: Storefront my orders for a customer
    When I POST a REST request to URL "/q/my-orders" with payload
    """
    {
      "filters": {
        "customerId": "cust-1"
      },
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "2"

  Scenario: Storefront order detail
    When I POST a REST request to URL "/q/order-detail" with payload
    """
    {
      "filters": {
        "orderId": "ord-1",
        "customerId": "cust-1"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "list[0].row.orderId" is "ord-1"
    And the REST response key "list[0].row.orderState" is "DELIVERED"

  Scenario: Storefront order line items
    When I POST a REST request to URL "/q/order-line-items" with payload
    """
    {
      "filters": {
        "orderId": "ord-1"
      }
    }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "2"

  Scenario: Storefront brand list
    When I POST a REST request to URL "/q/brand-list" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.brand"
    And the REST response contains key "list[0].row.productCount"

  Scenario: Storefront price ranges
    When I POST a REST request to URL "/q/price-ranges" with payload
    """
    {}
    """
    Then the http status code is 200
    And success is true
    And the REST response contains key "list[0].row.priceRange"
    And the REST response contains key "list[0].row.productCount"
