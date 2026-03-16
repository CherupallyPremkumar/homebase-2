Feature: Supplier Onboarding Lifecycle
  Tests the full onboarding lifecycle through the STM workflow with security.
  States: APPLICATION_SUBMITTED -> DOCUMENT_VERIFICATION -> BUSINESS_VERIFICATION -> TRAINING -> ONBOARDED -> COMPLETED
  Side states: DOCUMENTS_REQUESTED, REJECTED

# ====================================================================
# HAPPY PATH: Full lifecycle from submission to completion
# ====================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Submit application with all documents
Given that "flowName" equals "onboarding-flow"
And that "initialState" equals "APPLICATION_SUBMITTED"
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "applicantName": "Rajesh Kumar",
    "businessName": "Silk Weaves India",
    "businessType": "MANUFACTURER",
    "documents": [
        {"type": "TAX_ID", "fileUrl": "https://docs.example.com/tax-id-001.pdf"},
        {"type": "BUSINESS_LICENSE", "fileUrl": "https://docs.example.com/license-001.pdf"},
        {"type": "BANK_PROOF", "fileUrl": "https://docs.example.com/bank-001.pdf"}
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "onboardingId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.businessName" is "Silk Weaves India"
And the REST response key "mutatedEntity.applicantName" is "Rajesh Kumar"

Scenario: Verify documents (APPLICATION_SUBMITTED -> DOCUMENT_VERIFICATION)
Given that "event" equals "verifyDocuments"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "verificationNotes": "All three documents verified successfully",
    "comment": "Documents OK"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENT_VERIFICATION"

Scenario: Verify business (DOCUMENT_VERIFICATION -> BUSINESS_VERIFICATION)
Given that "event" equals "verifyBusiness"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "verificationNotes": "Business registration confirmed with MCA",
    "comment": "Business verified"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "BUSINESS_VERIFICATION"

Scenario: Start training (BUSINESS_VERIFICATION -> TRAINING)
Given that "event" equals "startTraining"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "comment": "Initiating training"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "TRAINING"

Scenario: Complete first training module (stays in TRAINING)
Given that "event" equals "completeTraining"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "moduleId": "PLATFORM_BASICS",
    "comment": "Completed platform basics"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "TRAINING"

Scenario: Complete second training module (stays in TRAINING)
Given that "event" equals "completeTraining"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "moduleId": "SELLER_POLICIES",
    "comment": "Completed seller policies"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "TRAINING"

Scenario: Complete third training module (TRAINING -> CHECK_TRAINING_COMPLETE -> ONBOARDED)
Given that "event" equals "completeTraining"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "moduleId": "SHIPPING_GUIDELINES",
    "comment": "Completed shipping guidelines"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ONBOARDED"

Scenario: Complete onboarding (ONBOARDED -> COMPLETED)
Given that "event" equals "completeOnboarding"
When I PATCH a REST request to URL "/onboarding/${onboardingId}/${event}" with payload
"""json
{
    "comment": "Supplier fully activated"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

# ====================================================================
# REJECTION PATH: Submit -> Verify Docs -> Reject
# ====================================================================

Scenario: Submit application for rejection test
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "applicantName": "Bad Actor",
    "businessName": "Fraudulent Corp",
    "businessType": "RESELLER",
    "documents": [
        {"type": "TAX_ID", "fileUrl": "https://docs.example.com/fake-tax.pdf"},
        {"type": "BUSINESS_LICENSE", "fileUrl": "https://docs.example.com/fake-license.pdf"},
        {"type": "BANK_PROOF", "fileUrl": "https://docs.example.com/fake-bank.pdf"}
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejectOnboardingId"
And the REST response key "mutatedEntity.currentState.stateId" is "APPLICATION_SUBMITTED"

Scenario: Reject application (APPLICATION_SUBMITTED -> REJECTED)
Given that "event" equals "reject"
When I PATCH a REST request to URL "/onboarding/${rejectOnboardingId}/${event}" with payload
"""json
{
    "reason": "Fraudulent business documents detected",
    "comment": "Rejecting fraudulent application"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectOnboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

# ====================================================================
# DOCUMENTS REQUESTED PATH: Submit -> Verify -> Request Docs -> Resubmit -> Verify
# ====================================================================

Scenario: Submit application for document resubmission test
When I POST a REST request to URL "/onboarding" with payload
"""json
{
    "applicantName": "Priya Sharma",
    "businessName": "Artisan Crafts",
    "businessType": "ARTISAN",
    "documents": [
        {"type": "TAX_ID", "fileUrl": "https://docs.example.com/tax-artisan.pdf"},
        {"type": "BUSINESS_LICENSE", "fileUrl": "https://docs.example.com/license-artisan.pdf"},
        {"type": "BANK_PROOF", "fileUrl": "https://docs.example.com/bank-artisan.pdf"}
    ]
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "resubmitId"
And the REST response key "mutatedEntity.currentState.stateId" is "APPLICATION_SUBMITTED"

Scenario: Verify documents for resubmission test
Given that "event" equals "verifyDocuments"
When I PATCH a REST request to URL "/onboarding/${resubmitId}/${event}" with payload
"""json
{
    "verificationNotes": "Docs verified",
    "comment": "Documents OK"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENT_VERIFICATION"

Scenario: Request additional documents (DOCUMENT_VERIFICATION -> DOCUMENTS_REQUESTED)
Given that "event" equals "requestDocuments"
When I PATCH a REST request to URL "/onboarding/${resubmitId}/${event}" with payload
"""json
{
    "requestedDocumentTypes": ["GST_CERTIFICATE"],
    "notes": "Please provide GST registration certificate",
    "comment": "Need GST certificate"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${resubmitId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENTS_REQUESTED"

Scenario: Resubmit documents (DOCUMENTS_REQUESTED -> DOCUMENT_VERIFICATION)
Given that "event" equals "resubmitDocuments"
When I PATCH a REST request to URL "/onboarding/${resubmitId}/${event}" with payload
"""json
{
    "documentTypes": ["GST_CERTIFICATE"],
    "documentNotes": "GST certificate attached",
    "comment": "Resubmitting with GST"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${resubmitId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DOCUMENT_VERIFICATION"

# ====================================================================
# GET: Retrieve onboarding by ID
# ====================================================================

Scenario: Retrieve onboarding application by ID
When I GET a REST request to URL "/onboarding/${onboardingId}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${onboardingId}"
And the REST response key "mutatedEntity.currentState.stateId" is "COMPLETED"

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
