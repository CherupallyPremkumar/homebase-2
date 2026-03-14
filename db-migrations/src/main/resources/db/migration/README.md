# Flyway Migrations — Per-Service Organization

## Folder Structure

```
db/migration/
├── common/          ← Shared schema (cross-service tables, audit fields, batch)
├── cart/            ← Cart-specific migrations
├── catalog/         ← Catalog categories, collections, items
├── inventory/       ← Inventory-specific
├── offer/           ← Offer tables
├── onboarding/      ← Supplier onboarding
├── order/           ← Order-specific
├── payment/         ← Payment, refunds, ledger, reconciliation, payouts
├── policy/          ← Policy engine
├── pricing/         ← Pricing engine
├── product/         ← Product-specific
├── promo/           ← Promo codes, usage
├── returnrequest/   ← Return requests
├── settlement/      ← Supplier settlements
├── shipping/        ← Shipments
├── supplier/        ← Supplier management
└── user/            ← User profiles, auth
```

## How It Works Today (Shared DB)

All folders are scanned by Flyway together. The `pom.xml` configures:
```
locations: classpath:db/migration/common,classpath:db/migration/cart,...
```

## How to Split Later (Per-Service DB)

When a service gets its own database:
1. Copy `common/` + `{service}/` to the service's own flyway config
2. Run `flyway baseline` on the new DB
3. Point the service's datasource to the new DB
4. New migrations go only in `{service}/`

## Table Ownership

Some early migrations (V1, V30, V31, V36, V37) create tables for multiple services.
These stay in `common/` because they must run first regardless of which services are deployed.

| Folder | Tables Owned |
|--------|-------------|
| common | products, users, orders, order_items, inventory, stock_reservations, payment_transactions, webhook_events, idempotency_keys, cart, cart_item, supplier, supplierproduct, all *_activity tables, audit_logs, admin_notifications, batch tables, returnrequest, promo_code, inventory_reservation, catalog tables, addresses, product_images, promo_code_usage, wishlist |
| order | order_saga_states |
| payment | ledger_*, reconciliation_*, refunds, refund_requests, provider_objects, payouts, payout_lines, provider_balance_transactions |
| shipping | shipments |
| settlement | settlements, settlement_lines, settlement_activity |
| inventory | (inventory lock version) |
| product | (product status column) |
| offer | offers |
| user | app_user, persistent_logins |
