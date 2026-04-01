Feature: Product Lifecycle — tests the full product state machine through all paths.
  Covers the happy path (DRAFT -> UNDER_REVIEW -> PUBLISHED -> DISCONTINUED),
  the rejection flow (UNDER_REVIEW -> DRAFT), the disable/enable flow,
  and invalid event handling.

# ═══════════════════════════════════════════════════════════════════════════════
# HAPPY PATH: Create -> Submit -> Approve -> Discontinue
# ═══════════════════════════════════════════════════════════════════════════════

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new product in DRAFT state
Given that "flowName" equals "product-flow"
And that "initialState" equals "DRAFT"
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Handmade Silk Scarf",
    "description": "Pure silk scarf with hand-block print",
    "brand": "HomeBase Originals"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.name" is "Handmade Silk Scarf"
And the REST response key "mutatedEntity.description" is "Pure silk scarf with hand-block print"
And the REST response key "mutatedEntity.brand" is "HomeBase Originals"

Scenario: Retrieve the product that was just created
When I GET a REST request to URL "/product/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Submit product for review (DRAFT -> UNDER_REVIEW)
Given that "comment" equals "Product listing complete, submitting for review"
And that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve the product (UNDER_REVIEW -> PUBLISHED)
Given that "comment" equals "Quality checks passed, approved for storefront"
And that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Discontinue the published product (PUBLISHED -> DISCONTINUED)
Given that "comment" equals "Product end-of-life, discontinuing"
And that "event" equals "discontinueProduct"
When I PATCH a REST request to URL "/product/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"

Scenario: Verify discontinued product is retrievable
When I GET a REST request to URL "/product/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"

# ═══════════════════════════════════════════════════════════════════════════════
# REJECTION FLOW: Create -> Submit -> Reject -> back to DRAFT
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a second product for rejection flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Low Quality Item",
    "description": "Product with issues",
    "brand": "Unknown Brand"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "rejectId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Submit second product for review
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${rejectId}/${event}" with payload
"""json
{
    "comment": "Submitting for review"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Reject the product (UNDER_REVIEW -> DRAFT)
Given that "event" equals "rejectProduct"
When I PATCH a REST request to URL "/product/${rejectId}/${event}" with payload
"""json
{
    "comment": "Images are blurry, description incomplete"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${rejectId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

# ═══════════════════════════════════════════════════════════════════════════════
# DISABLE/ENABLE FLOW: PUBLISHED -> DISABLED -> PUBLISHED
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a third product for disable/enable flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Seasonal Product",
    "description": "Available only in summer",
    "brand": "HomeBase Seasonal"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "disableId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Move third product through to PUBLISHED
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${disableId}/${event}" with payload
"""json
{
    "comment": "Ready for review"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve third product
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${disableId}/${event}" with payload
"""json
{
    "comment": "Approved"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Disable the published product (PUBLISHED -> DISABLED)
Given that "event" equals "disableProduct"
When I PATCH a REST request to URL "/product/${disableId}/${event}" with payload
"""json
{
    "comment": "Out of season, temporarily disabling"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${disableId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISABLED"

Scenario: Re-enable the disabled product (DISABLED -> PUBLISHED)
Given that "event" equals "enableProduct"
When I PATCH a REST request to URL "/product/${disableId}/${event}" with payload
"""json
{
    "comment": "Season started, re-enabling"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${disableId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Discontinue from DISABLED state (DISABLED -> DISCONTINUED)
Given that "event" equals "disableProduct"
When I PATCH a REST request to URL "/product/${disableId}/${event}" with payload
"""json
{
    "comment": "Disabling again"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "DISABLED"

Scenario: Discontinue disabled product
Given that "event" equals "discontinueProduct"
When I PATCH a REST request to URL "/product/${disableId}/${event}" with payload
"""json
{
    "comment": "Permanently discontinuing disabled product"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${disableId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"

# ═══════════════════════════════════════════════════════════════════════════════
# EDIT-AFTER-PUBLISH FLOW: PUBLISHED -> PENDING_UPDATE -> PUBLISHED
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a product for edit-after-publish flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Editable Product",
    "description": "Product that will be updated after publish",
    "brand": "HomeBase Editable"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "editId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Move edit product through to PUBLISHED
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${editId}/${event}" with payload
"""json
{
    "comment": "Ready for review"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve edit product
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${editId}/${event}" with payload
"""json
{
    "comment": "Approved"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Request update on published product (PUBLISHED -> PENDING_UPDATE)
Given that "event" equals "requestUpdate"
When I PATCH a REST request to URL "/product/${editId}/${event}" with payload
"""json
{
    "pendingChanges": "{\"name\": \"Updated Editable Product\", \"description\": \"Better description\"}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${editId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_UPDATE"

Scenario: Approve the update (PENDING_UPDATE -> PUBLISHED)
Given that "event" equals "approveUpdate"
When I PATCH a REST request to URL "/product/${editId}/${event}" with payload
"""json
{
    "comment": "Changes look good, approved"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${editId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Request another update for rejection test
Given that "event" equals "requestUpdate"
When I PATCH a REST request to URL "/product/${editId}/${event}" with payload
"""json
{
    "pendingChanges": "{\"name\": \"Bad Name\"}"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PENDING_UPDATE"

Scenario: Reject the update (PENDING_UPDATE -> PUBLISHED, no changes applied)
Given that "event" equals "rejectUpdate"
When I PATCH a REST request to URL "/product/${editId}/${event}" with payload
"""json
{
    "comment": "Name change not appropriate for this product"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${editId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

# ═══════════════════════════════════════════════════════════════════════════════
# ARCHIVE FLOW: PUBLISHED -> ARCHIVED -> PUBLISHED (unarchive)
#               ARCHIVED -> DISCONTINUED (permanent end-of-life)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a product for archive flow
When I POST a REST request to URL "/product" with payload
"""json
{
    "name": "Archivable Product",
    "description": "Product that will be archived",
    "brand": "HomeBase Archive"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "archiveId"
And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Move archive product through to PUBLISHED
Given that "event" equals "submitForReview"
When I PATCH a REST request to URL "/product/${archiveId}/${event}" with payload
"""json
{
    "comment": "Ready"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "UNDER_REVIEW"

Scenario: Approve archive product
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${archiveId}/${event}" with payload
"""json
{
    "comment": "Approved"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Archive the published product (PUBLISHED -> ARCHIVED)
Given that "event" equals "archiveProduct"
When I PATCH a REST request to URL "/product/${archiveId}/${event}" with payload
"""json
{
    "comment": "Seasonal product, archiving for off-season"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${archiveId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"

Scenario: Unarchive the product (ARCHIVED -> PUBLISHED)
Given that "event" equals "unarchiveProduct"
When I PATCH a REST request to URL "/product/${archiveId}/${event}" with payload
"""json
{
    "comment": "Season started, bringing product back"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${archiveId}"
And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Archive again then discontinue (ARCHIVED -> DISCONTINUED)
Given that "event" equals "archiveProduct"
When I PATCH a REST request to URL "/product/${archiveId}/${event}" with payload
"""json
{
    "comment": "Re-archiving"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"

Scenario: Discontinue from archived state
Given that "event" equals "discontinueProduct"
When I PATCH a REST request to URL "/product/${archiveId}/${event}" with payload
"""json
{
    "comment": "Product end-of-life, permanently discontinuing"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${archiveId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DISCONTINUED"

# ═══════════════════════════════════════════════════════════════════════════════
# INVALID EVENT: Sending wrong event on wrong state
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Send approveProduct on a DRAFT product — should fail
Given that "event" equals "approveProduct"
When I PATCH a REST request to URL "/product/${rejectId}/${event}" with payload
"""json
{
    "comment": "Trying to approve a DRAFT product directly"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to product — should fail
When I PATCH a REST request to URL "/product/${rejectId}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
