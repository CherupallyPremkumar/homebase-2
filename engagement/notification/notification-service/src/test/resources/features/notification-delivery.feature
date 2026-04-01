Feature: Notification Delivery Lifecycle
  Tests the full notification delivery lifecycle through the STM workflow.
  Covers: Create -> Queue -> Send -> Deliver, failure handling, retry with auto-state CHECK_RETRY,
  bounce on max retries, and channel validation.

# ====================================================================
# HAPPY PATH: Create -> Queue -> Send -> Deliver (EMAIL)
# ====================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create email notification
Given that "flowName" equals "notification-flow"
And that "initialState" equals "CREATED"
When I POST a REST request to URL "/notification" with payload
"""json
{
    "customerId": "cust-001",
    "channel": "EMAIL",
    "templateId": "ORDER_CONFIRMATION",
    "subject": "Your order has been confirmed",
    "body": "Dear Customer, your order #12345 has been confirmed.",
    "recipientAddress": "customer@example.com",
    "description": "Order confirmation email"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "emailNotifId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Queue the email notification (validates channel, template, unsubscribe)
Given that "event" equals "queue"
When I PATCH a REST request to URL "/notification/${emailNotifId}/${event}" with payload
"""json
{
    "comment": "Queueing for delivery"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${emailNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "QUEUED"

Scenario: Send the email notification (dispatch via channel adapter)
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${emailNotifId}/${event}" with payload
"""json
{
    "comment": "Sending email notification"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${emailNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

Scenario: Confirm email sent
Given that "event" equals "markDelivered"
When I PATCH a REST request to URL "/notification/${emailNotifId}/${event}" with payload
"""json
{
    "comment": "Email sent successfully"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${emailNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "SENT"

Scenario: Confirm email delivered
Given that "event" equals "markDelivered"
When I PATCH a REST request to URL "/notification/${emailNotifId}/${event}" with payload
"""json
{
    "comment": "Delivery confirmed by recipient server",
    "deliveryConfirmationId": "smtp-confirm-abc123"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${emailNotifId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

# ====================================================================
# PUSH NOTIFICATION PATH: Create -> Queue -> Send -> Deliver
# ====================================================================

Scenario: Create push notification
When I POST a REST request to URL "/notification" with payload
"""json
{
    "customerId": "cust-002",
    "channel": "PUSH",
    "subject": "Your package is out for delivery",
    "body": "Your order is being delivered today!",
    "recipientAddress": "device-token-xyz",
    "description": "Push notification for delivery update"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "pushNotifId"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Queue push notification
Given that "event" equals "queue"
When I PATCH a REST request to URL "/notification/${pushNotifId}/${event}" with payload
"""json
{
    "comment": "Queueing push notification"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "QUEUED"

Scenario: Send push notification
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${pushNotifId}/${event}" with payload
"""json
{
    "comment": "Sending push notification"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

# ====================================================================
# FAILURE + RETRY PATH: Create -> Queue -> Send -> Fail -> Retry -> Send
# ====================================================================

Scenario: Create SMS notification that will fail
When I POST a REST request to URL "/notification" with payload
"""json
{
    "customerId": "cust-003",
    "channel": "SMS",
    "body": "Your delivery is scheduled for tomorrow.",
    "recipientAddress": "+1234567890",
    "description": "SMS notification that will fail"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "failNotifId"
And the REST response key "mutatedEntity.currentState.stateId" is "CREATED"

Scenario: Queue SMS for delivery
Given that "event" equals "queue"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Queueing SMS"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "QUEUED"

Scenario: Attempt to send SMS
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Sending SMS"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

Scenario: SMS delivery fails
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "SMS gateway timeout",
    "failureReason": "Gateway timeout - carrier unreachable"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: Retry failed SMS (CHECK_RETRY auto-state routes to RETRY since retryCount < 3)
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Retrying notification delivery"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "RETRY"

Scenario: Re-send from RETRY state
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Sending after retry"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

Scenario: Confirm delivery after retry
Given that "event" equals "markDelivered"
When I PATCH a REST request to URL "/notification/${failNotifId}/${event}" with payload
"""json
{
    "comment": "Sent successfully after retry"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SENT"

# ====================================================================
# CHANNEL VALIDATION: Invalid channel rejected during queue
# ====================================================================

Scenario: Create notification with invalid channel
When I POST a REST request to URL "/notification" with payload
"""json
{
    "customerId": "cust-004",
    "channel": "PIGEON",
    "body": "This should not work",
    "description": "Notification with invalid channel"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "invalidChannelId"

Scenario: Queue rejects invalid channel
Given that "event" equals "queue"
When I PATCH a REST request to URL "/notification/${invalidChannelId}/${event}" with payload
"""json
{
    "comment": "Trying to queue invalid channel"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false

# ====================================================================
# MAX RETRY + BOUNCE: Fail 3 times -> retry routes to BOUNCED
# ====================================================================

Scenario: Create notification to hit max retries
When I POST a REST request to URL "/notification" with payload
"""json
{
    "customerId": "cust-005",
    "channel": "EMAIL",
    "templateId": "GENERIC_ALERT",
    "body": "Test bounce",
    "recipientAddress": "bounce@example.com",
    "description": "Notification to hit max retries"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "bounceId"

Scenario: Queue for bounce test
Given that "event" equals "queue"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Queue" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "QUEUED"

Scenario: First send attempt
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Send 1" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

Scenario: First failure (retryCount becomes 1)
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Fail 1", "failureReason": "Connection refused" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: First retry (retryCount=1 < 3, goes to RETRY)
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Retry 1" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "RETRY"

Scenario: Second send
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Send 2" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

Scenario: Second failure (retryCount becomes 2)
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Fail 2", "failureReason": "Mailbox full" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: Second retry (retryCount=2 < 3, goes to RETRY)
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Retry 2" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "RETRY"

Scenario: Third send
Given that "event" equals "send"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Send 3" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "SENDING"

Scenario: Third failure (retryCount becomes 3)
Given that "event" equals "fail"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Fail 3", "failureReason": "Permanent failure" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "FAILED"

Scenario: Third retry (retryCount=3 >= 3, CHECK_RETRY auto-routes to BOUNCED)
Given that "event" equals "retry"
When I PATCH a REST request to URL "/notification/${bounceId}/${event}" with payload
"""json
{ "comment": "Retry 3 - should bounce" }
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "BOUNCED"

# ====================================================================
# IN_APP NOTIFICATION PATH
# ====================================================================

Scenario: Create and queue IN_APP notification
When I POST a REST request to URL "/notification" with payload
"""json
{
    "customerId": "cust-006",
    "channel": "IN_APP",
    "subject": "Welcome to HomeBase!",
    "body": "Thank you for signing up.",
    "description": "In-app welcome notification"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "inAppNotifId"

Scenario: Queue IN_APP notification
Given that "event" equals "queue"
When I PATCH a REST request to URL "/notification/${inAppNotifId}/${event}" with payload
"""json
{ "comment": "Queue in-app notification" }
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "QUEUED"

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
