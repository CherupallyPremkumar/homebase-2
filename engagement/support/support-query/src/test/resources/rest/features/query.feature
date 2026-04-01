Feature: Tests the Support Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 Search all support tickets with pagination
When I POST a REST request to URL "/q/supportTickets" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "4"

Scenario: Filter tickets by state
When I POST a REST request to URL "/q/supportTickets" with payload
"""
{
	"filters" :{
		"stateId": "OPEN"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.ID" is "ticket-2"

Scenario: Filter tickets by priority
When I POST a REST request to URL "/q/supportTickets" with payload
"""
{
	"filters" :{
		"priority": "CRITICAL"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.ID" is "ticket-3"

Scenario: Get ticket by id
When I POST a REST request to URL "/q/supportTicket" with payload
"""
{
	"filters" :{
		"id": "ticket-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "list[0].row.ID" is "ticket-1"
And the REST response key "list[0].row.CATEGORY" is "DELIVERY"
And the REST response key "list[0].row.STATEID" is "RESOLVED"
