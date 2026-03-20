Feature: Tests the Supplier Query Service using a REST client.

Scenario: Tests out pagination capability
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
