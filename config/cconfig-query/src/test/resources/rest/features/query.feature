Feature: Tests the CConfig Query Service for admin dashboard.

Scenario: List all config entries with pagination
When I POST a REST request to URL "/q/cconfigEntries" with payload
"""
{
	"sortCriteria": [
		{"name": "moduleName", "ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true

Scenario: Filter config entries by module name
When I POST a REST request to URL "/q/cconfigEntries" with payload
"""
{
	"filters": {
		"moduleName": "cart"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"

Scenario: Filter config entries by module and key name pattern
When I POST a REST request to URL "/q/cconfigByModule" with payload
"""
{
	"filters": {
		"moduleName": "payment",
		"keyName": "%gateway%"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Filter config entries by tenant
When I POST a REST request to URL "/q/cconfigEntries" with payload
"""
{
	"filters": {
		"tenant": "homebase"
	},
	"pageNum": 1,
	"numRowsInPage": 5
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "5"

Scenario: Get all platform feature flags
When I POST a REST request to URL "/q/cconfigByModule" with payload
"""
{
	"filters": {
		"moduleName": "platform"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"

Scenario: List all form validation schemas
When I POST a REST request to URL "/q/cconfigFormSchemas" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 20
}
"""
Then the http status code is 200
And the top level code is 200
And success is true

Scenario: Get form schemas for a specific module
When I POST a REST request to URL "/q/cconfigFormSchemas" with payload
"""
{
	"filters": {
		"moduleName": "user"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
