Feature: Tests the Shipping Query Service using a REST client. 

Scenario: Tests out pagination capability
When I POST a REST request to URL "/q/shippings" with payload
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
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "shp-1"

Scenario: Test specific filter query
When I POST a REST request to URL "/q/shippings" with payload
"""
{
	"filters" :{
		"orderId": "ord-1",
        "stateId": "SHIPPED"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "shp-1"
And the REST response key "list[0].row.orderId" is "ord-1"
And the REST response key "list[0].row.stateId" is "SHIPPED"
