Feature: Tests the Promo Query Service using a REST client.

Scenario: Search all promos with pagination
When I POST a REST request to URL "/q/promos" with payload
"""
{
	"sortCriteria" :[
		{"name":"code","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "6"

Scenario: Filter promos by state
When I POST a REST request to URL "/q/promos" with payload
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

Scenario: Filter promos by discount type
When I POST a REST request to URL "/q/promos" with payload
"""
{
	"filters" :{
		"discountType": "FLAT"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "4"

Scenario: Get promo by code
When I POST a REST request to URL "/q/promo" with payload
"""
{
	"filters" :{
		"code": "FLAT200"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "list[0].row.code" is "FLAT200"
And the REST response key "list[0].row.discountType" is "FLAT"
