Feature: Product Review Moderation
Tests the Review Workflow Service using a REST client.
Manages review states: PENDING -> APPROVED/REJECTED/FLAGGED as documented in review-states.xml.

Scenario: Create a new review in PENDING state
Given that "flowName" equals "review-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/review" with payload
"""json
{
    "description": "Great handmade product",
    "productId": "prod-001",
    "userId": "user-123",
    "orderId": "order-456",
    "rating": 5,
    "title": "Excellent craftsmanship",
    "body": "The quality of this handmade item is outstanding.",
    "verifiedPurchase": true
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the review that just got created
When I GET a REST request to URL "/review/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Approve review
Given that "comment" equals "Review meets community guidelines"
And that "event" equals "approveReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark review as helpful
Given that "comment" equals "Customer found this review helpful"
And that "event" equals "markHelpful"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Flag review for re-moderation
Given that "comment" equals "Review flagged for inappropriate content"
And that "event" equals "flagReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "flagReason": "inappropriate"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "FLAGGED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Reject review with reason from FLAGGED state
Given that "comment" equals "Review violates community guidelines"
And that "event" equals "rejectReview"
When I PATCH a REST request to URL "/review/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "rejectionReason": "Contains spam and promotional content"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Create a second review to test verified purchase badge
Given that "flowName" equals "review-flow"
When I POST a REST request to URL "/review" with payload
"""json
{
    "description": "Another great product",
    "productId": "prod-002",
    "userId": "user-789",
    "orderId": "order-012",
    "rating": 4,
    "title": "Good value",
    "body": "Nice product for the price.",
    "verifiedPurchase": true
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Approve the verified purchase review
Given that "event" equals "approveReview"
When I PATCH a REST request to URL "/review/${id2}/${event}" with payload
"""json
{
    "comment": "Approved - verified purchase"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

Scenario: Send an invalid event to review. This will err out.
When I PATCH a REST request to URL "/review/${id2}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
