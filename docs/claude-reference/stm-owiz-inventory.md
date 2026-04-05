# STM & OWIZ Inventory

> Detailed inventory of all 23 STM flows, 13 OWIZ chains, and cross-BC event mappings.  
> Read this when you need to know specific states/events/transitions for a module.

## STM State Machine Inventory — 23 Flows

All STM XMLs are in `{bc}/{bc}-service/src/main/resources/com/homebase/ecom/{bc}/{bc}-states.xml` (except rules-engine: `stm/ruleset-lifecycle.xml`).

### Launch-Critical STMs (12 flows)

| Module | Flow ID | States | Events | Auto-States | Key Cross-BC Events |
|--------|---------|--------|--------|-------------|---------------------|
| **user** | user-flow | 8 | 15 | CHECK_VERIFICATION_TIMEOUT | — |
| **product** | product-flow | 8 | 13 | — | supplier.SUSPENDED → disableProduct |
| **inventory** | inventory-flow | 15 | 18 | CHECK_DEPLETION, CHECK_DAMAGE_SEVERITY, CHECK_AFTER_DISCARD | order.CANCELLED → releaseReservedStock |
| **offer** | offer-flow | 9 | 9 | CHECK_AUTO_APPROVE, CHECK_EXPIRATION | product.DISABLED → suspend |
| **cart** | cart-flow | 6 | 17 | — | checkout.COMPENSATED → cancelCheckout |
| **promo** | promo-flow | 8 | 8 | CHECK_USAGE, CHECK_EXPIRATION | — |
| **checkout** | checkout-flow | 8 | 8 | CHECK_RETRY_ALLOWED | payment→paymentSuccess/paymentFailed; publishes cart.completeCheckout, order.create |
| **order** | order-flow | 18 | 18 | CHECK_CANCELLATION_WINDOW, CHECK_FRAUD | payment→paymentSucceeded/Failed; shipping→markDelivered/deliveryFailed; publishes fulfillment.initiate, settlement.calculate |
| **payment** | payment-flow | 17 | 17 | CHECK_RETRY, CHECK_CHARGEBACK_OUTCOME | checkout→process; publishes order.paymentSucceeded/Failed, settlement.calculate |
| **shipping** | shipping-flow | 13 | 13 | CHECK_DELIVERY_ATTEMPTS | fulfillment→createLabel; publishes order.markDelivered/deliveryFailed |
| **notification** | notification-flow | 9 | 8 | CHECK_RETRY | consumed by all BCs via Kafka |
| **settlement** | settlement-flow | 10 | 8 | CHECK_AUTO_APPROVE | order.COMPLETED→calculate; publishes notification, reconciliation |

### Post-Launch STMs (8 flows)

| Module | Flow ID | States | Events | Auto-States |
|--------|---------|--------|--------|-------------|
| **returnrequest** | return-request-flow | 11 | 9 | CHECK_AUTO_APPROVE |
| **review** | review-flow | 8 | 10 | CHECK_AUTO_PUBLISH |
| **support** | support-flow | 9 | 10 | CHECK_SLA, CHECK_AUTO_CLOSE |
| **supplier** | supplier-flow | 8 | 9 | CHECK_PERFORMANCE |
| **onboarding** | onboarding-flow | 8 | 9 | CHECK_TRAINING_COMPLETE, CHECK_TIMEOUT |
| **rules-engine** | ruleSet-flow | 4 | 6 | — |
| **reconciliation** | reconciliation-flow | 9 | 7 | CHECK_MISMATCHES |
| **fulfillment** | fulfillment-flow (saga) | 9 | 9 | — |

### Saga Orchestrators (3 flows, 2 in supplier-lifecycle)

| Module | Flow ID | Steps | Compensation? |
|--------|---------|-------|---------------|
| **fulfillment** | fulfillment-flow | INITIATED→INVENTORY_RESERVED→SHIPMENT_CREATED→SHIPPED→CUSTOMER_NOTIFIED→COMPLETED | Yes (CANCELLING→COMPENSATION_DONE) |
| **return-processing** | return-processing-flow | INITIATED→PICKUP_SCHEDULED→ITEM_RECEIVED→CHECK_ITEM_CONDITION→INVENTORY_RESTOCKED→SETTLEMENT_ADJUSTED→REFUNDED→COMPLETED | Yes (CANCELLING→COMPENSATION_DONE) |
| **supplier-lifecycle** | suspend-flow | INITIATED→PRODUCTS_DISABLED→CATALOG_CLEARED→INVENTORY_FROZEN→ORDERS_CANCELLED→COMPLETED | Yes |
| **supplier-lifecycle** | reactivate-flow | INITIATED→PRODUCTS_ENABLED→CATALOG_RESTORED→INVENTORY_UNFROZEN→COMPLETED | Yes |

### Cross-BC Event Flow Map

```
checkout.COMPLETED ──→ cart.completeCheckout + order.create
checkout.COMPENSATED ─→ cart.cancelCheckout (restore cart to ACTIVE)
payment.SUCCEEDED ────→ order.paymentSucceeded
payment.FAILED ───────→ order.paymentFailed
payment.SETTLED ──────→ settlement.calculate
payment.REFUNDED ─────→ order.completeRefund
order.PAID ───────────→ fulfillment saga.initiate
order.CANCELLED ──────→ inventory.releaseReservedStock + payment.initiateRefund
order.COMPLETED ──────→ settlement.calculate
shipping.DELIVERED ───→ order.markDelivered
shipping.RETURNED ────→ order.deliveryFailed
shipping.LOST ────────→ order.deliveryFailed
returnrequest.APPROVED → return-processing saga.initiate
return-processing.COMPLETED → returnrequest.completeReturn
supplier.SUSPENDED ───→ supplier-lifecycle suspend-flow.initiate
supplier.REACTIVATED ─→ supplier-lifecycle reactivate-flow.initiate
```

### Stateless OWIZ Pipelines (NOT STMs — no state persistence)

| Module | Flow File | Purpose |
|--------|-----------|---------|
| **pricing** | pricing-flow.xml | Stateless price calculation chain |
| **tax** | tax-flow.xml | Stateless GST/tax computation chain |
| **reconciliation** | reconciliation-flow.xml | Internal OWIZ processing within reconciliation batch |

## OWIZ Chain Inventory — 13 Chains, 99 Steps

All OWIZ chains use Chenile's `org.chenile.owiz.impl.Chain` with `<command>` steps attached by index. Located in `{bc}/{bc}-service/src/main/resources/com/homebase/ecom/{bc}/`.

### Stateless Processing Pipelines (3 chains)

| Module | File | Steps | Trigger |
|--------|------|-------|---------|
| **pricing** | `pricing-flow.xml` | 10: fetchBase→applySellerOverride→validateSellerMinPrice→applySegmentPricing→applyPromo→applyBundleDiscount→calculateTCS→applyTax→roundAndFormat→buildPriceBreakdown | Cart/checkout price calculation |
| **tax** | `tax-flow.xml` | 8: classifyHSN→resolveSellerBuyerState→determineGSTType→checkExemptions→applyReverseCharge→computeGSTComponents→calculateTCS→buildTaxBreakdown | Payment/checkout tax computation |
| **reconciliation** | `reconciliation-flow.xml` | 8: fetchGatewayTransactions→fetchSystemTransactions→normalizeFormats→matchTransactions→detectDuplicates→applyToleranceThreshold→classifyMismatches→buildReconciliationReport | Daily batch reconciliation |

### STM Transition Sub-Chains (4 chains)

| Module | File | Chain | Steps | Called By |
|--------|------|-------|-------|-----------|
| **checkout** | `checkout-saga.xml` | checkoutSagaChain | 10: validateAddress→reserveInventory→lockPrices→applyCoupon→calculateTax→estimateDelivery→screenFraud→processPayment→createOrder→confirmCheckout | checkout STM PROCESSING transition |
| **payment** | `payment-processing-chain.xml` | paymentProcessingChain | 7: validatePaymentMethod→selectGateway→tokenizeInstrument→submitToGateway→handle3DSChallenge→verifyGatewayResponse→confirmPayment | payment STM INITIATED→PROCESSING |
| **inventory** | `inventory-inbound-chain.xml` | inboundChain | 6: validateAgainstPO→performQualityCheck→categorizeItems→allocateWarehouseLocation→updateStockLevels→generateGRN | inventory STM STOCK_PENDING→STOCK_INSPECTION |
| **analytics** | `analytics-aggregation-chain.xml` | analyticsChain | 7: fetchDailySales→aggregateByProduct→aggregateByCategory→aggregateBySeller→computeConversionRates→computeCustomerMetrics→buildDailySummary | Scheduler (daily 02:00 AM IST) |

### Multi-Chain Files (3 files, 6 chains)

| Module | File | Chains | Total Steps |
|--------|------|--------|-------------|
| **fulfillment** | `fulfillment-owiz-chains.xml` | 3: pick-chain (6), pack-chain (5), carrier-selection-chain (5) | 16 |
| **return-processing** | `return-processing-owiz-chains.xml` | 3: inspect-item-chain (4), restock-chain (3), refund-chain (3) | 10 |
| **supplier-lifecycle** | `supplier-lifecycle-owiz-chains.xml` | 3: disable-products-chain (3), cancel-orders-chain (3), enable-products-chain (3) | 9 |

### Shipping & Settlement Standalone Chains (2 chains)

| Module | File | Steps | Trigger |
|--------|------|-------|---------|
| **shipping** | `shipping-rate-chain.xml` | 7: fetchAvailableCarriers→calculateZoneRates→applyWeightDimensional→applyFreeShippingRules→applyPeakSurcharge→calculateDeliveryEstimates→rankAndBuildOptions | Checkout delivery estimate |
| **settlement** | `settlement-calculation-chain.xml` | 9: fetchOrderLineItems→calculateCategoryCommission→applyPlatformFees→deductReturnFees→deductChargebacks→calculateGSTOnCommission→applyTDS→computeNetPayout→buildSettlementBreakdown | settlement STM CALCULATING transition |
| **notification** | `notification-dispatch-chain.xml` | 7: resolveTemplate→resolveChannelPreference→checkDNDRegistry→formatMessage→dispatchToChannel→handleBounce→confirmDelivery | notification STM DISPATCHING transition |
