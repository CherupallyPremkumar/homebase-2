Feature: Tests the Cart Query Service using a REST client. 

Scenario: Tests out pagination capability
When I POST a REST request to URL "/q/carts" with payload
"""
{
	"sortCriteria" :[
		{"name":"createdAt","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.id" is "cart-1"
And the REST response key "list[1].row.id" is "cart-2"

Scenario: Test specific filter query
When I POST a REST request to URL "/q/carts" with payload
"""
{
	"filters" :{
		"customerId": "user-1",
        "stateId": "OPEN"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "cart-1"
And the REST response key "list[0].row.customerId" is "user-1"
And the REST response key "list[0].row.stateId" is "OPEN"
