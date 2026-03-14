# Entity + Flyway Audit — Completed Changes

## BaseJpaEntity Hierarchy (Chenile)

```
BaseJpaEntity (MappedSuperclass)
├── id              (String UUID, auto-generated)
├── createdTime     (Date)
├── lastModifiedTime(Date)
├── lastModifiedBy  (String)
├── createdBy       (String)
├── tenant          (String)
├── version         (long — @Version for OCC)
└── testEntity      (boolean, @Transient)

AbstractJpaStateEntity extends BaseJpaEntity (for STM workflow aggregates)
├── state           (@Embedded: flowId, stateId)
├── stateEntryTime  (Date)
├── slaYellowDate   (Date)
├── slaRedDate      (Date)
├── slaTendingLate  (int)
└── slaLate         (int)
```

---

## Payment Module — Entities Fixed

| Entity | Before | After |
|---|---|---|
| `PaymentTransaction` | Standalone `@Entity` with manual id/`@GeneratedValue`/`@PrePersist`/`createdAt` | Extends `BaseJpaEntity` |
| `WebhookEvent` | Same standalone pattern | Extends `BaseJpaEntity` |
| `ProviderObject` | Same standalone pattern | Extends `BaseJpaEntity`, overrides `initializeIfRequired()` to also init `fetchedAt` |
| `ReconciliationMismatch` | Same | Extends `BaseJpaEntity` |
| `ReconciliationItem` | Same | Extends `BaseJpaEntity` |
| `ReconciliationRun` | Same | Extends `BaseJpaEntity`, overrides to init `startedAt` |
| `ProviderBalanceTransaction` | Same | Extends `BaseJpaEntity` |
| `PayoutLine` | Same | Extends `BaseJpaEntity` |
| `RefundRequest` | ✓ Already extended `AbstractJpaStateEntity` | No change |
| `Refund` | ✓ Already extended `AbstractJpaStateEntity` | No change |
| `Payout` | ✓ Already extended `AbstractJpaStateEntity` | No change |

> **All removed**: manual `@Id`, `@GeneratedValue(UUID)`, `@PrePersist onCreate()`, and `createdAt` `LocalDateTime` fields.

---

## Other Entity Fixes

| Entity | Fix Applied |
|---|---|
| `Product` | `@Table(name = "product")` → `@Table(name = "products")` (matches V1 SQL and all FKs) |
| `PromoCode` | Removed duplicate `@Id` on `code` field — kept as `@Column(unique=true)`, inherited `id` from `BaseJpaEntity` is the surrogate PK |
| `InventoryReservation` | Changed from standalone entity with `Long id + @GeneratedValue(IDENTITY)` to `extends BaseJpaEntity` (String UUID) |
| `Returnrequest` | Added domain fields: `orderId`, `orderItemId`, `reason`, `quantity`. Fixed `@OneToMany` with proper `@JoinColumn` |
| `CatalogItem` | Removed redundant `createdAt`/`updatedAt` fields and `@PrePersist`/`@PreUpdate` — already inherited from `BaseJpaEntity` |

---

## Flyway Scripts Modified

| Script | Change |
|---|---|
| **V31** (modified) | Added ALL payment infrastructure tables to the `BaseJpaEntity` column loop: `payment_transactions`, `webhook_events`, `provider_objects`, `reconciliation_mismatches`, `reconciliation_items`, `reconciliation_runs`, `provider_balance_transactions`, `payout_lines`. Also added: `settlements`, `settlement_lines`, `returnrequest`, activity log tables. |
| **V36** (new) | Created all previously missing tables: `returnrequest`, `returnrequest_activity`, `promo_code`, `promo_code_activity_log`, `inventory_reservation`, `categories`, `collections`, `catalog_items`, `catalog_item_tags`, `category_product_mapping`, `collection_product_mapping` — all with full `BaseJpaEntity` columns + STM fields where applicable. |

---

## Tables Not Yet in Flyway (Still Missing — Future Work)

| Table | Entity |
|---|---|
| `offers` | `Offer.java` — created by V3 ✓ |
| `stock_reservations` / `stock_reservation_items` | `StockReservation`, `StockReservationItem` — created by V1 ✓ |
| `ledger_accounts`, `ledger_entries` | `LedgerAccount`, `LedgerEntry` — created by V10 ✓ |
