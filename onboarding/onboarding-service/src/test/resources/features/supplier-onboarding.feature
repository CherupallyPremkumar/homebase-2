Feature: Supplier Onboarding
  Tests the supplier onboarding saga through the STM workflow.
  Covers application submission, document validation, review, approval/rejection, and resubmission.

# ====================================================================
# HAPPY PATH: Submit -> Validate -> Review -> Approve -> Create -> Notify -> Active
# ====================================================================

Scenario: Submit application with all documents
Given that "flowName" equals "onboarding-flow"
And that "initialState" equals "APPLICATION_SUBMITTED"
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "description": "Handmade silk sarees supplier",
    "supplierName": "Silk Weaves India",
    "email": "contact@silkweaves.in",
    "phone": "+91-9876543210",
    "upiId": "silkweaves@upi",
    "address": "123 Weaver Lane, Kanchipuram",
    "commissionPercentage": 12.5
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "onboardingId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.supplierName" is "Silk Weaves India"
And the REST response key "mutatedEntity.email" is "contact@silkweaves.in"

Scenario: Validate documents (APPLICATION_SUBMITTED -> DOCUMENTS_VERIFIED)
Given that "event" equals "validateDocuments"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "comment": "All documents verified"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENTS_VERIFIED"

Scenario: Submit for review (DOCUMENTS_VERIFIED -> UNDER_REVIEW)
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "comment": "Submitting for admin review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve and create supplier (UNDER_REVIEW -> APPROVED)
Given that "event" equals "approve"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "comment": "Application approved by admin"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

# ====================================================================
# REJECTION PATH: Submit -> Validate -> Review -> Reject -> Resubmit
# ====================================================================

Scenario: Submit application for rejection test
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "description": "Supplier for rejection test",
    "supplierName": "Test Supplier",
    "email": "test@supplier.com",
    "phone": "+91-1234567890",
    "upiId": "test@upi",
    "address": "456 Test St"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejectOnboardingId"
And the REST response key "mutatedEntity.currentState.stateId" is "APPLICATION_SUBMITTED"

Scenario: Reject incomplete application (APPLICATION_SUBMITTED -> REJECTED)
Given that "event" equals "rejectApplication"
When I PATCH a REST request to URL "/onboarding/${rejectOnboardingId}/${event}" with payload
"""json
{
    "comment": "Rejecting due to incomplete docs",
    "reason": "GST certificate is missing and business address not verifiable"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectOnboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

Scenario: Resubmit after rejection (REJECTED -> APPLICATION_SUBMITTED)
Given that "event" equals "resubmit"
When I PATCH a REST request to URL "/onboarding/${rejectOnboardingId}/${event}" with payload
"""json
{
    "comment": "Resubmitting with corrected information"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectOnboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPLICATION_SUBMITTED"

# ====================================================================
# REQUEST MORE INFO PATH: Submit -> Validate -> Review -> RequestMoreInfo -> Submit docs -> Verify
# ====================================================================

Scenario: Create application for additional documents test
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "description": "Supplier needing more docs",
    "supplierName": "Artisan Crafts",
    "email": "artisan@crafts.com",
    "phone": "+91-5555555555",
    "upiId": "artisan@upi",
    "address": "789 Craft Ave, Jaipur"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "moreInfoId"

Scenario: Validate documents for more info test
Given that "event" equals "validateDocuments"
When I PATCH a REST request to URL "/onboarding/${moreInfoId}/${event}" with payload
"""json
{
    "comment": "Documents verified"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENTS_VERIFIED"

Scenario: Submit for review for more info test
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/onboarding/${moreInfoId}/${event}" with payload
"""json
{
    "comment": "Submitted for review"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Request additional documents (UNDER_REVIEW -> PENDING_DOCUMENTS)
Given that "event" equals "requestMoreInfo"
When I PATCH a REST request to URL "/onboarding/${moreInfoId}/${event}" with payload
"""json
{
    "comment": "Please provide PAN card copy and bank account proof"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${moreInfoId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_DOCUMENTS"

Scenario: Submit additional documents (PENDING_DOCUMENTS -> DOCUMENTS_VERIFIED)
Given that "event" equals "submitDocuments"
When I PATCH a REST request to URL "/onboarding/${moreInfoId}/${event}" with payload
"""json
{
    "comment": "Uploading PAN and bank proof documents"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${moreInfoId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENTS_VERIFIED"

# ====================================================================
# NOTIFY SUPPLIER: After approval, create supplier record and send welcome notification
# ====================================================================

Scenario: Notify supplier on activation - verify APPROVED state
When I GET a REST request to URL "/onboarding/${onboardingId}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

# ====================================================================
# DAD APPROVAL: Admin review validates before approval
# ====================================================================

Scenario: Dad approval required - reject from review state
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "description": "Supplier needing Dad approval",
    "supplierName": "Premium Silks",
    "email": "premium@silks.com",
    "phone": "+91-7777777777",
    "upiId": "premium@upi",
    "address": "101 Silk Rd, Varanasi"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "dadApprovalId"

Scenario: Validate docs for dad approval test
Given that "event" equals "validateDocuments"
When I PATCH a REST request to URL "/onboarding/${dadApprovalId}/${event}" with payload
"""json
{
    "comment": "Documents OK"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENTS_VERIFIED"

Scenario: Submit for review for dad approval
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/onboarding/${dadApprovalId}/${event}" with payload
"""json
{
    "comment": "Sending for review"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Dad rejects from review (UNDER_REVIEW -> REJECTED)
Given that "event" equals "reject"
When I PATCH a REST request to URL "/onboarding/${dadApprovalId}/${event}" with payload
"""json
{
    "comment": "Dad says no",
    "reason": "Supplier does not meet quality standards for our platform"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${dadApprovalId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

# ====================================================================
# INVALID EVENT
# ====================================================================

Scenario: Send invalid event to onboarding
When I PATCH a REST request to URL "/onboarding/${onboardingId}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
