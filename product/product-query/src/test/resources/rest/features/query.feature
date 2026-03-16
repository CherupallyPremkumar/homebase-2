Feature: Tests the Product Query Service — full bounded context coverage.
  Test data: 8 products across 5 states, 15 variants, 7 attribute definitions,
  6 categories, 6 media assets, 6 activity log entries.
    prod-1: Wireless Headphones (PUBLISHED, SoundMax, cat-audio) — 2 variants, 3 attributes, 2 images
    prod-2: Cotton T-Shirt (PUBLISHED, BasicWear, cat-apparel) — 4 variants, 1 attribute, 1 image
    prod-3: Stainless Steel Blender (UNDER_REVIEW, HomeChef, cat-home) — 1 variant, 3 attributes
    prod-4: Smartphone X200 (DRAFT, TechBrand, cat-phones) — 2 variants
    prod-5: Bluetooth Speaker (PUBLISHED, SoundMax, cat-audio) — 2 variants, 3 attributes, 2 images
    prod-6: Yoga Mat (UNDER_REVIEW, FitLife, cat-home) — 1 variant
    prod-7: Desk Lamp (DISABLED, HomeChef, cat-home) — 1 variant
    prod-8: Running Shoes (DISCONTINUED, FitLife, cat-apparel) — 2 variants

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

# ═══════════════════════════════════════════════════════════════════════
# ADMIN DASHBOARD QUERIES
# ═══════════════════════════════════════════════════════════════════════

Scenario: Paginated listing of all products sorted by id
When I POST a REST request to URL "/q/products" with payload
"""
{
	"sortCriteria": [
		{"name": "id", "ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"
And the REST response key "list[0].row.id" is "prod-1"
And the REST response key "list[0].row.name" is "Wireless Headphones"
And the REST response key "list[0].row.brand" is "SoundMax"

Scenario: Filter products by brand
When I POST a REST request to URL "/q/products" with payload
"""
{
	"filters": {
		"brand": "SoundMax"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.brand" is "SoundMax"

Scenario: Filter products by categoryId
When I POST a REST request to URL "/q/products" with payload
"""
{
	"filters": {
		"categoryId": "cat-apparel"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Filter products by stateId
When I POST a REST request to URL "/q/products" with payload
"""
{
	"filters": {
		"stateId": "PUBLISHED"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.stateId" is "PUBLISHED"

Scenario: Filter products by name using like query
When I POST a REST request to URL "/q/products" with payload
"""
{
	"filters": {
		"name": "%Cotton%"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "prod-2"

Scenario: Get product by id
When I POST a REST request to URL "/q/product" with payload
"""
{
	"filters": {
		"id": "prod-3"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "list[0].row.id" is "prod-3"
And the REST response key "list[0].row.name" is "Stainless Steel Blender"
And the REST response key "list[0].row.brand" is "HomeChef"
And the REST response key "list[0].row.stateId" is "UNDER_REVIEW"

Scenario: Dashboard - product state counts
When I POST a REST request to URL "/q/product-state-counts" with payload
"""
{}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "5"

Scenario: Dashboard - products pending review
When I POST a REST request to URL "/q/products-pending-review" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.stateId" is "UNDER_REVIEW"

# ═══════════════════════════════════════════════════════════════════════
# STOREFRONT QUERIES
# ═══════════════════════════════════════════════════════════════════════

Scenario: Storefront - only published products returned
When I POST a REST request to URL "/q/storefront-products" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.stateId" is "PUBLISHED"

Scenario: Storefront - filter published products by brand
When I POST a REST request to URL "/q/storefront-products" with payload
"""
{
	"filters": {
		"brand": "SoundMax"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Storefront - get product by slug
When I POST a REST request to URL "/q/product-by-slug" with payload
"""
{
	"filters": {
		"slug": "wireless-headphones"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "list[0].row.id" is "prod-1"
And the REST response key "list[0].row.slug" is "wireless-headphones"
And the REST response key "list[0].row.metaTitle" is "Best Wireless Headphones"

Scenario: Storefront - search products by text
When I POST a REST request to URL "/q/search-products" with payload
"""
{
	"filters": {
		"q": "%headphone%"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "prod-1"

Scenario: Storefront - get products by category including subcategories
When I POST a REST request to URL "/q/products-by-category" with payload
"""
{
	"filters": {
		"categoryId": "cat-electronics"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Category tree for navigation
When I POST a REST request to URL "/q/category-tree" with payload
"""
{}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "5"
And the REST response key "list[0].row.depth" is "0"

# ═══════════════════════════════════════════════════════════════════════
# VARIANT QUERIES
# ═══════════════════════════════════════════════════════════════════════

Scenario: Get variants for prod-1 (2 variants with color attributes)
When I POST a REST request to URL "/q/product-variants" with payload
"""
{
	"filters": {
		"productId": "prod-1"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.id" is "var-1a"
And the REST response key "list[0].row.sku" is "WH-BLK-001"
And the REST response key "list[0].row.productName" is "Wireless Headphones"
And the REST response key "list[0].row.attributeKey" is "color"
And the REST response key "list[0].row.attributeValue" is "black"

Scenario: Get variants for prod-2 (4 variants with color+size)
When I POST a REST request to URL "/q/product-variants" with payload
"""
{
	"filters": {
		"productId": "prod-2"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "8"

Scenario: Lookup variant by SKU
When I POST a REST request to URL "/q/variant-by-sku" with payload
"""
{
	"filters": {
		"sku": "WH-BLK-001"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "list[0].row.id" is "var-1a"
And the REST response key "list[0].row.productId" is "prod-1"
And the REST response key "list[0].row.productName" is "Wireless Headphones"

Scenario: Variant exists - PUBLISHED product (cart integration)
When I POST a REST request to URL "/q/variant-exists" with payload
"""
{
	"filters": {
		"productId": "prod-1",
		"variantId": "var-1a"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.variantId" is "var-1a"
And the REST response key "list[0].row.sku" is "WH-BLK-001"

Scenario: Variant exists - DRAFT product returns nothing (not sellable)
When I POST a REST request to URL "/q/variant-exists" with payload
"""
{
	"filters": {
		"productId": "prod-4",
		"variantId": "var-4a"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "0"

Scenario: Variant exists - wrong productId returns nothing
When I POST a REST request to URL "/q/variant-exists" with payload
"""
{
	"filters": {
		"productId": "prod-2",
		"variantId": "var-1a"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════
# MEDIA QUERIES
# ═══════════════════════════════════════════════════════════════════════

Scenario: Get media for prod-1 (2 images, primary first)
When I POST a REST request to URL "/q/product-media" with payload
"""
{
	"filters": {
		"productId": "prod-1"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.assetId" is "img-1a"
And the REST response key "list[0].row.cdnUrl" is "https://cdn.homebase.com/headphones-front.jpg"
And the REST response key "list[0].row.primary" is "true"

Scenario: Get media for product with no images
When I POST a REST request to URL "/q/product-media" with payload
"""
{
	"filters": {
		"productId": "prod-4"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════
# ATTRIBUTE QUERIES
# ═══════════════════════════════════════════════════════════════════════

Scenario: Get attributes for prod-1 (connectivity=BT5.3, battery=30, weight=250)
When I POST a REST request to URL "/q/product-attributes" with payload
"""
{
	"filters": {
		"productId": "prod-1"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.attributeCode" is "weight"
And the REST response key "list[0].row.rawValue" is "250"

Scenario: Get attributes for prod-2 (material=100% Cotton)
When I POST a REST request to URL "/q/product-attributes" with payload
"""
{
	"filters": {
		"productId": "prod-2"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.attributeCode" is "material"
And the REST response key "list[0].row.rawValue" is "100% Cotton"

Scenario: Get attribute definitions for audio category
When I POST a REST request to URL "/q/category-attributes" with payload
"""
{
	"filters": {
		"categoryId": "cat-audio"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "4"
And the REST response key "list[0].row.code" is "color"

Scenario: Get attribute definitions for apparel category
When I POST a REST request to URL "/q/category-attributes" with payload
"""
{
	"filters": {
		"categoryId": "cat-apparel"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

# ═══════════════════════════════════════════════════════════════════════
# TAGS QUERY
# ═══════════════════════════════════════════════════════════════════════

Scenario: Get tags for prod-1 (wireless, noise-cancelling, premium)
When I POST a REST request to URL "/q/product-tags" with payload
"""
{
	"filters": {
		"productId": "prod-1"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Get tags for product with no tags
When I POST a REST request to URL "/q/product-tags" with payload
"""
{
	"filters": {
		"productId": "prod-7"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "0"

# ═══════════════════════════════════════════════════════════════════════
# ACTIVITY LOG QUERY
# ═══════════════════════════════════════════════════════════════════════

Scenario: Get activity log for prod-1 (3 activities: CREATE, SUBMIT, APPROVE)
When I POST a REST request to URL "/q/product-activity-log" with payload
"""
{
	"filters": {
		"productId": "prod-1"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.activityName" is "CREATE"
And the REST response key "list[0].row.activityComment" is "Product created"
And the REST response key "list[2].row.activityName" is "APPROVE"

Scenario: Get activity log for prod-7 (1 activity: DISABLE)
When I POST a REST request to URL "/q/product-activity-log" with payload
"""
{
	"filters": {
		"productId": "prod-7"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.activityName" is "DISABLE"

