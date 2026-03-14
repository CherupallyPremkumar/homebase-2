Feature: Tests the cart Workflow Service using a REST client. This is done only for the
first testcase. Cart service exists and is under test.
It helps to create a cart and manages the state of the cart as documented in states xml
Scenario: Create a new cart
Given that "flowName" equals "cart-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/cart" with payload
"""json
{
    "description": "Description"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Description"

Scenario: Retrieve the cart that just got created
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

 Scenario: Send the addItem event to the cart with comments
 Given that "comment" equals "Comment for addItem"
 And that "event" equals "addItem"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "productId": "test-product-1",
    "quantity": 2
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

 Scenario: Send the initiateCheckout event to the cart with comments
 Given that "comment" equals "Comment for initiateCheckout"
 And that "event" equals "initiateCheckout"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "CHECKOUT_IN_PROGRESS"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

 Scenario: Send the completePayment event to the cart with comments
 Given that "comment" equals "Comment for completePayment"
 And that "event" equals "completePayment"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ORDER_CREATED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"


Scenario: Add new mandatory activities a1,a2 for the last state.
Add a new state "__TERMINAL_STATE__"
Add a completion checker activity "cc" to the last state that leads to __TERMINAL_STATE__
Send cc event on the cart with comments. This should fail since the mandatory activities
have not been completed.
Given that "terminalState" equals "__TERMINAL_STATE__"
And that config strategy is "cartConfigProvider" with prefix "Cart"
And that a new mandatory activity "a1" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new mandatory activity "a2" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new state "${terminalState}" is added to flow "${flowName}"
And that a new activity completion checker "cc" is added from state "${finalState}" to state "${terminalState}" in flow "${flowName}"
And that "comment" equals "Attempting to send cc event without mandatory activities being completed."
And that "event" equals "cc"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
    {
    "comment": "${comment}"
    }
"""
Then the REST response does not contain key "mutatedEntity"
And success is false
And the http status code is 400
And the top level subErrorCode is 49000

Scenario: Retrieve the cart that just got created
When I GET a REST request to URL "/cart/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"

Scenario: Perform mandatory activity (a1) on the  cart with comments
Given that "comment" equals "Performed activity a1."
And that "event" equals "a1"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
"comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value         |
| activityName    | ${event}      |
| activityComment | ${comment}    |

Scenario: Perform mandatory activity (a2) on the  cart with comments
Given that "comment" equals "Performed activity a2."
And that "event" equals "a2"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
"comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value         |
| activityName    | ${event}      |
| activityComment | ${comment}    |

Scenario: Perform mandatory activity (cc) on the  cart with comments
Given that "comment" equals "Performed activity cc after completing all activities."
And that "event" equals "cc"
When I PATCH a REST request to URL "/cart/${id}/${event}" with payload
"""json
{
"comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${terminalState}"

Scenario: Send an invalid event to cart . This will err out.
When I PATCH a REST request to URL "/cart/${id}/invalid" with payload
"""json
{
    "comment": "invalid stuff"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

