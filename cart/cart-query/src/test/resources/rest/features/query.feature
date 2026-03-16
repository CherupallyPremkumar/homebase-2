Feature: Tests the Cart Query Service - full bounded context coverage.
  All money values are in PAISE (smallest currency unit).
  Test data: 8 carts across all STM states, 3 cart items, 5 activity log entries
    cart-1: user-1, ACTIVE              (2026-01-10) - 2 items, subtotal=149900 paise (Rs.1499)
    cart-2: user-2, ACTIVE              (2026-01-12) - 1 item,  subtotal=239700 paise (Rs.2397)
    cart-3: user-3, CHECKOUT_INITIATED  (2026-01-08)
    cart-4: user-1, CHECKOUT_COMPLETED  (2025-12-01)
    cart-5: user-1, ABANDONED           (2025-12-20)
    cart-6: user-3, ABANDONED           (2025-12-22)
    cart-7: user-2, EXPIRED             (2025-12-10)
    cart-8: user-2, MERGED              (2025-11-15)

  Products: prod-1 (Widget A, WidgetCo, Electronics), prod-2 (Gadget B, GadgetPro, Gadgets), prod-3 (Gizmo C, GizmoCorp, Electronics)
  Variants: var-1a (Red/M), var-2a (Blue/L), var-3a (Black/S)
  Inventory: inv-1 (95 avail), inv-2 (48 avail), inv-3 (197 avail)
  Activities: cart-1 has 3 (create, addItem x2), cart-2 has 2 (create, addItem)

# ===== DASHBOARD: CART QUERIES =====

Scenario: Dashboard - list all carts sorted by createdTime ASC with all fields
When I POST a REST request to URL "/q/carts" with payload
"""
{
	"sortCriteria" :[
		{"name":"created_time","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "8"
And the REST response key "list[0].row.id" is "cart-8"
And the REST response key "list[0].row.stateId" is "MERGED"
And the REST response key "list[7].row.id" is "cart-2"
And the REST response key "list[7].row.stateId" is "ACTIVE"

Scenario: Dashboard - verify all cart fields returned
When I POST a REST request to URL "/q/carts" with payload
"""
{
	"filters" :{
		"userId": "user-1",
        "stateId": "ACTIVE"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "cart-1"
And the REST response key "list[0].row.userId" is "user-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"
And the REST response key "list[0].row.flowId" is "cart-flow"
And the REST response key "list[0].row.subtotal" is "149900"
And the REST response key "list[0].row.currency" is "INR"
And the REST response key "list[0].row.tenant" is "homebase"

Scenario: Dashboard - filter carts by ACTIVE state
When I POST a REST request to URL "/q/carts" with payload
"""
{
	"filters" :{
        "stateId": "ACTIVE"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Dashboard - filter carts by CHECKOUT_INITIATED
When I POST a REST request to URL "/q/carts" with payload
"""
{
	"filters" :{
        "stateId": "CHECKOUT_INITIATED"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "cart-3"

Scenario: Dashboard - get cart state counts
When I POST a REST request to URL "/q/cartStateCounts" with payload
"""
{}
"""
Then the http status code is 200
And the top level code is 200
And success is true

Scenario: Dashboard - list abandoned carts
When I POST a REST request to URL "/q/abandonedCarts" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Dashboard - filter abandoned carts by userId
When I POST a REST request to URL "/q/abandonedCarts" with payload
"""
{
	"filters" :{
		"userId": "user-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "cart-5"

Scenario: Dashboard - list expired carts
When I POST a REST request to URL "/q/expiredCarts" with payload
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
And the REST response key "list[0].row.id" is "cart-7"

# ===== STOREFRONT: CART QUERIES =====

Scenario: Storefront - get all carts for user-1
When I POST a REST request to URL "/q/cartsByCustomer" with payload
"""
{
	"filters" :{
		"userId": "user-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

Scenario: Storefront - get ACTIVE carts for user-1
When I POST a REST request to URL "/q/cartsByCustomer" with payload
"""
{
	"filters" :{
		"userId": "user-1",
		"stateId": "ACTIVE"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "cart-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"

Scenario: Storefront - get active cart for user-1
When I POST a REST request to URL "/q/activeCart" with payload
"""
{
	"filters" :{
		"userId": "user-1"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "list[0].row.id" is "cart-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"
And the REST response key "list[0].row.subtotal" is "149900"
And the REST response key "list[0].row.currency" is "INR"

Scenario: Storefront - get active cart for user-2
When I POST a REST request to URL "/q/activeCart" with payload
"""
{
	"filters" :{
		"userId": "user-2"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "list[0].row.id" is "cart-2"
And the REST response key "list[0].row.stateId" is "ACTIVE"

# ===== CART ITEMS QUERIES =====

Scenario: Storefront - get items for cart-1 (2 items) with variantId
When I POST a REST request to URL "/q/cartItems" with payload
"""
{
	"filters" :{
		"cartId": "cart-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"

Scenario: Storefront - verify cart item fields including variantId
When I POST a REST request to URL "/q/cartItems" with payload
"""
{
	"filters" :{
		"cartId": "cart-1"
	},
	"pageNum": 1,
	"numRowsInPage": 1
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"

Scenario: Storefront - get items for cart-2 (1 item) with variantId
When I POST a REST request to URL "/q/cartItems" with payload
"""
{
	"filters" :{
		"cartId": "cart-2"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.productName" is "Gizmo C"
And the REST response key "list[0].row.sku" is "SKU-003"
And the REST response key "list[0].row.variantId" is "var-3a"
And the REST response key "list[0].row.quantity" is "3"
And the REST response key "list[0].row.unitPrice" is "79900"
And the REST response key "list[0].row.lineTotal" is "239700"

Scenario: Dashboard - get all cart items filtered by productId
When I POST a REST request to URL "/q/allCartItems" with payload
"""
{
	"filters" :{
		"productId": "prod-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.cartId" is "cart-1"
And the REST response key "list[0].row.productName" is "Widget A"
And the REST response key "list[0].row.variantId" is "var-1a"
And the REST response key "list[0].row.unitPrice" is "50000"

Scenario: Dashboard - get all cart items filtered by sku
When I POST a REST request to URL "/q/allCartItems" with payload
"""
{
	"filters" :{
		"sku": "SKU-002"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.productName" is "Gadget B"
And the REST response key "list[0].row.variantId" is "var-2a"

Scenario: Dashboard - get all cart items (unfiltered)
When I POST a REST request to URL "/q/allCartItems" with payload
"""
{
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"

# ===== CART DETAIL: denormalized cart + items =====

Scenario: Dashboard - get cart detail for cart-1 (2 items)
When I POST a REST request to URL "/q/cartDetail" with payload
"""
{
	"filters" :{
		"cartId": "cart-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.cartId" is "cart-1"
And the REST response key "list[0].row.userId" is "user-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"
And the REST response key "list[0].row.subtotal" is "149900"
And the REST response key "list[0].row.currency" is "INR"
And the REST response key "list[0].row.productName" is "Widget A"
And the REST response key "list[0].row.variantId" is "var-1a"
And the REST response key "list[0].row.quantity" is "2"
And the REST response key "list[0].row.unitPrice" is "50000"
And the REST response key "list[0].row.lineTotal" is "100000"

Scenario: Dashboard - get cart detail for cart with no items (cart-3)
When I POST a REST request to URL "/q/cartDetail" with payload
"""
{
	"filters" :{
		"cartId": "cart-3"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.cartId" is "cart-3"
And the REST response key "list[0].row.stateId" is "CHECKOUT_INITIATED"

# ===== CART ACTIVITY LOG =====

Scenario: Dashboard - get activities for cart-1 (3 activities)
When I POST a REST request to URL "/q/cartActivities" with payload
"""
{
	"filters" :{
		"cartId": "cart-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[0].row.cartId" is "cart-1"
And the REST response key "list[0].row.activityName" is "create"
And the REST response key "list[0].row.activitySuccess" is "true"
And the REST response key "list[0].row.activityComment" is "Cart created for user-1"

Scenario: Dashboard - get activities for cart-2 (2 activities)
When I POST a REST request to URL "/q/cartActivities" with payload
"""
{
	"filters" :{
		"cartId": "cart-2"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.activityName" is "create"

# ===== CART FULL: cart + items + activities =====

Scenario: Dashboard - get full cart view for cart-1 (2 items x 3 activities = 6 rows)
When I POST a REST request to URL "/q/cartFull" with payload
"""
{
	"filters" :{
		"cartId": "cart-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "6"
And the REST response key "list[0].row.cartId" is "cart-1"
And the REST response key "list[0].row.userId" is "user-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"
And the REST response key "list[0].row.subtotal" is "149900"

Scenario: Dashboard - get full cart view for cart-2 (1 item x 2 activities = 2 rows)
When I POST a REST request to URL "/q/cartFull" with payload
"""
{
	"filters" :{
		"cartId": "cart-2"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.cartId" is "cart-2"
And the REST response key "list[0].row.productName" is "Gizmo C"
And the REST response key "list[0].row.activityName" is "create"

# ===== STOREFRONT CART: enriched UI view =====

Scenario: Storefront - get enriched cart for user-1 (product + variant + image + stock)
When I POST a REST request to URL "/q/storefrontCart" with payload
"""
{
	"filters" :{
		"userId": "user-1"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.cartId" is "cart-1"
And the REST response key "list[0].row.userId" is "user-1"
And the REST response key "list[0].row.stateId" is "ACTIVE"
And the REST response key "list[0].row.productName" is "Widget A"
And the REST response key "list[0].row.brand" is "WidgetCo"
And the REST response key "list[0].row.categoryName" is "Electronics"
And the REST response key "list[0].row.variantId" is "var-1a"
And the REST response key "list[0].row.variantColor" is "Red"
And the REST response key "list[0].row.variantSize" is "M"
And the REST response key "list[0].row.imageUrl" is "https://cdn.homebase.com/widget-a.jpg"
And the REST response key "list[0].row.availableQuantity" is "95"
And the REST response key "list[0].row.stockStatus" is "AVAILABLE"
And the REST response key "list[0].row.quantity" is "2"
And the REST response key "list[0].row.unitPrice" is "50000"
And the REST response key "list[0].row.lineTotal" is "100000"

Scenario: Storefront - get enriched cart by cartId (admin/support view)
When I POST a REST request to URL "/q/storefrontCartById" with payload
"""
{
	"filters" :{
		"cartId": "cart-2"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.cartId" is "cart-2"
And the REST response key "list[0].row.userId" is "user-2"
And the REST response key "list[0].row.productName" is "Gizmo C"
And the REST response key "list[0].row.brand" is "GizmoCorp"
And the REST response key "list[0].row.categoryName" is "Electronics"
And the REST response key "list[0].row.variantId" is "var-3a"
And the REST response key "list[0].row.variantColor" is "Black"
And the REST response key "list[0].row.variantSize" is "S"
And the REST response key "list[0].row.imageUrl" is "https://cdn.homebase.com/gizmo-c.jpg"
And the REST response key "list[0].row.availableQuantity" is "197"
And the REST response key "list[0].row.stockStatus" is "AVAILABLE"
And the REST response key "list[0].row.sku" is "SKU-003"
And the REST response key "list[0].row.quantity" is "3"
And the REST response key "list[0].row.unitPrice" is "79900"
And the REST response key "list[0].row.lineTotal" is "239700"

Scenario: Storefront - enriched cart returns no rows for user with no active/checkout carts
When I POST a REST request to URL "/q/storefrontCart" with payload
"""
{
	"filters" :{
		"userId": "user-2"
	},
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.cartId" is "cart-2"
And the REST response key "list[0].row.productName" is "Gizmo C"
