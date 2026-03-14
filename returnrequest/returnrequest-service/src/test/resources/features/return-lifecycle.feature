Feature: Tests the full Return Request Lifecycle using Chenile STM.
Covers the happy path: REQUESTED -> UNDER_REVIEW -> APPROVED -> IN_TRANSIT_BACK -> RECEIVED -> REFUNDED,
and the rejection path: REQUESTED -> UNDER_REVIEW -> REJECTED.
Includes mandatory activity tracking for return processing.

Scenario: Create a new return request
Given that "flowName" equals "return-request-flow"
And that "initialState" equals "REQUESTED"
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for damaged product",
    "orderId": "ORD-2024-001",
    "orderItemId": "ITEM-001",
    "reason": "Product arrived damaged",
    "quantity": 1
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Return request for damaged product"

Scenario: Retrieve the return request that was just created
When I GET a REST request to URL "/returnrequest/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Inspect the return request (REQUESTED -> UNDER_REVIEW)
Given that "comment" equals "Quality checker reviewing return request and product photos"
And that "event" equals "inspectReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "inspectorId": "QC-001"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Approve the return request (UNDER_REVIEW -> APPROVED)
Given that "comment" equals "Damage confirmed from photos, return approved for full refund"
And that "event" equals "approveReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "refundType": "FULL"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Initiate pickup for approved return (APPROVED -> IN_TRANSIT_BACK)
Given that "comment" equals "Return pickup scheduled with courier"
And that "event" equals "pickupInitiated"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "pickupTrackingNumber": "RET-DHL-56789"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT_BACK"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Item received at warehouse (IN_TRANSIT_BACK -> RECEIVED)
Given that "comment" equals "Item received at warehouse, condition matches description"
And that "event" equals "itemReceived"
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
And the REST response key "mutatedEntity.currentState.stateId" is "RECEIVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Process refund for received item (RECEIVED -> REFUNDED)
Given that "comment" equals "Full refund of INR 2,500 processed to customer wallet"
And that "event" equals "processRefund"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "refundAmount": 2500.00,
    "refundMethod": "WALLET"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Verify refunded state is terminal (no further transitions)
When I PATCH a REST request to URL "/returnrequest/${id}/inspectReturn" with payload
"""json
{
    "comment": "Attempting event on refunded return"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Create a second return request for the rejection flow
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for buyer remorse",
    "orderId": "ORD-2024-002",
    "reason": "Changed my mind",
    "quantity": 1
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Inspect the second return request
Given that "comment" equals "Reviewing return request for buyer remorse"
And that "event" equals "inspectReturn"
When I PATCH a REST request to URL "/returnrequest/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject the second return request (UNDER_REVIEW -> REJECTED)
Given that "comment" equals "Return policy does not cover buyer remorse after 7 days"
And that "event" equals "rejectReturn"
When I PATCH a REST request to URL "/returnrequest/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "rejectionReason": "Outside return policy window"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "rejectedState"

Scenario: Verify rejected state is terminal (no further transitions)
When I PATCH a REST request to URL "/returnrequest/${id2}/approveReturn" with payload
"""json
{
    "comment": "Attempting to approve a rejected return"
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
Given that "comment" equals "Refund amount of INR 2,500 verified against order total"
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
