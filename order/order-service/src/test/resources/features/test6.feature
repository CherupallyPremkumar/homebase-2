Feature: Testcase ID 6 — Invalid transitions and edge cases.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create an order
When I POST a REST request to URL "/order" with payload
"""json
{
    "description": "Invalid transition test",
    "customerId": "cust-test6",
    "totalAmount": 500,
    "currency": "INR"
}
"""
Then store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Cannot start processing from CREATED (need payment first)
Given that "event" equals "startProcessing"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Try processing before payment" }
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Cannot mark shipped from CREATED
Given that "event" equals "markShipped"
When I PATCH a REST request to URL "/order/${id}/${event}" with payload
"""json
{ "comment": "Try shipping before payment" }
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
