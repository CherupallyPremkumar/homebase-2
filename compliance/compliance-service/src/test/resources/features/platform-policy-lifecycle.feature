Feature: Platform Policy Lifecycle — tests the full policy STM through all paths.
  Covers the happy path (DRAFT -> PUBLISHED -> AMENDED -> PUBLISHED),
  suspend flow, retire flow, and invalid transition rejection.

# =============================================================================
# HAPPY PATH: Create -> publish -> PUBLISHED -> amend -> AMENDED -> publish -> PUBLISHED
# =============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new platform policy in DRAFT state
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "Return & Refund Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/returns",
      "contentHash": "sha256-ret1",
      "summaryText": "Items can be returned within 7 days of delivery",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "policyId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  And the REST response key "mutatedEntity.title" is "Return & Refund Policy"

Scenario: Retrieve the policy that was just created
  When I GET a REST request to URL "/platform-policy/${policyId}"
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${policyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  And the REST response key "mutatedEntity.policyCategory" is "CUSTOMER_FACING"

Scenario: Publish the policy (DRAFT -> PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${policyId}/${event}" with payload
  """json
  {
      "title": "Return & Refund Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/returns",
      "contentHash": "sha256-ret1",
      "summaryText": "Items can be returned within 7 days of delivery",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${policyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Amend the published policy (PUBLISHED -> AMENDED)
  Given that "event" equals "amend"
  When I PATCH a REST request to URL "/platform-policy/${policyId}/${event}" with payload
  """json
  {
      "title": "Return & Refund Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.1",
      "contentUrl": "https://homebase.com/policies/returns-v1.1",
      "contentHash": "sha256-ret11",
      "summaryText": "Items can be returned within 10 days of delivery (updated per CPA 2019)",
      "effectiveDate": "2026-05-01",
      "reason": "Updated return window per Consumer Protection Act 2019"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${policyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "AMENDED"

Scenario: Re-publish the amended policy (AMENDED -> PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${policyId}/${event}" with payload
  """json
  {
      "title": "Return & Refund Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.1",
      "contentUrl": "https://homebase.com/policies/returns-v1.1",
      "contentHash": "sha256-ret11",
      "summaryText": "Items can be returned within 10 days of delivery (updated per CPA 2019)",
      "effectiveDate": "2026-05-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${policyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

# =============================================================================
# SUSPEND AND RETIRE FLOW: Create -> publish -> suspend -> retire
# =============================================================================

Scenario: Create a policy for suspend/retire test
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "Cancellation Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/cancellation",
      "contentHash": "sha256-cancel1",
      "summaryText": "Orders can be cancelled before shipment",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "cancelPolicyId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Publish cancellation policy (DRAFT -> PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${cancelPolicyId}/${event}" with payload
  """json
  {
      "title": "Cancellation Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/cancellation",
      "contentHash": "sha256-cancel1",
      "summaryText": "Orders can be cancelled before shipment",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Suspend the policy (PUBLISHED -> SUSPENDED)
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/platform-policy/${cancelPolicyId}/${event}" with payload
  """json
  {
      "comment": "Temporarily suspended for legal review"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${cancelPolicyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Retire the suspended policy (SUSPENDED -> RETIRED)
  Given that "event" equals "retire"
  When I PATCH a REST request to URL "/platform-policy/${cancelPolicyId}/${event}" with payload
  """json
  {
      "comment": "Policy permanently retired — replaced by new cancellation policy"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${cancelPolicyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "RETIRED"

Scenario: Verify retired policy is final — publish should fail
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${cancelPolicyId}/${event}" with payload
  """json
  {
      "title": "Should not work"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

# =============================================================================
# SUSPENDED -> PUBLISHED (reinstate via publish)
# =============================================================================

Scenario: Create policy for suspend-then-republish test
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "Seller Code of Conduct",
      "policyCategory": "SELLER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/seller-conduct",
      "contentHash": "sha256-conduct1",
      "summaryText": "Standards of behavior for all marketplace sellers",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "conductPolicyId"

Scenario: Publish seller conduct policy
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${conductPolicyId}/${event}" with payload
  """json
  {
      "title": "Seller Code of Conduct",
      "policyCategory": "SELLER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/seller-conduct",
      "contentHash": "sha256-conduct1",
      "summaryText": "Standards of behavior for all marketplace sellers",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Suspend seller conduct policy
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/platform-policy/${conductPolicyId}/${event}" with payload
  """json
  {
      "comment": "Under review for compliance with E-Commerce Rules 2020"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Republish the suspended policy (SUSPENDED -> PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${conductPolicyId}/${event}" with payload
  """json
  {
      "title": "Seller Code of Conduct",
      "policyCategory": "SELLER_FACING",
      "versionLabel": "1.1",
      "contentUrl": "https://homebase.com/policies/seller-conduct-v1.1",
      "contentHash": "sha256-conduct11",
      "summaryText": "Updated standards compliant with E-Commerce Rules 2020",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${conductPolicyId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

# =============================================================================
# INVALID TRANSITIONS
# =============================================================================

Scenario: Create a DRAFT policy for invalid transition test
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "Test Invalid Transition",
      "policyCategory": "INTERNAL",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/test",
      "contentHash": "sha256-test",
      "effectiveDate": "2026-04-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "draftPolicyId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Attempt to amend a DRAFT policy — should fail
  Given that "event" equals "amend"
  When I PATCH a REST request to URL "/platform-policy/${draftPolicyId}/${event}" with payload
  """json
  {
      "comment": "Cannot amend a draft"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

Scenario: Attempt to suspend a DRAFT policy — should fail
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/platform-policy/${draftPolicyId}/${event}" with payload
  """json
  {
      "comment": "Cannot suspend a draft"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

Scenario: Send an invalid event — should fail
  When I PATCH a REST request to URL "/platform-policy/${draftPolicyId}/nonExistentEvent" with payload
  """json
  {
      "comment": "Invalid event"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

# =============================================================================
# VALIDATION: Missing required fields should reject
# =============================================================================

Scenario: Create policy without title — should fail with 10011
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/test",
      "contentHash": "sha256-test",
      "effectiveDate": "2026-06-01"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10011
  And the REST response does not contain key "mutatedEntity"

Scenario: Create policy without policyCategory — should fail with 10013
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "Test Policy",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/test",
      "contentHash": "sha256-test",
      "effectiveDate": "2026-06-01"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10013
  And the REST response does not contain key "mutatedEntity"

Scenario: Publish policy without summaryText — should fail with 10016
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "No Summary Policy",
      "policyCategory": "CUSTOMER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/no-summary",
      "contentHash": "sha256-nosum",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "noSummaryId"
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${noSummaryId}/${event}" with payload
  """json
  {
      "comment": "Publish without summary"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10016
  And the REST response does not contain key "mutatedEntity"

Scenario: Amend policy without reason — should fail with 10019
  When I POST a REST request to URL "/platform-policy" with payload
  """json
  {
      "title": "Amend No Reason Policy",
      "policyCategory": "SELLER_FACING",
      "versionLabel": "1.0",
      "contentUrl": "https://homebase.com/policies/amend-test",
      "contentHash": "sha256-amend",
      "summaryText": "Test policy for amend validation",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "amendTestId"
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/platform-policy/${amendTestId}/${event}" with payload
  """json
  {
      "contentHash": "sha256-amend",
      "summaryText": "Test policy for amend validation",
      "effectiveDate": "2026-06-01"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"
  Given that "event" equals "amend"
  When I PATCH a REST request to URL "/platform-policy/${amendTestId}/${event}" with payload
  """json
  {
      "contentUrl": "https://homebase.com/policies/amend-test-v2"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10019
  And the REST response does not contain key "mutatedEntity"

Scenario: Suspend policy without comment — should fail with 10010
  Given that "event" equals "suspend"
  When I PATCH a REST request to URL "/platform-policy/${amendTestId}/${event}" with payload
  """json
  {
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 10010
  And the REST response does not contain key "mutatedEntity"
