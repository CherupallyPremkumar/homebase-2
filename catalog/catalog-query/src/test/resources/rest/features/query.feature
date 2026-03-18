Feature: Tests the Catalog Query Service using a REST client.

Scenario: Browse active catalog items with pagination
When I POST a REST request to URL "/catalog/browseCatalog" with payload
"""
{
	"sortCriteria": [
		{"name": "name", "ascendingOrder": true}
	],
	"pageNum": 0,
	"numRowsInPage": 3
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Browse catalog with name filter (like query)
When I POST a REST request to URL "/catalog/browseCatalog" with payload
"""
{
	"filters": {
		"name": "Silk"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.name" is "Banarasi Silk Saree"

Scenario: Get featured user picks
When I POST a REST request to URL "/catalog/userPicks" with payload
"""
{
	"filters": {
		"featured": true,
		"active": true
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Get category banners
When I POST a REST request to URL "/catalog/categoryBanners" with payload
"""
{
	"filters": {
		"active": true
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Get active collections
When I POST a REST request to URL "/catalog/userCollections" with payload
"""
{
	"filters": {
		"active": true
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Get collections by type
When I POST a REST request to URL "/catalog/userCollections" with payload
"""
{
	"filters": {
		"type": "DYNAMIC"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.name" is "New Arrivals"

Scenario: Get suggestions by tags
When I POST a REST request to URL "/catalog/itemSuggestions" with payload
"""
{
	"filters": {
		"tags": ["handmade", "decor"],
		"active": true
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Get navigation menu categories
When I POST a REST request to URL "/catalog/categoryMenu" with payload
"""
{
	"filters": {
		"level": 0
	},
	"sortCriteria": [
		{"name": "displayOrder", "ascendingOrder": true}
	]
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.name" is "Handmade Textiles"

Scenario: Get items by category
When I POST a REST request to URL "/catalog/catalogByCategory" with payload
"""
{
	"filters": {
		"categoryId": "cat-2",
		"active": true
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

# ═══════════════════════════════════════════════════════════════════════════════
# FULL-TEXT SEARCH (PostgreSQL tsvector)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Full-text search — "silk saree" matches Banarasi Silk Saree
When I POST a REST request to URL "/catalog/searchCatalog" with payload
"""
{
	"filters": {
		"searchTerm": "silk saree"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.name" is "Banarasi Silk Saree"

Scenario: Full-text search — "cotton" matches organic cotton shirt and block print cushion
When I POST a REST request to URL "/catalog/searchCatalog" with payload
"""
{
	"filters": {
		"searchTerm": "cotton"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Full-text search with price filter
When I POST a REST request to URL "/catalog/searchCatalog" with payload
"""
{
	"filters": {
		"searchTerm": "handmade",
		"maxPrice": 1000
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.name" is "Block Print Cushion Cover"

Scenario: Full-text search with in_stock filter
When I POST a REST request to URL "/catalog/searchCatalog" with payload
"""
{
	"filters": {
		"searchTerm": "brass lamp",
		"inStock": true
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "0"

Scenario: Fuzzy search — typo "Banarasi Slik" finds "Banarasi Silk"
When I POST a REST request to URL "/catalog/fuzzyCatalog" with payload
"""
{
	"filters": {
		"searchTerm": "Banarasi Slik Saree"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.name" is "Banarasi Silk Saree"
