Feature: Tests the Checkout Orchestration Service using a REST client.
The checkout service orchestrates the full checkout saga: cart validation,
inventory reservation, order creation, and payment initiation.

Scenario: Initialize checkout from a valid cart
When I POST a REST request to URL "/api/v1/checkout/initiate" with payload
"""json
{
    "cartId": "test-cart-001",
    "shippingAddressId": "addr-001"
}
"""
Then the http status code is 200
And the REST response contains key "payload"
And store "$.payload.orderId" from response to "orderId"

Scenario: Retrieve order details after checkout initiation
When I GET a REST request to URL "/order/${orderId}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${orderId}"

Scenario: Initiate payment for the created order
When I POST a REST request to URL "/api/v1/checkout/payment/initiate" with payload
"""json
{
    "orderId": "${orderId}",
    "currency": "INR"
}
"""
Then the http status code is 200
And the REST response contains key "payload"

Scenario: Check payment status while processing
When I POST a REST request to URL "/api/v1/checkout/payment/status" with payload
"""json
{
    "orderId": "${orderId}"
}
"""
Then the http status code is 200

Scenario: Initialize checkout with missing cart should fail
When I POST a REST request to URL "/api/v1/checkout/initiate" with payload
"""json
{
    "cartId": "non-existent-cart-999",
    "shippingAddressId": "addr-001"
}
"""
Then the http status code is 400

Scenario: Initiate payment for non-existent order should fail
When I POST a REST request to URL "/api/v1/checkout/payment/initiate" with payload
"""json
{
    "orderId": "non-existent-order-999",
    "currency": "INR"
}
"""
Then the http status code is 400

Scenario: Check payment status for non-existent order should fail
When I POST a REST request to URL "/api/v1/checkout/payment/status" with payload
"""json
{
    "orderId": "non-existent-order-999"
}
"""
Then the http status code is 400
