Feature: RuleSet Lifecycle — tests the full STM through all paths.
  Covers the happy path (DRAFT -> REVIEW -> ACTIVE -> DEPRECATED),
  direct activation, deactivation, revert, and invalid transition rejection.

# =============================================================================
# HAPPY PATH: Create -> submit -> REVIEW -> approve -> ACTIVE -> deprecate -> DEPRECATED
# =============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new RuleSet in DRAFT state
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Test Volume Discount Rules",
      "description": "Volume-based pricing rules for test",
      "targetModule": "PRICING",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "bulk-order-discount",
              "expression": "#totalQuantity >= 10",
              "effect": "ALLOW",
              "priority": 100,
              "active": true
          },
          {
              "name": "multi-buy-discount",
              "expression": "#totalQuantity >= 3",
              "effect": "ALLOW",
              "priority": 50,
              "active": true
          }
      ]
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "ruleSetId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  And the REST response key "mutatedEntity.name" is "Test Volume Discount Rules"

Scenario: Retrieve the RuleSet that was just created
  When I GET a REST request to URL "/ruleSet/${ruleSetId}"
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${ruleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  And the REST response key "mutatedEntity.targetModule" is "PRICING"

Scenario: Submit the RuleSet for review (DRAFT -> REVIEW)
  Given that "event" equals "submit"
  When I PATCH a REST request to URL "/ruleSet/${ruleSetId}/${event}" with payload
  """json
  {
      "comments": "Ready for review"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${ruleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "REVIEW"

Scenario: Approve the RuleSet (REVIEW -> ACTIVE)
  Given that "event" equals "approve"
  When I PATCH a REST request to URL "/ruleSet/${ruleSetId}/${event}" with payload
  """json
  {
      "comments": "Looks good, approved"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${ruleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Deprecate the active RuleSet (ACTIVE -> DEPRECATED)
  Given that "event" equals "deprecate"
  When I PATCH a REST request to URL "/ruleSet/${ruleSetId}/${event}" with payload
  """json
  {
      "comments": "Replaced by new discount policy"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${ruleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "DEPRECATED"

Scenario: Reactivate deprecated RuleSet (DEPRECATED -> ACTIVE)
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/ruleSet/${ruleSetId}/${event}" with payload
  """json
  {
      "comments": "Re-enabling volume discounts"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${ruleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# =============================================================================
# DEACTIVATE AND REVERT FLOW: Create -> submit -> revert -> submit -> approve -> deactivate
# =============================================================================

Scenario: Create a RuleSet for revert test
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Checkout Validation Rules",
      "description": "Rules for validating checkout conditions",
      "targetModule": "CHECKOUT",
      "defaultEffect": "ALLOW",
      "rules": [
          {
              "name": "block-cod-above-limit",
              "expression": "#paymentMethod == 'COD' && #orderTotal > 1000000",
              "effect": "DENY",
              "priority": 100,
              "active": true
          }
      ]
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "checkoutRuleSetId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Submit checkout rules (DRAFT -> REVIEW)
  Given that "event" equals "submit"
  When I PATCH a REST request to URL "/ruleSet/${checkoutRuleSetId}/${event}" with payload
  """json
  {
      "comments": "Submit for review"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "REVIEW"

Scenario: Revert checkout rules back to draft (REVIEW -> DRAFT)
  Given that "event" equals "revert"
  When I PATCH a REST request to URL "/ruleSet/${checkoutRuleSetId}/${event}" with payload
  """json
  {
      "comments": "Needs threshold adjustment before approval"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${checkoutRuleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Re-submit and approve checkout rules
  Given that "event" equals "submit"
  When I PATCH a REST request to URL "/ruleSet/${checkoutRuleSetId}/${event}" with payload
  """json
  {
      "comments": "Resubmitting after threshold fix"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "REVIEW"
  Given that "event" equals "approve"
  When I PATCH a REST request to URL "/ruleSet/${checkoutRuleSetId}/${event}" with payload
  """json
  {
      "comments": "Approved after revision"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Deactivate active rules back to DRAFT (ACTIVE -> DRAFT)
  Given that "event" equals "deactivate"
  When I PATCH a REST request to URL "/ruleSet/${checkoutRuleSetId}/${event}" with payload
  """json
  {
      "comments": "Temporarily deactivating for A/B testing"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${checkoutRuleSetId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

# =============================================================================
# DIRECT ACTIVATION: Create -> activate (skip REVIEW)
# =============================================================================

Scenario: Create and directly activate a RuleSet (DRAFT -> ACTIVE)
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Order Cancellation Rules",
      "description": "Controls when orders can be cancelled",
      "targetModule": "ORDER",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "allow-cancel-before-shipping",
              "expression": "#fulfillmentStatus == 'PENDING'",
              "effect": "ALLOW",
              "priority": 100,
              "active": true
          }
      ]
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "directActivateId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/ruleSet/${directActivateId}/${event}" with payload
  """json
  {
      "comments": "Direct activation by admin"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.id" is "${directActivateId}"
  And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# =============================================================================
# INVALID TRANSITIONS
# =============================================================================

Scenario: Create a DRAFT RuleSet for invalid transition test
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Invalid Transition Test",
      "description": "Testing invalid transitions",
      "targetModule": "RETURN",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "test-rule",
              "expression": "true",
              "effect": "ALLOW",
              "priority": 1,
              "active": true
          }
      ]
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "invalidTestId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Attempt to approve a DRAFT RuleSet — should fail (approve only from REVIEW)
  Given that "event" equals "approve"
  When I PATCH a REST request to URL "/ruleSet/${invalidTestId}/${event}" with payload
  """json
  {
      "comments": "Cannot approve a draft"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

Scenario: Attempt to deprecate a DRAFT RuleSet — should fail
  Given that "event" equals "deprecate"
  When I PATCH a REST request to URL "/ruleSet/${invalidTestId}/${event}" with payload
  """json
  {
      "comments": "Cannot deprecate a draft"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

Scenario: Send an invalid event — should fail
  When I PATCH a REST request to URL "/ruleSet/${invalidTestId}/nonExistentEvent" with payload
  """json
  {
      "comments": "Invalid event"
  }
  """
  Then the REST response does not contain key "mutatedEntity"
  And the http status code is 422

# =============================================================================
# VALIDATION: Missing required fields should reject with 400
# =============================================================================

Scenario: Create RuleSet without name — should fail with 11001
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "targetModule": "PRICING",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "test",
              "expression": "true",
              "effect": "ALLOW",
              "priority": 1,
              "active": true
          }
      ]
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 11001
  And the REST response does not contain key "mutatedEntity"

Scenario: Create RuleSet without targetModule — should fail with 11003
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Missing Module Rules",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "test",
              "expression": "true",
              "effect": "ALLOW",
              "priority": 1,
              "active": true
          }
      ]
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 11003
  And the REST response does not contain key "mutatedEntity"

Scenario: Submit RuleSet without rules — should fail with 11004
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Empty RuleSet",
      "targetModule": "PRICING",
      "defaultEffect": "DENY"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "emptyRuleSetId"
  Given that "event" equals "submit"
  When I PATCH a REST request to URL "/ruleSet/${emptyRuleSetId}/${event}" with payload
  """json
  {
      "comments": "Trying to submit empty ruleset"
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 11004
  And the REST response does not contain key "mutatedEntity"

Scenario: Deprecate without comment — should fail with 11009
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Comment Test RuleSet",
      "targetModule": "ORDER",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "test-rule",
              "expression": "true",
              "effect": "ALLOW",
              "priority": 1,
              "active": true
          }
      ]
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "commentTestId"
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/ruleSet/${commentTestId}/${event}" with payload
  """json
  {
      "comments": "Direct activate"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"
  Given that "event" equals "deprecate"
  When I PATCH a REST request to URL "/ruleSet/${commentTestId}/${event}" with payload
  """json
  {
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 11009
  And the REST response does not contain key "mutatedEntity"

Scenario: Revert without comment — should fail with 11009
  When I POST a REST request to URL "/ruleSet" with payload
  """json
  {
      "name": "Revert Comment Test",
      "targetModule": "OFFER",
      "defaultEffect": "DENY",
      "rules": [
          {
              "name": "test-rule",
              "expression": "true",
              "effect": "ALLOW",
              "priority": 1,
              "active": true
          }
      ]
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "revertCommentId"
  Given that "event" equals "submit"
  When I PATCH a REST request to URL "/ruleSet/${revertCommentId}/${event}" with payload
  """json
  {
      "comments": "Submit for review"
  }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "REVIEW"
  Given that "event" equals "revert"
  When I PATCH a REST request to URL "/ruleSet/${revertCommentId}/${event}" with payload
  """json
  {
  }
  """
  Then success is false
  And the http status code is 400
  And the top level subErrorCode is 11009
  And the REST response does not contain key "mutatedEntity"
