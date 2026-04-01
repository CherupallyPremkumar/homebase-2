Feature: Tests the User Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 Get all users with pagination
When I POST a REST request to URL "/q/users" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"

Scenario: Filter users by role
When I POST a REST request to URL "/q/users" with payload
"""
{
	"filters" :{
		"role": "CUSTOMER"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "7"

Scenario: Filter users by state
When I POST a REST request to URL "/q/users" with payload
"""
{
	"filters" :{
		"stateId": "ACTIVE"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "4"

Scenario: Filter users by email
When I POST a REST request to URL "/q/users" with payload
"""
{
	"filters" :{
		"email": "priya.sharma@gmail.com"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.firstName" is "Priya"
