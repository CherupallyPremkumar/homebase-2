Feature: Catalog - Category and Collection CRUD via REST controllers.

# Category CRUD

Scenario: Create a root category
When I POST a REST request to URL "/category" with payload
"""
{
    "name": "Handmade Textiles",
    "description": "Artisan fabrics and woven goods",
    "slug": "handmade-textiles",
    "active": true
}
"""
Then the http status code is 200
And the REST response contains key "id"
And the REST response key "name" is "Handmade Textiles"
And store "$.payload.id" from response to "catId"

Scenario: Retrieve the created category by ID
When I GET a REST request to URL "/category/${catId}"
Then the http status code is 200
And the REST response key "name" is "Handmade Textiles"

Scenario: Create a child category under root
When I POST a REST request to URL "/category" with payload
"""
{
    "name": "Silk Scarves",
    "description": "Hand-woven silk scarves",
    "slug": "silk-scarves",
    "parentId": "${catId}",
    "active": true
}
"""
Then the http status code is 200
And the REST response key "name" is "Silk Scarves"
And store "$.payload.id" from response to "childCatId"

Scenario: Create another child category
When I POST a REST request to URL "/category" with payload
"""
{
    "name": "Cotton Fabrics",
    "description": "Handloom cotton fabrics",
    "slug": "cotton-fabrics",
    "parentId": "${catId}",
    "active": true
}
"""
Then the http status code is 200
And the REST response key "name" is "Cotton Fabrics"
And store "$.payload.id" from response to "childCatId2"

Scenario: Get root categories
When I GET a REST request to URL "/category/_roots"
Then the http status code is 200

Scenario: Get full category tree
When I GET a REST request to URL "/category/_tree"
Then the http status code is 200

Scenario: Get children of root category
When I GET a REST request to URL "/category/${catId}/_children"
Then the http status code is 200

Scenario: Update a category
When I PUT a REST request to URL "/category/${catId}" with payload
"""
{
    "name": "Handmade Textiles and Fabrics",
    "description": "Updated: Artisan fabrics, woven goods, and textiles",
    "slug": "handmade-textiles-fabrics",
    "active": true
}
"""
Then the http status code is 200
And the REST response key "name" is "Handmade Textiles and Fabrics"

Scenario: Delete a child category (soft delete)
When I DELETE a REST request to URL "/category/${childCatId2}" with payload
"""
{}
"""
Then the http status code is 200

# Collection CRUD

Scenario: Create a manual collection
When I POST a REST request to URL "/collection" with payload
"""
{
    "name": "Summer Essentials",
    "description": "Curated collection of summer products",
    "slug": "summer-essentials",
    "type": "CURATED",
    "active": true,
    "imageUrl": "https://cdn.homebase.com/collections/summer.jpg"
}
"""
Then the http status code is 200
And the REST response key "name" is "Summer Essentials"
And store "$.payload.id" from response to "collId"

Scenario: Create a dynamic collection
When I POST a REST request to URL "/collection" with payload
"""
{
    "name": "New Arrivals",
    "description": "Automatically populated with latest products",
    "slug": "new-arrivals",
    "type": "DYNAMIC",
    "active": true,
    "autoUpdate": true
}
"""
Then the http status code is 200
And the REST response key "name" is "New Arrivals"
And store "$.payload.id" from response to "dynCollId"

Scenario: Retrieve collection by ID
When I GET a REST request to URL "/collection/${collId}"
Then the http status code is 200
And the REST response key "name" is "Summer Essentials"

Scenario: List all active collections
When I GET a REST request to URL "/collection"
Then the http status code is 200

Scenario: List dynamic collections
When I GET a REST request to URL "/collection/_dynamic"
Then the http status code is 200

Scenario: Update a collection
When I PUT a REST request to URL "/collection/${collId}" with payload
"""
{
    "name": "Summer Essentials 2026",
    "description": "Updated summer collection for 2026",
    "slug": "summer-essentials-2026",
    "type": "CURATED",
    "active": true,
    "imageUrl": "https://cdn.homebase.com/collections/summer-2026.jpg"
}
"""
Then the http status code is 200
And the REST response key "name" is "Summer Essentials 2026"

Scenario: Delete a collection (soft delete)
When I DELETE a REST request to URL "/collection/${dynCollId}" with payload
"""
{}
"""
Then the http status code is 200
