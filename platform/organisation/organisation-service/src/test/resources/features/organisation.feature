Feature: Organisation CRUD — create, retrieve, update company profile

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create organisation
  When I POST a REST request to URL "/organisation" with payload
  """json
  {
    "companyName": "HomeBase Pvt Ltd",
    "legalName": "HomeBase Private Limited",
    "registrationNumber": "U72200KA2024PTC123456",
    "gstNumber": "29AABCU9603R1ZM",
    "panNumber": "AABCU9603R",
    "domain": "homebase.com",
    "active": true,
    "branding": {
      "logoUrl": "https://cdn.homebase.com/logo.png",
      "faviconUrl": "https://cdn.homebase.com/favicon.ico",
      "primaryColor": "#F97316",
      "copyrightText": "© 2026 HomeBase Pvt Ltd",
      "foundedYear": 2024
    },
    "contact": {
      "primaryEmail": "hello@homebase.com",
      "supportEmail": "support@homebase.com",
      "primaryPhone": "+91-9876543210",
      "supportPhone": "+91-1800-123-4567",
      "websiteUrl": "https://www.homebase.com"
    },
    "address": {
      "addressLine1": "123 MG Road",
      "addressLine2": "4th Floor, Tech Park",
      "city": "Bangalore",
      "state": "Karnataka",
      "pincode": "560001",
      "country": "India"
    },
    "locale": {
      "currency": "INR",
      "timezone": "Asia/Kolkata",
      "locale": "en_IN",
      "dateFormat": "dd/MM/yyyy",
      "countryCode": "IN"
    },
    "social": {
      "facebookUrl": "https://facebook.com/homebase",
      "twitterUrl": "https://twitter.com/homebase",
      "instagramUrl": "https://instagram.com/homebase",
      "linkedinUrl": "https://linkedin.com/company/homebase",
      "youtubeUrl": "https://youtube.com/homebase"
    }
  }
  """
  Then the http status code is 200
  And the REST response key "companyName" is "HomeBase Pvt Ltd"
  And store "$.payload.id" from response to "orgId"

Scenario: Retrieve organisation by ID
  When I GET a REST request to URL "/organisation/${orgId}"
  Then the http status code is 200
  And the REST response key "companyName" is "HomeBase Pvt Ltd"
  And the REST response key "domain" is "homebase.com"

Scenario: Update organisation branding
  When I PUT a REST request to URL "/organisation/${orgId}" with payload
  """json
  {
    "companyName": "HomeBase Pvt Ltd",
    "legalName": "HomeBase Private Limited",
    "domain": "homebase.com",
    "active": true,
    "branding": {
      "logoUrl": "https://cdn.homebase.com/logo-v2.png",
      "primaryColor": "#2563EB",
      "copyrightText": "© 2026 HomeBase Pvt Ltd. All rights reserved.",
      "foundedYear": 2024
    }
  }
  """
  Then the http status code is 200
  And the REST response key "branding.primaryColor" is "#2563EB"
