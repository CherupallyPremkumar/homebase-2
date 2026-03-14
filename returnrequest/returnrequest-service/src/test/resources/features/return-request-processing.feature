Feature: Return Request Processing
Tests complete return request business logic including window validation,
auto-approve for low-value items, rejection with comment policy,
full lifecycle flow, and refund processing.

Scenario: Submit return request within window
Given that "flowName" equals "return-request-flow"
And that "initialState" equals "REQUESTED"
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for damaged product",
    "orderId": "ORD-001",
    "orderItemId": "ITEM-001",
    "reason": "DAMAGED",
    "quantity": 1,
    "itemPrice": 1500.00,
    "orderDeliveryDate": "2026-03-11T10:00:00"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.orderId" is "ORD-001"
And the REST response key "mutatedEntity.reason" is "DAMAGED"

Scenario: Retrieve the return request that was just created
When I GET a REST request to URL "/returnrequest/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Inspect return for high-value item goes to UNDER_REVIEW
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

Scenario: Approve return request
Given that "comment" equals "Damage confirmed, return approved for full refund"
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

Scenario: Initiate pickup for approved return
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

Scenario: Warehouse receives returned item
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

Scenario: Complete return and refund - refund amount should match item price
Given that "comment" equals "Full refund processed to customer wallet"
And that "event" equals "processRefund"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "refundAmount": 1500.00,
    "refundMethod": "WALLET"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUNDED"

Scenario: Verify refunded state is terminal
When I PATCH a REST request to URL "/returnrequest/${id}/inspectReturn" with payload
"""json
{
    "comment": "Attempting event on refunded return"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Create a return request for auto-approve test with low-value item
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for low-value item",
    "orderId": "ORD-AUTO-001",
    "orderItemId": "ITEM-AUTO-001",
    "reason": "DAMAGED",
    "quantity": 1,
    "itemPrice": 200.00,
    "orderDeliveryDate": "2026-03-11T10:00:00"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "autoId"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Auto-approve low-value return - inspect marks as AUTO_APPROVED
Given that "comment" equals "Quality check for low-value item"
And that "event" equals "inspectReturn"
When I PATCH a REST request to URL "/returnrequest/${autoId}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "inspectorId": "QC-002"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${autoId}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"
And the REST response key "mutatedEntity.returnType" is "AUTO_APPROVED"

Scenario: Create a return request for the rejection flow
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for buyer remorse",
    "orderId": "ORD-REJ-001",
    "orderItemId": "ITEM-REJ-001",
    "reason": "DAMAGED",
    "quantity": 1,
    "orderDeliveryDate": "2026-03-11T10:00:00"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejId"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Inspect the return request for rejection test
Given that "comment" equals "Reviewing return for rejection"
And that "event" equals "inspectReturn"
When I PATCH a REST request to URL "/returnrequest/${rejId}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejId}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject return without comment should fail (comment-required-on-reject policy)
Given that "event" equals "rejectReturn"
When I PATCH a REST request to URL "/returnrequest/${rejId}/${event}" with payload
"""json
{
    "rejectionReason": "Outside return policy"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false

Scenario: Reject return with proper comment succeeds
Given that "comment" equals "Return policy does not cover this item after inspection"
And that "event" equals "rejectReturn"
When I PATCH a REST request to URL "/returnrequest/${rejId}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "rejectionReason": "Outside return policy window"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

Scenario: Verify rejected state is terminal
When I PATCH a REST request to URL "/returnrequest/${rejId}/approveReturn" with payload
"""json
{
    "comment": "Attempting to approve rejected return"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to return request
When I PATCH a REST request to URL "/returnrequest/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
