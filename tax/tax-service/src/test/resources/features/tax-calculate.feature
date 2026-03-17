Feature: Tax Calculation Engine — GST
  Stateless GST calculation. Items + source/destination state in → tax breakdown out.

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Intra-state GST — CGST + SGST for electronics
When I POST a REST request to URL "/tax/_calculate" with payload
"""json
{
  "items": [
    {
      "variantId": "var-electronics-1",
      "productCategory": "ELECTRONICS",
      "taxableAmount": 100000,
      "quantity": 1
    }
  ],
  "sourceState": "KARNATAKA",
  "destinationState": "KARNATAKA",
  "currency": "INR"
}
"""
Then success is true
And the REST response key "taxType" is "INTRA_STATE"
And the REST response key "totalTax" is "18000"
And the REST response key "items[0].cgstAmount" is "9000"
And the REST response key "items[0].sgstAmount" is "9000"
And the REST response key "items[0].hsnCode" is "8471"

Scenario: Inter-state GST — IGST for electronics
When I POST a REST request to URL "/tax/_calculate" with payload
"""json
{
  "items": [
    {
      "variantId": "var-electronics-2",
      "productCategory": "ELECTRONICS",
      "taxableAmount": 100000,
      "quantity": 1
    }
  ],
  "sourceState": "KARNATAKA",
  "destinationState": "MAHARASHTRA",
  "currency": "INR"
}
"""
Then success is true
And the REST response key "taxType" is "INTER_STATE"
And the REST response key "totalTax" is "18000"
And the REST response key "items[0].igstAmount" is "18000"

Scenario: Multi-item with different HSN codes
When I POST a REST request to URL "/tax/_calculate" with payload
"""json
{
  "items": [
    {
      "variantId": "var-phone",
      "productCategory": "ELECTRONICS",
      "taxableAmount": 50000,
      "quantity": 1
    },
    {
      "variantId": "var-shirt",
      "productCategory": "CLOTHING",
      "taxableAmount": 20000,
      "quantity": 2
    }
  ],
  "sourceState": "KARNATAKA",
  "destinationState": "KARNATAKA",
  "currency": "INR"
}
"""
Then success is true
And the REST response key "taxType" is "INTRA_STATE"
And the REST response key "items[0].hsnCode" is "8471"
And the REST response key "items[1].hsnCode" is "6109"
And the REST response key "items[0].totalTax" is "9000"
And the REST response key "items[1].totalTax" is "1000"
And the REST response key "totalTax" is "10000"

Scenario: Reject request with missing source state
When I POST a REST request to URL "/tax/_calculate" with payload
"""json
{
  "items": [
    {
      "variantId": "var-1",
      "productCategory": "ELECTRONICS",
      "taxableAmount": 10000,
      "quantity": 1
    }
  ],
  "destinationState": "KARNATAKA",
  "currency": "INR"
}
"""
Then success is true
And the REST response key "error" is "true"
And the REST response key "message" is "Source state is required"

Scenario: Tax-exempt category — 0% tax
When I POST a REST request to URL "/tax/_calculate" with payload
"""json
{
  "items": [
    {
      "variantId": "var-milk",
      "productCategory": "ESSENTIAL",
      "taxableAmount": 5000,
      "quantity": 1
    }
  ],
  "sourceState": "KARNATAKA",
  "destinationState": "KARNATAKA",
  "currency": "INR"
}
"""
Then success is true
And the REST response key "totalTax" is "0"
And the REST response key "items[0].totalTax" is "0"

Scenario: Luxury item with cess
When I POST a REST request to URL "/tax/_calculate" with payload
"""json
{
  "items": [
    {
      "variantId": "var-jewelry",
      "productCategory": "LUXURY",
      "taxableAmount": 200000,
      "quantity": 1
    }
  ],
  "sourceState": "KARNATAKA",
  "destinationState": "MAHARASHTRA",
  "currency": "INR"
}
"""
Then success is true
And the REST response key "taxType" is "INTER_STATE"
And the REST response key "items[0].igstAmount" is "56000"
And the REST response key "items[0].cessAmount" is "2000"
And the REST response key "items[0].totalTax" is "58000"
And the REST response key "totalTax" is "58000"
