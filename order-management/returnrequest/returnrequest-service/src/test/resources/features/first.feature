Feature: Tests the returnrequest Workflow Service using a REST client.
Covers basic lifecycle: REQUESTED -> UNDER_REVIEW -> APPROVED -> ITEM_RECEIVED -> INSPECTED -> REFUND_INITIATED
and rejection path, with Keycloak security integration.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new return request
Given that "flowName" equals "return-request-flow"
And that "initialState" equals "REQUESTED"
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for damaged product",
    "orderId": "ORD-2024-001",
    "customerId": "CUST-001",
    "reason": "DAMAGED",
    "returnType": "REFUND",
    "totalRefundAmount": 200.00
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Return request for damaged product"

Scenario: Retrieve the return request that just got created
When I GET a REST request to URL "/returnrequest/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Review return request - auto-approve path (value 200 <= 500 threshold)
Given that "comment" equals "Reviewing return request"
And that "event" equals "reviewReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reviewerId": "SUPPORT-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Receive item at warehouse (APPROVED -> ITEM_RECEIVED)
Given that "comment" equals "Item received at warehouse"
And that "event" equals "receiveItem"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "warehouseId": "WH-BLR-001",
    "conditionOnReceipt": "DAMAGED_AS_REPORTED"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Inspect item at warehouse (ITEM_RECEIVED -> INSPECTED)
Given that "comment" equals "Item inspected, damage confirmed"
And that "event" equals "inspectItem"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "inspectorId": "INSP-001",
    "inspectorNotes": "Damage confirmed as reported"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INSPECTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Initiate refund (INSPECTED -> REFUND_INITIATED)
Given that "comment" equals "Refund initiated for full amount"
And that "event" equals "initiateRefund"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "finalRefundAmount": 200.00,
    "refundMethod": "ORIGINAL_PAYMENT"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUND_INITIATED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Verify REFUND_INITIATED state is terminal (no further transitions)
When I PATCH a REST request to URL "/returnrequest/${id}/reviewReturn" with payload
"""json
{
    "comment": "Attempting event on refund-initiated return"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Add mandatory activities for refund processing verification
Given that "terminalState" equals "__TERMINAL_STATE__"
And that config strategy is "returnrequestConfigProvider" with prefix "Returnrequest"
And that a new mandatory activity "verifyRefundAmount" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new mandatory activity "notifyAccounting" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new state "${terminalState}" is added to flow "${flowName}"
And that a new activity completion checker "closeReturn" is added from state "${finalState}" to state "${terminalState}" in flow "${flowName}"
And that "comment" equals "Attempting closeReturn without completing mandatory activities"
And that "event" equals "closeReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false
And the http status code is 400
And the top level subErrorCode is 49000

Scenario: Perform mandatory activity verifyRefundAmount
Given that "comment" equals "Refund amount verified against order total"
And that "event" equals "verifyRefundAmount"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value                                                    |
| activityName    | ${event}                                                 |
| activityComment | ${comment}                                               |

Scenario: Perform mandatory activity notifyAccounting
Given that "comment" equals "Accounting team notified of refund for reconciliation"
And that "event" equals "notifyAccounting"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value                                                    |
| activityName    | ${event}                                                 |
| activityComment | ${comment}                                               |

Scenario: Close return after all mandatory activities done
Given that "comment" equals "All return processing complete, closing"
And that "event" equals "closeReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${terminalState}"

Scenario: Send an invalid event to returnrequest. This will err out.
When I PATCH a REST request to URL "/returnrequest/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
