Feature: Tests the Notification Query Service using a REST client.

Scenario: Get all notifications with pagination
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
