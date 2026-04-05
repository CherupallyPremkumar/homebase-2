Feature: Banner Lifecycle — create, activate, deactivate, expire

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new banner in DRAFT state
  When I POST a REST request to URL "/cms-banner" with payload
  """json
  {
    "name": "Summer Sale Banner",
    "title": "Summer Sale - Up to 50% Off",
    "imageUrl": "https://cdn.example.com/banners/summer-sale.jpg",
    "position": "HOME_TOP",
    "displayOrder": 1
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "bannerId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Activate the banner (DRAFT -> ACTIVE)
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/cms-banner/${bannerId}/${event}" with payload
  """json
  { "comment": "Activating summer sale banner" }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Deactivate the banner (ACTIVE -> DRAFT)
  Given that "event" equals "deactivate"
  When I PATCH a REST request to URL "/cms-banner/${bannerId}/${event}" with payload
  """json
  { "comment": "Temporarily deactivating banner" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Schedule the banner (DRAFT -> SCHEDULED)
  Given that "event" equals "schedule"
  When I PATCH a REST request to URL "/cms-banner/${bannerId}/${event}" with payload
  """json
  { "comment": "Scheduling banner for next week" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "SCHEDULED"

Scenario: Activate from scheduled (SCHEDULED -> ACTIVE)
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/cms-banner/${bannerId}/${event}" with payload
  """json
  { "comment": "Activating scheduled banner" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Expire the banner (ACTIVE -> EXPIRED)
  Given that "event" equals "expire"
  When I PATCH a REST request to URL "/cms-banner/${bannerId}/${event}" with payload
  """json
  { "comment": "Banner has expired" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "EXPIRED"

Scenario: Reactivate expired banner (EXPIRED -> ACTIVE)
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/cms-banner/${bannerId}/${event}" with payload
  """json
  { "comment": "Reactivating expired banner" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Retrieve the banner
  When I GET a REST request to URL "/cms-banner/${bannerId}"
  Then the http status code is 200
  And the REST response key "mutatedEntity.name" is "Summer Sale Banner"
