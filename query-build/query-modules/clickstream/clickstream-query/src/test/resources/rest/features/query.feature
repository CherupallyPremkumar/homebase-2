Feature: Clickstream Query -- admin views page views and click events

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: List all page views
  When I POST a REST request to URL "/q/pageViews" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "3"

Scenario: Filter page views by userId
  When I POST a REST request to URL "/q/pageViews" with payload
  """
  { "filters": { "userId": "user-1" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter page views by pageType
  When I POST a REST request to URL "/q/pageViews" with payload
  """
  { "filters": { "pageType": "PDP" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: Filter page views by deviceType
  When I POST a REST request to URL "/q/pageViews" with payload
  """
  { "filters": { "deviceType": "DESKTOP" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "1"

Scenario: Filter page views by sessionId
  When I POST a REST request to URL "/q/pageViews" with payload
  """
  { "filters": { "sessionId": "sess-abc-001" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: List all click events
  When I POST a REST request to URL "/q/clickEvents" with payload
  """
  { "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "4"

Scenario: Filter click events by userId
  When I POST a REST request to URL "/q/clickEvents" with payload
  """
  { "filters": { "userId": "user-2" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter click events by elementType
  When I POST a REST request to URL "/q/clickEvents" with payload
  """
  { "filters": { "elementType": "BUTTON" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"

Scenario: Filter click events by sessionId
  When I POST a REST request to URL "/q/clickEvents" with payload
  """
  { "filters": { "sessionId": "sess-def-002" }, "pageNum": 1, "numRowsInPage": 10 }
  """
  Then the http status code is 200
  And the REST response key "numRowsReturned" is "2"
