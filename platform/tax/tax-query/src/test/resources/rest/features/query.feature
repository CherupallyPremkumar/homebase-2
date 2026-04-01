Feature: Tests the Tax Query Service using a REST client.

Scenario: List all tax rates with pagination
When I POST a REST request to URL "/q/taxRates" with payload
"""
{
	"sortCriteria" :[
		{"name":"rate","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"

Scenario: Filter tax rates by region code
When I POST a REST request to URL "/q/taxRates" with payload
"""
{
	"filters" :{
        "regionCode": "IN"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"

Scenario: Filter tax rates by tax type CGST
When I POST a REST request to URL "/q/taxRates" with payload
"""
{
	"filters" :{
        "taxType": "CGST"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.taxType" is "CGST"

Scenario: Filter tax rates by tax type IGST
When I POST a REST request to URL "/q/taxRates" with payload
"""
{
	"filters" :{
        "taxType": "IGST"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "tax-igst-18"
