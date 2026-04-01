Feature: Tests the Supplier Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 Tests out pagination capability
When I POST a REST request to URL "/q/suppliers" with payload
"""
{
	"sortCriteria" :[
		{"name":"rating","ascendingOrder": false}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "6"

Scenario: Test specific filter query by state
When I POST a REST request to URL "/q/suppliers" with payload
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
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.stateId" is "ACTIVE"
And the REST response key "list[1].row.stateId" is "ACTIVE"

Scenario: Test filter by business type
When I POST a REST request to URL "/q/suppliers" with payload
"""
{
	"filters" :{
        "businessType": "SOLE_PROPRIETORSHIP"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
