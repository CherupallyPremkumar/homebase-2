Feature: Announcement Lifecycle — create, schedule, activate, expire

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new announcement in DRAFT state
  When I POST a REST request to URL "/cms-announcement" with payload
  """json
  {
    "title": "New Feature Launch",
    "message": "We are excited to announce our new feature!",
    "announcementType": "FEATURE",
    "targetAudience": "ALL",
    "priority": "HIGH",
    "dismissible": true
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "announcementId"
  And the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Schedule the announcement (DRAFT -> SCHEDULED)
  Given that "event" equals "schedule"
  When I PATCH a REST request to URL "/cms-announcement/${announcementId}/${event}" with payload
  """json
  { "comment": "Scheduling for next week" }
  """
  Then the REST response contains key "mutatedEntity"
  And the REST response key "mutatedEntity.currentState.stateId" is "SCHEDULED"

Scenario: Activate the announcement (SCHEDULED -> ACTIVE)
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/cms-announcement/${announcementId}/${event}" with payload
  """json
  { "comment": "Activating announcement" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Expire the announcement (ACTIVE -> EXPIRED)
  Given that "event" equals "expire"
  When I PATCH a REST request to URL "/cms-announcement/${announcementId}/${event}" with payload
  """json
  { "comment": "Announcement has expired" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "EXPIRED"

Scenario: Reactivate expired announcement (EXPIRED -> ACTIVE)
  Given that "event" equals "activate"
  When I PATCH a REST request to URL "/cms-announcement/${announcementId}/${event}" with payload
  """json
  { "comment": "Reactivating expired announcement" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Deactivate the announcement (ACTIVE -> DRAFT)
  Given that "event" equals "deactivate"
  When I PATCH a REST request to URL "/cms-announcement/${announcementId}/${event}" with payload
  """json
  { "comment": "Deactivating announcement" }
  """
  Then the REST response key "mutatedEntity.currentState.stateId" is "DRAFT"

Scenario: Retrieve the announcement
  When I GET a REST request to URL "/cms-announcement/${announcementId}"
  Then the http status code is 200
  And the REST response key "mutatedEntity.title" is "New Feature Launch"
