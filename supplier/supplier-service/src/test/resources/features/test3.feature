Feature: Testcase ID 3
Tests the supplier resubmission flow: APPLIED -> UNDER_REVIEW -> REJECTED -> APPLIED -> UNDER_REVIEW -> APPROVED.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new supplier
Given that "flowName" equals "supplier-flow"
And that "initialState" equals "APPLIED"
When I POST a REST request to URL "/supplier" with payload
"""json
{
    "businessName": "Test Supplier 3",
    "businessType": "COMPANY",
    "contactEmail": "test3@supplier.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Retrieve the supplier that just got created
When I GET a REST request to URL "/supplier/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Review the supplier (APPLIED -> UNDER_REVIEW)
Given that "comment" equals "Reviewing"
And that "event" equals "reviewSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject the supplier (UNDER_REVIEW -> REJECTED)
Given that "comment" equals "Missing tax documents"
And that "event" equals "rejectSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "reason": "Missing tax documents"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

Scenario: Resubmit the supplier (REJECTED -> APPLIED)
Given that "comment" equals "Documents updated"
And that "event" equals "resubmitSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"

Scenario: Review again (APPLIED -> UNDER_REVIEW)
Given that "comment" equals "Re-reviewing"
And that "event" equals "reviewSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve the supplier (UNDER_REVIEW -> APPROVED)
Given that "comment" equals "Approved on second review"
And that "event" equals "approveSupplier"
When I PATCH a REST request to URL "/supplier/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
