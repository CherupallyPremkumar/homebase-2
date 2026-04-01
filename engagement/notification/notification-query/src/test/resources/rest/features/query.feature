Feature: Tests the Notification Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 Get all notifications with pagination
When I POST a REST request to URL "/q/notifications" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "5"

Scenario: Filter notifications by channel
When I POST a REST request to URL "/q/notifications" with payload
"""
{
	"filters" :{
		"channel": "EMAIL"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "4"

Scenario: Filter notifications by state
When I POST a REST request to URL "/q/notifications" with payload
"""
{
	"filters" :{
		"stateId": "DELIVERED"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Filter notifications by customer
When I POST a REST request to URL "/q/notifications" with payload
"""
{
	"filters" :{
		"customerId": "user-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
