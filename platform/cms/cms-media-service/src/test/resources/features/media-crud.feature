Feature: CMS Media CRUD — create, retrieve, delete

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create a new media asset
  When I POST a REST request to URL "/cms-media" with payload
  """json
  {
    "fileKey": "banners/summer-sale-2024.jpg",
    "originalName": "summer-sale-2024.jpg",
    "cdnUrl": "https://cdn.example.com/banners/summer-sale-2024.jpg",
    "mimeType": "image/jpeg",
    "fileSizeBytes": 245000,
    "width": 1920,
    "height": 600,
    "altText": "Summer Sale Banner",
    "folder": "banners"
  }
  """
  Then the http status code is 200
  And store "$.payload.id" from response to "mediaId"

Scenario: Retrieve the media asset
  When I GET a REST request to URL "/cms-media/${mediaId}"
  Then the http status code is 200
  And the REST response key "originalName" is "summer-sale-2024.jpg"
