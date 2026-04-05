Feature: Tests the full Return Request Lifecycle using Chenile STM.
Covers the happy path with manual review (high-value): REQUESTED -> UNDER_REVIEW -> APPROVED -> ITEM_RECEIVED -> INSPECTED -> REFUND_INITIATED,
and the rejection path: REQUESTED -> UNDER_REVIEW -> REJECTED.
Includes mandatory activity tracking for refund processing.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a high-value return request (above auto-approve threshold)
Given that "flowName" equals "return-request-flow"
And that "initialState" equals "REQUESTED"
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for expensive electronics",
    "orderId": "ORD-HV-001",
    "customerId": "CUST-HV-001",
    "reason": "DEFECTIVE",
    "returnType": "REFUND",
    "totalRefundAmount": 2500.00
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Retrieve the return request
When I GET a REST request to URL "/returnrequest/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Review return - high-value goes to UNDER_REVIEW (not auto-approved)
Given that "comment" equals "Support reviewing high-value return"
And that "event" equals "reviewReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reviewerId": "SUPPORT-002"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve the return request (UNDER_REVIEW -> APPROVED)
Given that "comment" equals "Defect confirmed, return approved for full refund"
And that "event" equals "approveReturn"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "refundType": "REFUND",
    "refundAmount": 2500.00
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Receive item at warehouse (APPROVED -> ITEM_RECEIVED)
Given that "comment" equals "Item received at warehouse"
And that "event" equals "receiveItem"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "warehouseId": "WH-BLR-001",
    "conditionOnReceipt": "DEFECTIVE_AS_REPORTED"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ITEM_RECEIVED"

Scenario: Inspect item (ITEM_RECEIVED -> INSPECTED)
Given that "comment" equals "Inspection completed, defect confirmed"
And that "event" equals "inspectItem"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "inspectorId": "INSP-002",
    "inspectorNotes": "Hardware defect confirmed"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "INSPECTED"

Scenario: Initiate refund (INSPECTED -> REFUND_INITIATED)
Given that "comment" equals "Full refund of 2500 processed"
And that "event" equals "initiateRefund"
When I PATCH a REST request to URL "/returnrequest/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "finalRefundAmount": 2500.00,
    "refundMethod": "ORIGINAL_PAYMENT"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REFUND_INITIATED"

Scenario: Verify REFUND_INITIATED is terminal (no further transitions)
When I PATCH a REST request to URL "/returnrequest/${id}/reviewReturn" with payload
"""json
{
    "comment": "Attempting event on completed return"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Create a return request for the rejection flow
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request for buyer remorse",
    "orderId": "ORD-REJ-001",
    "customerId": "CUST-REJ-001",
    "reason": "DAMAGED",
    "returnType": "REFUND",
    "totalRefundAmount": 800.00
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Review the second return request
Given that "comment" equals "Reviewing return request for rejection"
And that "event" equals "reviewReturn"
When I PATCH a REST request to URL "/returnrequest/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reviewerId": "SUPPORT-003"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject the return request (UNDER_REVIEW -> REJECTED)
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

Scenario: Verify rejected state is terminal (no further transitions)
When I PATCH a REST request to URL "/returnrequest/${id2}/approveReturn" with payload
"""json
{
    "comment": "Attempting to approve a rejected return"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to returnrequest. This will err out.
When I PATCH a REST request to URL "/returnrequest/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
