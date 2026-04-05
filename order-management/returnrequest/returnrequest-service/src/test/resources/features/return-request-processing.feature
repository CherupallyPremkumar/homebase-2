Feature: Return Request Processing
Tests auto-approve for low-value items, rejection with comment policy,
partial approval, and value-based routing through CHECK_AUTO_APPROVE.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Submit low-value return request (under 500 threshold)
Given that "flowName" equals "return-request-flow"
And that "initialState" equals "REQUESTED"
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return low-value item",
    "orderId": "ORD-LOW-001",
    "customerId": "CUST-LOW-001",
    "reason": "DAMAGED",
    "returnType": "REFUND",
    "totalRefundAmount": 150.00
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "autoId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Review low-value return - should auto-approve (150 <= 500)
Given that "comment" equals "Reviewing low-value return"
And that "event" equals "reviewReturn"
When I PATCH a REST request to URL "/returnrequest/${autoId}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reviewerId": "SUPPORT-AUTO"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${autoId}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Submit high-value return request (above 500 threshold)
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return expensive item",
    "orderId": "ORD-HIGH-001",
    "customerId": "CUST-HIGH-001",
    "reason": "DEFECTIVE",
    "returnType": "REFUND",
    "totalRefundAmount": 1500.00
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "manualId"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Review high-value return - should go to UNDER_REVIEW (1500 > 500)
Given that "comment" equals "Reviewing high-value return"
And that "event" equals "reviewReturn"
When I PATCH a REST request to URL "/returnrequest/${manualId}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reviewerId": "SUPPORT-MANUAL"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${manualId}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Create a return request for rejection test
When I POST a REST request to URL "/returnrequest" with payload
"""json
{
    "description": "Return request to test rejection",
    "orderId": "ORD-REJ-002",
    "customerId": "CUST-REJ-002",
    "reason": "DAMAGED",
    "returnType": "REFUND",
    "totalRefundAmount": 800.00
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejId"
And the REST response key "mutatedEntity.currentState.stateId" is "REQUESTED"

Scenario: Review the return for rejection
Given that "comment" equals "Reviewing return for rejection"
And that "event" equals "reviewReturn"
When I PATCH a REST request to URL "/returnrequest/${rejId}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejId}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject return without comment should fail (comment-required policy)
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
When I PATCH a REST request to URL "/returnrequest/${rejId}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
