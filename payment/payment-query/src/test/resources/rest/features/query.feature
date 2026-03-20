Feature: Tests the Payment Query Service using a REST client.

Scenario: List all payments with pagination
When I POST a REST request to URL "/q/payments" with payload
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
And the REST response key "numRowsReturned" is "8"
And the REST response key "list[0].row.id" is "pay-1"

Scenario: Filter payments by payment method UPI
When I POST a REST request to URL "/q/payments" with payload
"""
{
	"filters" :{
        "paymentMethod": "UPI"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "4"

Scenario: Filter payments by state SUCCEEDED
When I POST a REST request to URL "/q/payments" with payload
"""
{
	"filters" :{
        "stateId": "SUCCEEDED"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.stateId" is "SUCCEEDED"

Scenario: Filter payments by customer
When I POST a REST request to URL "/q/payments" with payload
"""
{
	"filters" :{
        "customerId": "user-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Get payment by order ID
When I POST a REST request to URL "/q/payment-by-order" with payload
"""
{
	"filters" :{
        "orderId": "ord-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "pay-1"
And the REST response key "list[0].row.orderId" is "ord-1"
