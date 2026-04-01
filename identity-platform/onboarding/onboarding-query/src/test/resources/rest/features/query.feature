Feature: Tests the Onboarding Query Service using a REST client.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

 List all onboardings with pagination
When I POST a REST request to URL "/q/onboardings" with payload
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
And the REST response key "numRowsReturned" is "6"

Scenario: Filter onboardings by state COMPLETED
When I POST a REST request to URL "/q/onboardings" with payload
"""
{
	"filters" :{
        "stateId": "COMPLETED"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.stateId" is "COMPLETED"

Scenario: Filter onboardings by supplier
When I POST a REST request to URL "/q/onboarding-by-supplier" with payload
"""
{
	"filters" :{
        "supplierId": "supplier-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "onb-1"
And the REST response key "list[0].row.supplierId" is "supplier-1"

Scenario: Get pending review onboardings
When I POST a REST request to URL "/q/onboardings-pending" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.stateId" is "DOCUMENTS_SUBMITTED"

Scenario: Filter onboardings by business type
When I POST a REST request to URL "/q/onboardings" with payload
"""
{
	"filters" :{
        "businessType": "SOLE_PROPRIETORSHIP"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
