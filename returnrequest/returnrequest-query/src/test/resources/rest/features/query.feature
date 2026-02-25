Feature: Tests the Returnrequest Query Service using a REST client. 

Scenario: Tests out pagination capability
When I POST a REST request to URL "/q/returnrequests" with payload
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
And the REST response key "list[0].row.id" is "ref-1"

Scenario: Test specific filter query
When I POST a REST request to URL "/q/returnrequests" with payload
"""
{
	"filters" :{
		"orderId": "ord-1",
        "stateId": "PENDING"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "ref-1"
And the REST response key "list[0].row.orderId" is "ord-1"
And the REST response key "list[0].row.stateId" is "PENDING"
