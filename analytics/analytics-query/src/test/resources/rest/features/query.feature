Feature: Tests the Analytics Query Service using a REST client.

Scenario: Tests daily sales pagination
When I POST a REST request to URL "/q/dailySales" with payload
"""
{
	"sortCriteria" :[
		{"name":"summaryDate","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "7"
And the REST response key "list[0].row.totalOrders" is "185"
And the REST response key "list[0].row.currency" is "INR"

Scenario: Test daily sales filter by currency
When I POST a REST request to URL "/q/dailySales" with payload
"""
{
	"filters" :{
		"currency": "INR"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "7"

Scenario: Test product performance query
When I POST a REST request to URL "/q/productPerformance" with payload
"""
{
	"sortCriteria" :[
		{"name":"views","ascendingOrder": false}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "5"

Scenario: Test product performance filter by productId
When I POST a REST request to URL "/q/productPerformance" with payload
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
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.productId" is "prod-1"
And the REST response key "list[0].row.views" is "1245"
