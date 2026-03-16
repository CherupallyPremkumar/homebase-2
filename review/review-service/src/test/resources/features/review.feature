Feature: Product Review Full Lifecycle
Tests the Review Workflow Service using a REST client.
Manages review states: SUBMITTED -> CHECK_AUTO_PUBLISH -> PUBLISHED/UNDER_MODERATION
UNDER_MODERATION -> PUBLISHED/REJECTED/EDIT_REQUESTED
PUBLISHED -> FLAGGED/ARCHIVED
FLAGGED -> UNDER_MODERATION/PUBLISHED/REJECTED
EDIT_REQUESTED -> UNDER_MODERATION

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

# ================================================================
# Scenario 1: Verified purchase auto-publishes
# ================================================================
Scenario: Create a verified purchase review (auto-publishes)
Given that "flowName" equals "review-flow"
And that "initialState" equals "SUBMITTED"
When I POST a REST request to URL "/review" with payload
"""json
{
    "productId": "prod-001",
    "customerId": "cust-123",
    "orderId": "order-456",
    "rating": 5,
    "title": "Excellent craftsmanship",
    "body": "The quality of this handmade item is outstanding and exceeds expectations.",
    "verifiedPurchase": true
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Submit the verified purchase review (triggers auto-publish)
Given that "event" equals "submitReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "Submitting review",
    "productId": "prod-001",
    "orderId": "order-456",
    "rating": 5,
    "title": "Excellent craftsmanship",
    "body": "The quality of this handmade item is outstanding and exceeds expectations."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark published review as helpful
Given that "event" equals "markHelpful"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "Helpful review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Report published review
Given that "event" equals "reportReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "Reporting review",
    "reportReason": "Seems like a fake review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Flag published review for re-moderation
Given that "event" equals "flagReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "Flagging review",
    "flagReason": "Suspected promotional content"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "FLAGGED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Moderator sends flagged review back to published
Given that "event" equals "publishReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "Review is fine after re-moderation",
    "moderatorNotes": "Content verified, no violations found"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Archive the review
Given that "event" equals "archiveReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "Archiving old review",
    "archiveReason": "Product discontinued"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"

# ================================================================
# Scenario 2: Non-verified purchase goes to moderation
# ================================================================
Scenario: Create a non-verified review (goes to moderation)
Given that "flowName" equals "review-flow"
When I POST a REST request to URL "/review" with payload
"""json
{
    "productId": "prod-002",
    "customerId": "cust-789",
    "rating": 4,
    "title": "Good value for money",
    "body": "Nice product overall, worth the price paid for it.",
    "verifiedPurchase": false
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState2"

Scenario: Submit the non-verified review (goes to UNDER_MODERATION)
Given that "event" equals "submitReview"
When I PATCH a REST request to URL "/review/${id2}/${event}" with payload
"""json
{
    "comment": "Submitting review",
    "productId": "prod-002",
    "rating": 4,
    "title": "Good value for money",
    "body": "Nice product overall, worth the price paid for it."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_MODERATION"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState2"

Scenario: Request edit on review under moderation
Given that "event" equals "requestEdit"
When I PATCH a REST request to URL "/review/${id2}/${event}" with payload
"""json
{
    "comment": "Needs more detail",
    "editInstructions": "Please provide more specific details about the product quality"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "EDIT_REQUESTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState2"

Scenario: Customer resubmits review after edit request
Given that "event" equals "resubmitReview"
When I PATCH a REST request to URL "/review/${id2}/${event}" with payload
"""json
{
    "comment": "Resubmitting with more details",
    "rating": 4,
    "title": "Good value for money",
    "body": "Nice product overall, the stitching is solid and the material feels durable. Worth the price."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_MODERATION"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState2"

Scenario: Moderator publishes the resubmitted review
Given that "event" equals "publishReview"
When I PATCH a REST request to URL "/review/${id2}/${event}" with payload
"""json
{
    "comment": "Approved after edit",
    "moderatorNotes": "Content improved, approved"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

# ================================================================
# Scenario 3: Rejection flow
# ================================================================
Scenario: Create a review to test rejection
Given that "flowName" equals "review-flow"
When I POST a REST request to URL "/review" with payload
"""json
{
    "productId": "prod-003",
    "customerId": "cust-spam",
    "rating": 1,
    "title": "Terrible product do not buy",
    "body": "This is a spam review with promotional content and misleading information.",
    "verifiedPurchase": false
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id3"

Scenario: Submit spam review (goes to moderation)
Given that "event" equals "submitReview"
When I PATCH a REST request to URL "/review/${id3}/${event}" with payload
"""json
{
    "comment": "Submitting review",
    "productId": "prod-003",
    "rating": 1,
    "title": "Terrible product do not buy",
    "body": "This is a spam review with promotional content and misleading information."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_MODERATION"

Scenario: Reject spam review with reason
Given that "event" equals "rejectReview"
When I PATCH a REST request to URL "/review/${id3}/${event}" with payload
"""json
{
    "comment": "Rejecting spam",
    "rejectionReason": "Contains spam and promotional content"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id3}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

# ================================================================
# Scenario 4: Invalid event error handling
# ================================================================
Scenario: Send an invalid event to review. This will err out.
When I PATCH a REST request to URL "/review/${id2}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
