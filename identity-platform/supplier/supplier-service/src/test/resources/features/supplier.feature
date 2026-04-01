Feature: Comprehensive Supplier Lifecycle STM Tests
  Covers ALL state transitions in the supplier state machine:
  APPLIED, UNDER_REVIEW, APPROVED, REJECTED, ACTIVE, ON_PROBATION, SUSPENDED, TERMINATED.
  Includes happy path, unhappy path (invalid transitions), rejection/resubmission,
  and terminal state validation.

  Background:
    When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
    And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

  # ================================================================
  # HAPPY PATH: Full lifecycle from application to activation
  # ================================================================

  Scenario: 1. Create supplier - initial state is APPLIED
    When I POST a REST request to URL "/supplier" with payload
    """json
    {
      "businessName": "Premium Textiles Pvt Ltd",
      "businessType": "COMPANY",
      "contactEmail": "admin@premiumtextiles.com",
      "contactPhone": "9876543210",
      "address": "42 Industrial Area, Surat, Gujarat",
      "taxId": "GSTIN29ABCDE1234F1Z5"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"
    And the REST response key "mutatedEntity.businessName" is "Premium Textiles Pvt Ltd"
    And store "$.payload.mutatedEntity.id" from response to "supplierId"

  Scenario: 2. Retrieve newly created supplier
    When I GET a REST request to URL "/supplier/${supplierId}"
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"
    And the REST response key "mutatedEntity.businessName" is "Premium Textiles Pvt Ltd"

  Scenario: 3. Review supplier application (APPLIED -> UNDER_REVIEW)
    When I PATCH a REST request to URL "/supplier/${supplierId}/reviewSupplier" with payload
    """json
    {
      "comment": "Starting document verification - checking GST, PAN, bank details",
      "reviewNotes": "Documents received, verifying with government databases"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

  Scenario: 4. Approve supplier (UNDER_REVIEW -> APPROVED)
    When I PATCH a REST request to URL "/supplier/${supplierId}/approveSupplier" with payload
    """json
    {
      "comment": "All documents verified successfully, GST valid, bank account confirmed"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  Scenario: 5. Activate supplier after onboarding (APPROVED -> ACTIVE)
    When I PATCH a REST request to URL "/supplier/${supplierId}/activateSupplier" with payload
    """json
    {
      "comment": "Onboarding complete, training finished, ready to list products"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

  # ================================================================
  # PROBATION FLOW: ACTIVE -> ON_PROBATION -> ACTIVE
  # ================================================================

  Scenario: 6. Put active supplier on probation (ACTIVE -> ON_PROBATION)
    When I PATCH a REST request to URL "/supplier/${supplierId}/putOnProbation" with payload
    """json
    {
      "comment": "High return rate detected - 25% returns in last 30 days",
      "reason": "Excessive return rate exceeding 20% threshold"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "ON_PROBATION"

  Scenario: 7. Resolve probation - supplier improved (ON_PROBATION -> ACTIVE)
    When I PATCH a REST request to URL "/supplier/${supplierId}/resolveFromProbation" with payload
    """json
    {
      "comment": "Return rate improved to 8%, below threshold. Probation lifted."
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

  # ================================================================
  # SUSPENSION FLOW: ACTIVE -> SUSPENDED -> ACTIVE
  # ================================================================

  Scenario: 8. Suspend active supplier (ACTIVE -> SUSPENDED)
    When I PATCH a REST request to URL "/supplier/${supplierId}/suspendSupplier" with payload
    """json
    {
      "comment": "Customer complaints about counterfeit products",
      "reason": "Multiple complaints about product authenticity"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

  Scenario: 9. Reactivate suspended supplier (SUSPENDED -> ACTIVE)
    When I PATCH a REST request to URL "/supplier/${supplierId}/reactivateSupplier" with payload
    """json
    {
      "comment": "Investigation complete - complaints resolved, supplier cleared"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

  # ================================================================
  # TERMINATION: ACTIVE -> TERMINATED (terminal state)
  # ================================================================

  Scenario: 10. Terminate supplier (ACTIVE -> TERMINATED)
    When I PATCH a REST request to URL "/supplier/${supplierId}/terminateSupplier" with payload
    """json
    {
      "comment": "Supplier violated platform terms of service - permanent ban",
      "reason": "Repeated policy violations: selling prohibited items"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${supplierId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "TERMINATED"

  # ================================================================
  # UNHAPPY PATH: Terminal state rejects all events
  # ================================================================

  Scenario: 11. Cannot reactivate a terminated supplier (terminal state)
    When I PATCH a REST request to URL "/supplier/${supplierId}/reactivateSupplier" with payload
    """json
    {
      "comment": "Attempting to reactivate terminated supplier"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  Scenario: 12. Cannot suspend a terminated supplier (terminal state)
    When I PATCH a REST request to URL "/supplier/${supplierId}/suspendSupplier" with payload
    """json
    {
      "comment": "Attempting to suspend terminated supplier"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  # ================================================================
  # REJECTION AND RESUBMISSION FLOW
  # ================================================================

  Scenario: 13. Create second supplier for rejection flow
    When I POST a REST request to URL "/supplier" with payload
    """json
    {
      "businessName": "Rejected Fabrics LLC",
      "businessType": "INDIVIDUAL",
      "contactEmail": "info@rejectedfabrics.com",
      "contactPhone": "9123456789"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"
    And store "$.payload.mutatedEntity.id" from response to "rejectedId"

  Scenario: 14. Review second supplier (APPLIED -> UNDER_REVIEW)
    When I PATCH a REST request to URL "/supplier/${rejectedId}/reviewSupplier" with payload
    """json
    {
      "comment": "Reviewing application documents"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

  Scenario: 15. Reject supplier (UNDER_REVIEW -> REJECTED)
    When I PATCH a REST request to URL "/supplier/${rejectedId}/rejectSupplier" with payload
    """json
    {
      "comment": "GST certificate expired, bank account not matching business name",
      "reason": "Invalid GST certificate and bank account mismatch"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${rejectedId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "REJECTED"

  Scenario: 16. Resubmit rejected supplier (REJECTED -> APPLIED)
    When I PATCH a REST request to URL "/supplier/${rejectedId}/resubmitSupplier" with payload
    """json
    {
      "comment": "Updated GST certificate and corrected bank details. Please re-review."
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${rejectedId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"

  Scenario: 17. Re-review and approve resubmitted supplier
    When I PATCH a REST request to URL "/supplier/${rejectedId}/reviewSupplier" with payload
    """json
    {
      "comment": "Re-reviewing updated documents"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

  Scenario: 18. Approve resubmitted supplier
    When I PATCH a REST request to URL "/supplier/${rejectedId}/approveSupplier" with payload
    """json
    {
      "comment": "Documents verified on resubmission, approved"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${rejectedId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  # ================================================================
  # UNHAPPY PATH: Invalid transitions (skipping states)
  # ================================================================

  Scenario: 19. Cannot approve directly from APPLIED (must review first)
    When I POST a REST request to URL "/supplier" with payload
    """json
    {
      "businessName": "Skip Review Corp",
      "businessType": "COMPANY",
      "contactEmail": "skip@review.com"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "APPLIED"
    And store "$.payload.mutatedEntity.id" from response to "skipId"

  Scenario: 20. Attempt approveSupplier from APPLIED state - must fail
    When I PATCH a REST request to URL "/supplier/${skipId}/approveSupplier" with payload
    """json
    {
      "comment": "Trying to approve without review"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  Scenario: 21. Cannot suspend from APPLIED state
    When I PATCH a REST request to URL "/supplier/${skipId}/suspendSupplier" with payload
    """json
    {
      "comment": "Trying to suspend from APPLIED"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  Scenario: 22. Cannot terminate from APPLIED state
    When I PATCH a REST request to URL "/supplier/${skipId}/terminateSupplier" with payload
    """json
    {
      "comment": "Trying to terminate from APPLIED"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  Scenario: 23. Invalid event name returns error
    When I PATCH a REST request to URL "/supplier/${skipId}/nonExistentEvent" with payload
    """json
    {
      "comment": "Invalid event"
    }
    """
    Then the REST response does not contain key "mutatedEntity"
    And the http status code is 422

  # ================================================================
  # PROBATION TO SUSPENSION AND TERMINATION
  # ================================================================

  Scenario: 24. Create supplier and get to ON_PROBATION for suspend test
    When I POST a REST request to URL "/supplier" with payload
    """json
    {
      "businessName": "Probation Test Supplier",
      "businessType": "COMPANY",
      "contactEmail": "probation@test.com"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "probationId"

  Scenario: 25. Move through APPLIED -> UNDER_REVIEW -> APPROVED -> ACTIVE -> ON_PROBATION
    When I PATCH a REST request to URL "/supplier/${probationId}/reviewSupplier" with payload
    """json
    { "comment": "Review" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

  Scenario: 26. Approve
    When I PATCH a REST request to URL "/supplier/${probationId}/approveSupplier" with payload
    """json
    { "comment": "Approved" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  Scenario: 27. Activate
    When I PATCH a REST request to URL "/supplier/${probationId}/activateSupplier" with payload
    """json
    { "comment": "Activated" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

  Scenario: 28. Put on probation
    When I PATCH a REST request to URL "/supplier/${probationId}/putOnProbation" with payload
    """json
    { "comment": "Performance issues", "reason": "Low fulfillment rate" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "ON_PROBATION"

  Scenario: 29. Suspend from probation (ON_PROBATION -> SUSPENDED)
    When I PATCH a REST request to URL "/supplier/${probationId}/suspendSupplier" with payload
    """json
    {
      "comment": "Performance did not improve during probation",
      "reason": "Failed to meet improvement targets"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${probationId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

  Scenario: 30. Terminate from suspended (SUSPENDED -> TERMINATED)
    When I PATCH a REST request to URL "/supplier/${probationId}/terminateSupplier" with payload
    """json
    {
      "comment": "Supplier consistently failed to meet standards",
      "reason": "Repeated policy violations after suspension"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${probationId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "TERMINATED"

  # ================================================================
  # PROBATION TO TERMINATION (direct)
  # ================================================================

  Scenario: 31. Create supplier for ON_PROBATION -> TERMINATED test
    When I POST a REST request to URL "/supplier" with payload
    """json
    {
      "businessName": "Direct Terminate Test",
      "businessType": "INDIVIDUAL",
      "contactEmail": "terminate@test.com"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "terminateId"

  Scenario: 32. Fast-track to ON_PROBATION
    When I PATCH a REST request to URL "/supplier/${terminateId}/reviewSupplier" with payload
    """json
    { "comment": "Review" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

  Scenario: 33. Approve
    When I PATCH a REST request to URL "/supplier/${terminateId}/approveSupplier" with payload
    """json
    { "comment": "Approved" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "APPROVED"

  Scenario: 34. Activate
    When I PATCH a REST request to URL "/supplier/${terminateId}/activateSupplier" with payload
    """json
    { "comment": "Activated" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

  Scenario: 35. Put on probation
    When I PATCH a REST request to URL "/supplier/${terminateId}/putOnProbation" with payload
    """json
    { "comment": "Poor performance", "reason": "Low rating 1.5" }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "ON_PROBATION"

  Scenario: 36. Terminate directly from probation (ON_PROBATION -> TERMINATED)
    When I PATCH a REST request to URL "/supplier/${terminateId}/terminateSupplier" with payload
    """json
    {
      "comment": "Fraud detected during probation period",
      "reason": "Fraudulent activity confirmed"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.id" is "${terminateId}"
    And the REST response key "mutatedEntity.currentState.stateId" is "TERMINATED"
