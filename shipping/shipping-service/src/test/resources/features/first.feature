Feature: Tests the shipping Workflow Service using a REST client. This is done only for the
first testcase. Shipping service exists and is under test.
It helps to create a shipping and manages the state of the shipping as documented in states xml
Scenario: Create a new shipping
Given that "flowName" equals "shipping-flow"
And that "initialState" equals "AWAITING_PICKUP"
When I POST a REST request to URL "/shipping" with payload
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

Scenario: Retrieve the shipping that just got created
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

 Scenario: Send the courierAssigned event to the shipping with comments
 Given that "comment" equals "Comment for courierAssigned"
 And that "event" equals "courierAssigned"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

 Scenario: Send the inTransit event to the shipping with comments
 Given that "comment" equals "Comment for inTransit"
 And that "event" equals "inTransit"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

 Scenario: Send the returnRequested event to the shipping with comments
 Given that "comment" equals "Comment for returnRequested"
 And that "event" equals "returnRequested"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_REQUESTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"


Scenario: Add new mandatory activities a1,a2 for the last state.
Add a new state "__TERMINAL_STATE__"
Add a completion checker activity "cc" to the last state that leads to __TERMINAL_STATE__
Send cc event on the shipping with comments. This should fail since the mandatory activities
have not been completed.
Given that "terminalState" equals "__TERMINAL_STATE__"
And that config strategy is "shippingConfigProvider" with prefix "Shipping"
And that a new mandatory activity "a1" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new mandatory activity "a2" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new state "${terminalState}" is added to flow "${flowName}"
And that a new activity completion checker "cc" is added from state "${finalState}" to state "${terminalState}" in flow "${flowName}"
And that "comment" equals "Attempting to send cc event without mandatory activities being completed."
And that "event" equals "cc"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
    {
    "comment": "${comment}"
    }
"""
Then the REST response does not contain key "mutatedEntity"
And success is false
And the http status code is 400
And the top level subErrorCode is 49000

Scenario: Retrieve the shipping that just got created
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"

Scenario: Perform mandatory activity (a1) on the  shipping with comments
Given that "comment" equals "Performed activity a1."
And that "event" equals "a1"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
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

Scenario: Perform mandatory activity (a2) on the  shipping with comments
Given that "comment" equals "Performed activity a2."
And that "event" equals "a2"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
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

Scenario: Perform mandatory activity (cc) on the  shipping with comments
Given that "comment" equals "Performed activity cc after completing all activities."
And that "event" equals "cc"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
"comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${terminalState}"

Scenario: Send an invalid event to shipping . This will err out.
When I PATCH a REST request to URL "/shipping/${id}/invalid" with payload
"""json
{
    "comment": "invalid stuff"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

