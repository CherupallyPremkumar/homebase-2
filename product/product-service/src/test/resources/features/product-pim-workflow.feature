Feature: Product Information Management
  Tests the complete PIM workflow including submit for review, approval,
  rejection with mandatory comment, disable, enable, and discontinue flows.
  Validates business rules: required fields for submission, rejection comment
  enforcement, and event publishing on state transitions.

# =============================================================================
# HAPPY PATH: Submit -> Approve -> Published
# =============================================================================

Scenario: Submit product for review
  Given a product "Banarasi Silk Saree" in "DRAFT" state
  And all required fields are filled
  When supplier submits for review
  Then product should be in "UNDER_REVIEW" state

Scenario: Approve product
  Given a product "Kanchipuram Saree" in "UNDER_REVIEW" state
  When admin approves
  Then product should be in "PUBLISHED" state
  And ProductPublishedEvent should be published

# =============================================================================
# VALIDATION: Incomplete product submission
# =============================================================================

Scenario: Reject incomplete product
  Given a product in "DRAFT" state without description
  When supplier submits incomplete product for review
  Then it should fail with "Description is required"

# =============================================================================
# REJECTION FLOW: Under Review -> Draft with mandatory comment
# =============================================================================

Scenario: Reject product with reason
  Given a product "Low Quality Item" in "UNDER_REVIEW" state
  When admin rejects with reason "Images are blurry, description incomplete"
  Then product should be in "DRAFT" state

Scenario: Reject product without comment should fail
  Given a product "Another Item" in "UNDER_REVIEW" state
  When admin rejects without a comment
  Then it should fail with "A comment is required when rejecting a product"

# =============================================================================
# DISABLE/ENABLE FLOW
# =============================================================================

Scenario: Disable product
  Given a published product
  When admin disables it
  Then product should be in "DISABLED" state
  And it should be hidden from catalog

Scenario: Re-enable disabled product
  Given a product "Seasonal Product" in "DISABLED" state
  When admin re-enables it
  Then product should be in "PUBLISHED" state
  And ProductPublishedEvent should be published

# =============================================================================
# DISCONTINUE FLOW: Permanent removal
# =============================================================================

Scenario: Discontinue published product permanently
  Given a published product
  When admin discontinues it
  Then product should be in "DISCONTINUED" state

Scenario: Discontinue disabled product
  Given a product "End of Life Product" in "DISABLED" state
  When admin discontinues it
  Then product should be in "DISCONTINUED" state
