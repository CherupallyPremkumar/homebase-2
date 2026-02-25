Feature: Tests the Supplierproduct Query Service using a REST client. 

Scenario: Tests out pagination capability
When I POST a REST request to URL "/q/supplierproducts" with payload
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
And the REST response key "list[0].row.id" is "sp-1"
And the REST response key "list[1].row.id" is "sp-2"

Scenario: Test specific filter query
When I POST a REST request to URL "/q/supplierproducts" with payload
"""
{
	"filters" :{
		"supplierId": "supp-1",
		"productId": "prod-1",
        "stateId": "ACTIVE"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "sp-1"
And the REST response key "list[0].row.productId" is "prod-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"
