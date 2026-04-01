Feature: Tests the CMS Query Service using a REST client.

Scenario: Tests CMS pages pagination
When I POST a REST request to URL "/q/pages" with payload
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

Scenario: Test CMS pages filter by pageType
When I POST a REST request to URL "/q/pages" with payload
"""
{
	"filters" :{
		"pageType": "STATIC"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "page-about"
And the REST response key "list[0].row.slug" is "about-us"
And the REST response key "list[0].row.title" is "About HomeBase"

Scenario: Test CMS pages filter by published status
When I POST a REST request to URL "/q/pages" with payload
"""
{
	"filters" :{
		"published": "true"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Tests banners pagination
When I POST a REST request to URL "/q/banners" with payload
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
And the REST response key "numRowsReturned" is "4"

Scenario: Test banners filter by position
When I POST a REST request to URL "/q/banners" with payload
"""
{
	"filters" :{
		"position": "HERO"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Test banners filter by active status
When I POST a REST request to URL "/q/banners" with payload
"""
{
	"filters" :{
		"active": "true"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
