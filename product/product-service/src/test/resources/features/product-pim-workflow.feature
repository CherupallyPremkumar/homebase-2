Feature: Product Information Management
  Tests the complete PIM workflow including submit for review, approval,
  rejection with comment, disable, enable, and discontinue flows.

# =============================================================================
# HAPPY PATH: Submit -> Approve -> Published
# =============================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: PIM — Create product and submit for review (DRAFT -> UNDER_REVIEW)
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Banarasi Silk Saree",
    "description": "Handwoven silk saree from Varanasi",
    "brand": "HomeBase Originals"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "pimId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${pimId}/${event}" with payload
"""json
{
    "comment": "All fields complete, submitting for review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${pimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: PIM — Approve product (UNDER_REVIEW -> PUBLISHED)
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${pimId}/${event}" with payload
"""json
{
    "comment": "Quality checks passed, approved"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${pimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

# =============================================================================
# REJECTION FLOW: Under Review -> Draft with comment
# =============================================================================

Scenario: PIM — Create product for rejection flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Low Quality Item",
    "description": "Product with quality issues",
    "brand": "Unknown Brand"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejectPimId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: PIM — Submit rejection product for review
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${rejectPimId}/${event}" with payload
"""json
{
    "comment": "Submitting for review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: PIM — Reject product with reason (UNDER_REVIEW -> DRAFT)
Given that "event" equals "rejectProduct"
When I PATCH a REST request to URL "/product/${rejectPimId}/${event}" with payload
"""json
{
    "comment": "Images are blurry, description incomplete"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectPimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

# =============================================================================
# DISABLE/ENABLE FLOW
# =============================================================================

Scenario: PIM — Create product for disable/enable flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Seasonal Product",
    "description": "Available only in certain seasons",
    "brand": "HomeBase Seasonal"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "disablePimId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: PIM — Move disable product through to PUBLISHED
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${disablePimId}/${event}" with payload
"""json
{
    "comment": "Ready for review"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: PIM — Approve disable product
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${disablePimId}/${event}" with payload
"""json
{
    "comment": "Approved"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: PIM — Disable the published product (PUBLISHED -> DISABLED)
Given that "event" equals "disableProduct"
When I PATCH a REST request to URL "/product/${disablePimId}/${event}" with payload
"""json
{
    "comment": "Out of season, temporarily disabling"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${disablePimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISABLED"

Scenario: PIM — Re-enable the disabled product (DISABLED -> PUBLISHED)
Given that "event" equals "enableProduct"
When I PATCH a REST request to URL "/product/${disablePimId}/${event}" with payload
"""json
{
    "comment": "Season started, re-enabling"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${disablePimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

# =============================================================================
# DISCONTINUE FLOW: Permanent removal
# =============================================================================

Scenario: PIM — Discontinue published product (PUBLISHED -> DISCONTINUED)
Given that "event" equals "discontinueProduct"
When I PATCH a REST request to URL "/product/${disablePimId}/${event}" with payload
"""json
{
    "comment": "Product end-of-life, permanently discontinuing"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${disablePimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"

Scenario: PIM — Create product for disable-then-discontinue flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "End of Life Product",
    "description": "Product reaching end of life",
    "brand": "HomeBase EOL"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "eolPimId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: PIM — Move EOL product to PUBLISHED
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${eolPimId}/${event}" with payload
"""json
{
    "comment": "Ready"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: PIM — Approve EOL product
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${eolPimId}/${event}" with payload
"""json
{
    "comment": "Approved"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: PIM — Disable EOL product
Given that "event" equals "disableProduct"
When I PATCH a REST request to URL "/product/${eolPimId}/${event}" with payload
"""json
{
    "comment": "Disabling before discontinue"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DISABLED"

Scenario: PIM — Discontinue disabled product (DISABLED -> DISCONTINUED)
Given that "event" equals "discontinueProduct"
When I PATCH a REST request to URL "/product/${eolPimId}/${event}" with payload
"""json
{
    "comment": "Permanently discontinuing disabled product"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${eolPimId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"
