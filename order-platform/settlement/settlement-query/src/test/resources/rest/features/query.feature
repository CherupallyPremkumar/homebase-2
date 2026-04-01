Feature: Tests the Settlement Query Service using a REST client. 

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 Tests out pagination capability
When I POST a REST request to URL "/q/settlements" with payload
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
And the REST response key "list[0].row.id" is "stl-1"
And the REST response key "list[1].row.id" is "stl-2"

Scenario: Test specific filter query
When I POST a REST request to URL "/q/settlements" with payload
"""
{
	"filters" :{
		"supplierId": "supp-1",
        "stateId": "PAID"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "stl-1"
And the REST response key "list[0].row.supplierId" is "supp-1"
And the REST response key "list[0].row.stateId" is "PAID"
