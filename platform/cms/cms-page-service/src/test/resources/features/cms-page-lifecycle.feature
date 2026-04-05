Feature: CMS Page Lifecycle — create, publish, unpublish, archive, restore

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new CMS page in DRAFT state
  When I POST a REST request to URL "/cms-page" with payload
  """json
  {
    "slug": "test-homepage",
    "title": "Test Homepage",
    "pageType": "LANDING",
    "layoutType": "block-based",
    "metaTitle": "Test Homepage | HomeBase",
    "metaDescription": "Welcome to the test homepage"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "pageId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Publish the page (DRAFT → PUBLISHED)
  Given that "event" equals "publish"
  When I PATCH a REST request to URL "/cms-page/${pageId}/${event}" with payload
  """json
  { "comment": "Publishing test homepage" }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Unpublish the page (PUBLISHED → DRAFT)
  Given that "event" equals "unpublish"
  When I PATCH a REST request to URL "/cms-page/${pageId}/${event}" with payload
  """json
  { "comment": "Taking page offline for edits" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Submit for review (DRAFT → IN_REVIEW)
  Given that "event" equals "submitForReview"
  When I PATCH a REST request to URL "/cms-page/${pageId}/${event}" with payload
  """json
  { "comment": "Ready for review" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "IN_REVIEW"

Scenario: Approve the page (IN_REVIEW → PUBLISHED)
  Given that "event" equals "approve"
  When I PATCH a REST request to URL "/cms-page/${pageId}/${event}" with payload
  """json
  { "comment": "Approved for publication" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "PUBLISHED"

Scenario: Archive the page (PUBLISHED → ARCHIVED)
  Given that "event" equals "archive"
  When I PATCH a REST request to URL "/cms-page/${pageId}/${event}" with payload
  """json
  { "comment": "Archiving old page" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ARCHIVED"

Scenario: Restore archived page (ARCHIVED → DRAFT)
  Given that "event" equals "restore"
  When I PATCH a REST request to URL "/cms-page/${pageId}/${event}" with payload
  """json
  { "comment": "Restoring for updates" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Retrieve the page
  When I GET a REST request to URL "/cms-page/${pageId}"
  Then the http status code is 200
  And the REST response key "mutatedEntity.slug" is "test-homepage"

Scenario: Reject page creation without title (cconfig validation)
  When I POST a REST request to URL "/cms-page" with payload
  """json
  {
    "slug": "no-title-page",
    "pageType": "STATIC"
  }
  """
  Then success is false
  And the top level code is 400

Scenario: Reject page creation without slug (cconfig validation)
  When I POST a REST request to URL "/cms-page" with payload
  """json
  {
    "title": "No Slug Page",
    "pageType": "STATIC"
  }
  """
  Then success is false
  And the top level code is 400
