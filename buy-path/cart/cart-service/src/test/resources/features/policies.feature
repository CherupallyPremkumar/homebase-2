Feature: Cart Policy Enforcement
  Tests that the cart correctly enforces cconfig-based policies:
  item limits, quantity limits, and coupon limits.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a cart for policy tests
Given that "flowName" equals "cart-flow"
And that "initialState" equals "ACTIVE"
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Policy test cart"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Add item to cart succeeds
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding first item",
    "productId": "prod-policy-001",
    "variantId": "var-policy-001-default",
    "productName": "Policy Test Product",
    "quantity": 1,
    "unitPrice": 100
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add item with excessive quantity should fail
Given that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "Adding item with excessive quantity",
    "productId": "prod-policy-excess",
    "variantId": "var-policy-excess-default",
    "productName": "Excess Quantity Product",
    "quantity": 99,
    "unitPrice": 50
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false
And the http status code is 400
