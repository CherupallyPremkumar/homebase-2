Feature: Tests the Product Query Service using a REST client. 

Scenario: Tests out pagination capability
When I POST a REST request to URL "/q/products" with payload
"""
{
	"sortCriteria" :[
		{"name":"id","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.id" is "prod-1"
And the REST response key "list[1].row.id" is "prod-2"
And the REST response key "list[2].row.id" is "prod-3"

Scenario: Test specific filter query
When I POST a REST request to URL "/q/products" with payload
"""
{
	"filters" :{
		"category": "Apparel"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "prod-1"
And the REST response key "list[0].row.category" is "Apparel"
