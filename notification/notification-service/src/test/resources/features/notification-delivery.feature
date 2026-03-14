Feature: Notification Delivery
  Tests the notification delivery lifecycle through the STM workflow.
  Covers sending via different channels, failure handling, retries, and read tracking.

# ====================================================================
# HAPPY PATH: Create -> Send (EMAIL) -> Mark Read
# ====================================================================

Scenario: Send email notification
Given that "flowName" equals "notification-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/notification" with payload
"""json
{
    "userId": "user-001",
    "description": "Order confirmation email"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "emailNotifId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Dispatch the email notification
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${emailNotifId}/${event}" with payload
"""json
{
    "comment": "Sending email notification",
    "channel": "EMAIL",
    "templateCode": "ORDER_CONFIRMATION",
    "subject": "Your order has been confirmed",
    "body": "Dear Customer, your order #12345 has been confirmed.",
    "referenceType": "ORDER",
    "referenceId": "order-12345"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${emailNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SENT"
And the REST response key "mutatedEntity.channel" is "EMAIL"
And the REST response key "mutatedEntity.subject" is "Your order has been confirmed"

Scenario: Mark notification as read
Given that "event" equals "markRead"
When I PATCH a REST request to URL "/notification/${emailNotifId}/${event}" with payload
"""json
{
    "comment": "Customer opened the email"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${emailNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "READ"

# ====================================================================
# PUSH NOTIFICATION PATH: Create -> Send (PUSH) -> Read
# ====================================================================

Scenario: Send push notification
When I POST a REST request to URL "/notification" with payload
"""json
{
    "userId": "user-002",
    "description": "Push notification for delivery update"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "pushNotifId"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Dispatch push notification
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${pushNotifId}/${event}" with payload
"""json
{
    "comment": "Sending push notification",
    "channel": "PUSH",
    "subject": "Your package is out for delivery",
    "body": "Your order is being delivered today!",
    "referenceType": "SHIPMENT",
    "referenceId": "shipment-567"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${pushNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SENT"
And the REST response key "mutatedEntity.channel" is "PUSH"

# ====================================================================
# FAILURE + RETRY PATH: Create -> Fail -> Retry -> Send
# ====================================================================

Scenario: Notification delivery failure
When I POST a REST request to URL "/notification" with payload
"""json
{
    "userId": "user-003",
    "description": "SMS notification that will fail"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failNotifId"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Record notification failure
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "SMS gateway timeout - delivery failed"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: Retry failed notification
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Retrying notification delivery"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Successfully send after retry
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Sending after retry",
    "channel": "SMS",
    "subject": "Delivery Update",
    "body": "Your package has been delivered.",
    "referenceType": "ORDER",
    "referenceId": "order-789"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${failNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SENT"

# ====================================================================
# RESPECT USER PREFERENCES: Validates channel type checking
# ====================================================================

Scenario: Respect user notification preferences - invalid channel rejected
When I POST a REST request to URL "/notification" with payload
"""json
{
    "userId": "user-004",
    "description": "Notification with invalid channel"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "prefNotifId"

Scenario: Attempt to send with invalid channel
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${prefNotifId}/${event}" with payload
"""json
{
    "comment": "Trying invalid channel",
    "channel": "PIGEON",
    "body": "This should not work"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false

# ====================================================================
# MAX RETRY LIMIT: Fail -> Retry -> Fail -> Retry -> Fail -> Retry (fails)
# ====================================================================

Scenario: Max retry limit exceeded - create notification
When I POST a REST request to URL "/notification" with payload
"""json
{
    "userId": "user-005",
    "description": "Notification to hit max retries"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "maxRetryId"

Scenario: First failure
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${maxRetryId}/${event}" with payload
"""json
{
    "comment": "First delivery failure"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: First retry
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${maxRetryId}/${event}" with payload
"""json
{
    "comment": "First retry"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Second failure
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${maxRetryId}/${event}" with payload
"""json
{
    "comment": "Second delivery failure"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: Second retry
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${maxRetryId}/${event}" with payload
"""json
{
    "comment": "Second retry"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Third failure
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${maxRetryId}/${event}" with payload
"""json
{
    "comment": "Third delivery failure"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: Third retry exceeds max limit
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${maxRetryId}/${event}" with payload
"""json
{
    "comment": "Third retry - should fail"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false

# ====================================================================
# INVALID EVENT
# ====================================================================

Scenario: Send invalid event to notification
When I PATCH a REST request to URL "/notification/${emailNotifId}/invalidEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
