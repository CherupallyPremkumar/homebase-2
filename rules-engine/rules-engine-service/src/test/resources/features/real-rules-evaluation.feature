Feature: Real Rules Evaluation — tests production rules loaded from policy-rules.json
  Validates that all modules get correct decisions from their real rule sets.

# ═══════════════════════════════════════════════════════════════════════════════
# PRICING: Volume Discounts
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Bulk order (10+ items) gets 20% volume discount
  When I evaluate rules for module "PRICING" with facts:
    | totalQuantity | 15       |
    | customerTier  | STANDARD |
  Then the decision effect is "ALLOW"
  And the decision metadata "discountAction" is "PERCENTAGE"
  And the decision metadata "discountValue" is "20"

Scenario: Multi-buy (3-9 items) gets 10% volume discount
  When I evaluate rules for module "PRICING" with facts:
    | totalQuantity | 5        |
    | customerTier  | STANDARD |
  Then the decision effect is "ALLOW"
  And the decision metadata "discountValue" is "10"

Scenario: Single item gets no volume discount (default DENY)
  When I evaluate rules for module "PRICING" with facts:
    | totalQuantity | 1        |
    | customerTier  | STANDARD |
  Then at least one decision has effect "DENY"

# ═══════════════════════════════════════════════════════════════════════════════
# PRICING: Tier Discounts (evaluateAll returns multiple matches)
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Wholesale customer gets 15% tier discount
  When I evaluate all rules for module "PRICING" with facts:
    | totalQuantity | 1         |
    | customerTier  | WHOLESALE |
  Then at least one decision has metadata "discountValue" equal to "15"

Scenario: VIP customer gets 5% tier discount
  When I evaluate all rules for module "PRICING" with facts:
    | totalQuantity | 1   |
    | customerTier  | VIP |
  Then at least one decision has metadata "discountValue" equal to "5"

Scenario: Premium customer gets 3% tier discount
  When I evaluate all rules for module "PRICING" with facts:
    | totalQuantity | 1       |
    | customerTier  | PREMIUM |
  Then at least one decision has metadata "discountValue" equal to "3"

# ═══════════════════════════════════════════════════════════════════════════════
# CHECKOUT: Validation rules
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Normal checkout is allowed
  When I evaluate rules for module "CHECKOUT" with facts:
    | orderTotal     | 500000  |
    | paymentMethod  | UPI     |
    | customerStatus | ACTIVE  |
    | kycVerified    | true    |
  Then the decision effect is "ALLOW"

Scenario: COD blocked above 10k
  When I evaluate rules for module "CHECKOUT" with facts:
    | orderTotal    | 1500000 |
    | paymentMethod | COD     |
  Then the decision effect is "DENY"

Scenario: Blacklisted customer blocked
  When I evaluate rules for module "CHECKOUT" with facts:
    | orderTotal     | 100000     |
    | paymentMethod  | UPI        |
    | customerStatus | BLACKLISTED |
  Then the decision effect is "DENY"

# ═══════════════════════════════════════════════════════════════════════════════
# ORDER: Cancellation rules
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Cancel order before shipping — allowed
  When I evaluate rules for module "ORDER" with facts:
    | fulfillmentStatus | PENDING |
  Then the decision effect is "ALLOW"
  And the decision metadata "refundType" is "FULL"

Scenario: Cancel within 24h of shipping — allowed with deduction
  When I evaluate rules for module "ORDER" with facts:
    | fulfillmentStatus  | SHIPPED |
    | hoursSinceShipped  | 12      |
  Then the decision effect is "ALLOW"
  And the decision metadata "refundType" is "FULL_MINUS_SHIPPING"

Scenario: Cancel after delivery — blocked (use return)
  When I evaluate rules for module "ORDER" with facts:
    | fulfillmentStatus | DELIVERED |
  Then the decision effect is "DENY"

# ═══════════════════════════════════════════════════════════════════════════════
# RETURN: Eligibility rules
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Clothing return within 30 days — allowed
  When I evaluate rules for module "RETURN" with facts:
    | category          | CLOTHING |
    | daysSinceDelivery | 15       |
    | customerTier      | STANDARD |
  Then the decision effect is "ALLOW"

Scenario: Electronics return within 15 days — allowed
  When I evaluate rules for module "RETURN" with facts:
    | category          | ELECTRONICS |
    | daysSinceDelivery | 10          |
    | customerTier      | STANDARD    |
  Then the decision effect is "ALLOW"

Scenario: Digital product return — blocked
  When I evaluate rules for module "RETURN" with facts:
    | category          | DIGITAL  |
    | daysSinceDelivery | 1        |
    | customerTier      | PREMIUM  |
  Then the decision effect is "DENY"

Scenario: Premium member extended 60-day return
  When I evaluate rules for module "RETURN" with facts:
    | category          | CLOTHING |
    | daysSinceDelivery | 45       |
    | customerTier      | PREMIUM  |
  Then the decision effect is "ALLOW"

Scenario: Defective product return after normal window
  When I evaluate rules for module "RETURN" with facts:
    | category          | ELECTRONICS |
    | daysSinceDelivery | 90          |
    | returnReason      | DEFECTIVE   |
    | customerTier      | STANDARD    |
  Then the decision effect is "ALLOW"
  And the decision metadata "refundType" is "FULL_WITH_SHIPPING"

Scenario: Late return (60+ days, standard customer) — blocked
  When I evaluate rules for module "RETURN" with facts:
    | category          | CLOTHING |
    | daysSinceDelivery | 90       |
    | customerTier      | STANDARD |
  Then the decision effect is "DENY"

# ═══════════════════════════════════════════════════════════════════════════════
# OFFER: Listing validation
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Verified seller listing electronics — allowed
  When I evaluate rules for module "OFFER" with facts:
    | category     | ELECTRONICS |
    | sellerTier   | VERIFIED    |
    | sellerStatus | ACTIVE      |
    | listingPrice | 10000       |
    | mapPrice     | 8000        |
  Then the decision effect is "ALLOW"

Scenario: Unverified seller listing electronics — blocked
  When I evaluate rules for module "OFFER" with facts:
    | category     | ELECTRONICS |
    | sellerTier   | BASIC       |
    | sellerStatus | ACTIVE      |
    | listingPrice | 10000       |
    | mapPrice     | 8000        |
  Then the decision effect is "DENY"

Scenario: Listing below MAP price — blocked
  When I evaluate rules for module "OFFER" with facts:
    | category     | CLOTHING |
    | sellerTier   | VERIFIED |
    | sellerStatus | ACTIVE   |
    | listingPrice | 500      |
    | mapPrice     | 999      |
  Then the decision effect is "DENY"

# ═══════════════════════════════════════════════════════════════════════════════
# SUPPLIER: Performance rules
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Supplier with high ODR — suspended
  When I evaluate rules for module "SUPPLIER" with facts:
    | orderDefectRate  | 2.5  |
    | lateShipmentRate | 3.0  |
    | totalOrders      | 200  |
    | customerRating   | 3.8  |
  Then the decision effect is "DENY"
  And the decision metadata "action" is "SUSPEND"

Scenario: Excellent supplier — premium eligible
  When I evaluate rules for module "SUPPLIER" with facts:
    | orderDefectRate  | 0.3  |
    | lateShipmentRate | 1.5  |
    | totalOrders      | 500  |
    | customerRating   | 4.7  |
  Then the decision effect is "ALLOW"
  And the decision metadata "action" is "UPGRADE"
