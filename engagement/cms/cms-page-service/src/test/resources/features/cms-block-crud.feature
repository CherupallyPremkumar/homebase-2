Feature: CMS Block CRUD — create and retrieve content blocks

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a page then add a hero banner block
  When I POST a REST request to URL "/cms-page" with payload
  """json
  {
    "slug": "block-test-page",
    "title": "Block Test Page",
    "pageType": "LANDING",
    "layoutType": "block-based"
  }
  """
  Then the REST response contains key "mutatedEntity"
  And store "$.payload.mutatedEntity.id" from response to "pageId"
  When I POST a REST request to URL "/cms-block" with payload
  """json
  {
    "pageId": "${pageId}",
    "blockType": "hero_banner",
    "data": "{\"heading\":\"Summer Sale\",\"subheading\":\"Up to 60% off\"}",
    "sortOrder": 1,
    "active": true
  }
  """
  Then the http status code is 200
  And store "$.payload.id" from response to "blockId"

Scenario: Create a page and a product carousel block
  When I POST a REST request to URL "/cms-page" with payload
  """json
  {
    "slug": "carousel-test-page",
    "title": "Carousel Test Page",
    "pageType": "LANDING",
    "layoutType": "block-based"
  }
  """
  And store "$.payload.mutatedEntity.id" from response to "pageId2"
  When I POST a REST request to URL "/cms-block" with payload
  """json
  {
    "pageId": "${pageId2}",
    "blockType": "product_carousel",
    "data": "{\"title\":\"Trending Now\",\"strategy\":\"trending\",\"limit\":8}",
    "sortOrder": 2,
    "active": true
  }
  """
  Then the http status code is 200
