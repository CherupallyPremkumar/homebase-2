Feature: Tests the Review Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 Search all reviews with pagination
When I POST a REST request to URL "/q/reviews" with payload
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

Scenario: Filter reviews by state
When I POST a REST request to URL "/q/reviews" with payload
"""
{
	"filters" :{
		"stateId": "PUBLISHED"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Filter reviews by product
When I POST a REST request to URL "/q/reviews" with payload
"""
{
	"filters" :{
		"productId": "prod-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Get review by id
When I POST a REST request to URL "/q/review" with payload
"""
{
	"filters" :{
		"id": "rev-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "list[0].row.ID" is "rev-1"
And the REST response key "list[0].row.RATING" is "5"
