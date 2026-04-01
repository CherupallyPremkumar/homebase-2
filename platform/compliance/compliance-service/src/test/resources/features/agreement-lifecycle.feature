Feature: Agreement Lifecycle — tests the full agreement STM through all paths.
  Covers the happy path (DRAFT -> PUBLISHED -> SUPERSEDED),
  suspend/reinstate flow, and invalid transition rejection.

# =============================================================================
# HAPPY PATH: Create -> publish -> PUBLISHED -> supersede -> SUPERSEDED
# =============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new agreement in DRAFT state
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Seller Agreement v3",
      "agreementType": "SELLER_AGREEMENT",
      "versionLabel": "3.0",
      "contentUrl": "https://legal.homebase.com/agreements/seller-v3",
      "contentHash": "sha256-abc123",
      "effectiveDate": "2026-04-01",
      "expiryDate": "2027-03-31",
      "mandatoryAcceptance": true
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "agreementId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  And the REST response key "mutatedEntity.title" is "Seller Agreement v3"

Scenario: Retrieve the agreement that was just created
  When I GET a REST request to URL "/agreement/${agreementId}"
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${agreementId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  And the REST response key "mutatedEntity.agreementType" is "SELLER_AGREEMENT"

Scenario: Publish the agreement (DRAFT -> PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/agreement/${agreementId}/${event}" with payload
  """json
  {
      "title": "Seller Agreement v3",
      "agreementType": "SELLER_AGREEMENT",
      "versionLabel": "3.0",
      "contentUrl": "https://legal.homebase.com/agreements/seller-v3",
      "contentHash": "sha256-abc123",
      "effectiveDate": "2026-04-01",
      "expiryDate": "2027-03-31",
      "mandatoryAcceptance": true
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${agreementId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Verify published agreement is retrievable
  When I GET a REST request to URL "/agreement/${agreementId}"
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"
  And the REST response key "mutatedEntity.mandatoryAcceptance" is "true"

Scenario: Create a second agreement to supersede the first
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Seller Agreement v4",
      "agreementType": "SELLER_AGREEMENT",
      "versionLabel": "4.0",
      "contentUrl": "https://legal.homebase.com/agreements/seller-v4",
      "contentHash": "sha256-def456",
      "effectiveDate": "2026-07-01",
      "mandatoryAcceptance": true
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "newAgreementId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Supersede the published agreement (PUBLISHED -> SUPERSEDED)
  Given that "event" equals "supersede"
  When I PATCH a REST request to URL "/agreement/${agreementId}/${event}" with payload
  """json
  {
      "supersededById": "${newAgreementId}",
      "reason": "Updated terms for FY2026-27"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${agreementId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "SUPERSEDED"

Scenario: Verify superseded agreement is final — publish should fail
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/agreement/${agreementId}/${event}" with payload
  """json
  {
      "title": "Should not work"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

# =============================================================================
# SUSPEND / REINSTATE FLOW: Create -> publish -> suspend -> reinstate -> retire
# =============================================================================

Scenario: Create agreement for suspend test
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Privacy Policy v2",
      "agreementType": "PRIVACY_POLICY",
      "versionLabel": "2.0",
      "contentUrl": "https://legal.homebase.com/privacy-v2",
      "contentHash": "sha256-priv2",
      "effectiveDate": "2026-04-01",
      "mandatoryAcceptance": false
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "suspendId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Publish the privacy policy (DRAFT -> PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/agreement/${suspendId}/${event}" with payload
  """json
  {
      "title": "Privacy Policy v2",
      "agreementType": "PRIVACY_POLICY",
      "versionLabel": "2.0",
      "contentUrl": "https://legal.homebase.com/privacy-v2",
      "contentHash": "sha256-priv2",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Suspend the agreement (PUBLISHED -> SUSPENDED)
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/agreement/${suspendId}/${event}" with payload
  """json
  {
      "comment": "Legal review required due to DPDP 2023 changes"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${suspendId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Reinstate the agreement (SUSPENDED -> PUBLISHED)
  Given that "event" equals "reinstate"
  When I PATCH a REST request to URL "/agreement/${suspendId}/${event}" with payload
  """json
  {
      "comment": "Legal review complete, policy compliant"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${suspendId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Retire the reinstated agreement (PUBLISHED -> RETIRED)
  Given that "event" equals "retire"
  When I PATCH a REST request to URL "/agreement/${suspendId}/${event}" with payload
  """json
  {
      "comment": "End of life — replaced by new agreement"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${suspendId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "RETIRED"

Scenario: Verify retired agreement is final — reinstate should fail
  Given that "event" equals "reinstate"
  When I PATCH a REST request to URL "/agreement/${suspendId}/${event}" with payload
  """json
  {
      "comment": "Try to reinstate retired agreement"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

# =============================================================================
# INVALID: Suspend from DRAFT — should fail (no suspend transition on DRAFT)
# =============================================================================

Scenario: Create a DRAFT agreement for invalid transition test
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Test Invalid Transition",
      "agreementType": "DATA_PROCESSING",
      "versionLabel": "1.0",
      "contentUrl": "https://legal.homebase.com/dpa-v1",
      "contentHash": "sha256-dpa1",
      "effectiveDate": "2026-05-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "draftOnlyId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Attempt to suspend a DRAFT agreement — should fail
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/agreement/${draftOnlyId}/${event}" with payload
  """json
  {
      "comment": "Cannot suspend a draft"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

Scenario: Send an invalid event to agreement — should fail
  When I PATCH a REST request to URL "/agreement/${draftOnlyId}/nonExistentEvent" with payload
  """json
  {
      "comment": "Invalid event"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

# =============================================================================
# VALIDATION: Missing required fields should reject with 400
# =============================================================================

Scenario: Create agreement without title — should fail with 10001
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "agreementType": "SELLER_AGREEMENT",
      "versionLabel": "1.0",
      "contentUrl": "https://legal.homebase.com/test",
      "contentHash": "sha256-test",
      "effectiveDate": "2026-06-01"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10001
  And the REST response does not contain key "mutatedEntity"

Scenario: Create agreement without agreementType — should fail with 10003
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Test Agreement",
      "versionLabel": "1.0",
      "contentUrl": "https://legal.homebase.com/test",
      "contentHash": "sha256-test",
      "effectiveDate": "2026-06-01"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10003
  And the REST response does not contain key "mutatedEntity"

Scenario: Publish agreement without contentHash — should fail with 10007
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Hash Test Agreement",
      "agreementType": "DATA_PROCESSING",
      "versionLabel": "1.0",
      "contentUrl": "https://legal.homebase.com/hash-test",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "noHashId"
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/agreement/${noHashId}/${event}" with payload
  """json
  {
      "comment": "Try publish without hash"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10007
  And the REST response does not contain key "mutatedEntity"

Scenario: Publish agreement with past effective date — should fail with 10008
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Past Date Agreement",
      "agreementType": "DATA_PROCESSING",
      "versionLabel": "1.0",
      "contentUrl": "https://legal.homebase.com/past-date",
      "contentHash": "sha256-past",
      "effectiveDate": "2026-01-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "pastDateId"
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/agreement/${pastDateId}/${event}" with payload
  """json
  {
      "effectiveDate": "2026-01-01",
      "contentHash": "sha256-past"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10008
  And the REST response does not contain key "mutatedEntity"

Scenario: Suspend agreement without comment — should fail with 10010
  When I POST a REST request to URL "/agreement" with payload
  """json
  {
      "title": "Comment Test Agreement",
      "agreementType": "SELLER_AGREEMENT",
      "versionLabel": "1.0",
      "contentUrl": "https://legal.homebase.com/comment-test",
      "contentHash": "sha256-comment",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "commentTestId"
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/agreement/${commentTestId}/${event}" with payload
  """json
  {
      "contentHash": "sha256-comment",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/agreement/${commentTestId}/${event}" with payload
  """json
  {
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10010
  And the REST response does not contain key "mutatedEntity"

Scenario: Supersede agreement without supersededById — should fail with 10009
  Given that "event" equals "supersede"
  When I PATCH a REST request to URL "/agreement/${commentTestId}/${event}" with payload
  """json
  {
      "reason": "Missing superseded-by ID"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10009
  And the REST response does not contain key "mutatedEntity"
