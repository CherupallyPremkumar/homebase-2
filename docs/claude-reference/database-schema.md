# Database Schema Reference — 280 Tables, 380+ Changesets

> All migrations live in each module's `{bc}/{bc}-liquibase` sub-module (e.g., `cart/cart-liquibase`). No centralized `db-migrations/` module.  
> Read this when adding new tables, migrations, or checking what tables exist for a module.

## Entity Base Classes (Column Standards)

**BaseJpaEntity** (non-STM entities): `id VARCHAR(255) PK`, `created_time`, `last_modified_time`, `last_modified_by VARCHAR(100)`, `tenant VARCHAR(50)`, `created_by VARCHAR(100)`, `version BIGINT`

**AbstractJpaStateEntity** (STM entities — extends BaseJpaEntity): all above + `state_entry_time`, `sla_yellow_date`, `sla_red_date`, `sla_tending_late INT`, `sla_late INT`, `flow_id VARCHAR(100)`, `state_id VARCHAR(100)`

**Every new entity table MUST include the full BaseJpaEntity columns.** Append-only log/event tables (audit_log, integration_log, payment_attempts, etc.) may omit `version`/`last_modified_*` columns.

## STM Tables (43 — extend AbstractJpaStateEntity)

`user_profiles`, `products`, `cart`, `checkout`, `orders`, `payment`, `offer`, `shipments`, `inventory_item`, `supplier`, `supplier_onboarding`, `returnrequest`, `coupon`, `support_tickets`, `settlements`, `notifications`, `fulfillment_orders`, `reconciliation_batches`, `product_reviews`, `policy`, `fulfillment_saga`, `return_processing_saga`, `supplier_lifecycle_saga`, `gift_cards`, `wallets`, `pay_later_agreements`, `subscriptions`, `deals`, `deal_claims`, `conversations`, `loyalty_members`, `membership_subscriptions`, `fraud_assessments`, `experiments`, `ad_campaigns`, `affiliates`, `commission_payouts`, `seller_violations`, `brands`, `ip_violations`, `warranty_purchases`, `warranty_claims`, `digital_licenses`

## Module → Tables Mapping

| Module | Tables | STM? |
|--------|--------|------|
| **infrastructure** | batch_job_instance, batch_job_execution, batch_job_execution_params, batch_step_execution, batch_step_execution_context, batch_job_execution_context, service_definition, operation_definition, param_definition, client_service_interceptors, client_op_interceptors | No |
| **user** | user_profiles, user_address, user_activity_log, user_recently_viewed, wishlist_items | Yes |
| **product** | products, product_variants, variant_attributes, product_attribute_values, product_media, variant_media, product_tags, product_activity_log, brands, product_specifications, product_questions, categories, category_attributes, attribute_definitions, attribute_options, media_assets, attribute_groups, attribute_sets, attribute_set_groups, attribute_set_attributes, category_attribute_mapping, attribute_definition_locales, attribute_option_locales, product_relationship_types, product_relationships, product_bundle_options, product_bundle_selections, product_custom_options, product_custom_option_values, variant_attribute_values, units_of_measure, external_category_mapping, external_attribute_mapping | Yes |
| **inventory** | inventory_item, inventory_item_activity, inventory_reservations, inventory_movements, inventory_damage_records, fulfillment_centers | Yes |
| **order** | orders, order_items, order_activity, order_addresses, order_status_history | Yes |
| **cart** | cart, cart_item, cart_activity_log | Yes |
| **checkout** | checkout, checkout_item, checkout_saga_log | Yes |
| **offer** | offer, offer_activity_log | Yes |
| **payment** | payment, payment_activity_log, payment_refunds, payment_webhook_log, saved_payment_methods | Yes |
| **shipping** | shipments, shipping_activity, shipping_tracking_events, shipping_rates | Yes |
| **notification** | notifications, notification_metadata, notification_activity, notification_preferences, notification_templates, notification_batches | Yes |
| **returnrequest** | returnrequest, returnrequest_activity | Yes |
| **review** | product_reviews, review_activity, review_images, review_votes, review_responses | Yes |
| **settlement** | settlements, settlement_adjustments, settlement_activity, settlement_line_items | Yes |
| **support** | support_tickets, ticket_messages, ticket_activity | Yes |
| **promo** | coupon, coupon_usage_log, promo_code_activity_log, coupon_compatibility | Yes |
| **pricing** | pricing_rules, price_history | No (stateless OWIZ) |
| **tax** | tax_rates, tax_category_mapping, order_tax_lines | No (stateless OWIZ) |
| **supplier** | supplier, supplier_activity | Yes |
| **onboarding** | supplier_onboarding, onboarding_activity | Yes |
| **catalog** | catalog_items, catalog_item_tags, catalog_categories, category_product_mapping, collections, collection_product_mapping | No (query-only) |
| **cms** | cms_pages, banners | No (query-only) |
| **analytics** | daily_sales_summary, product_performance, supplier_performance, platform_kpi_snapshots, category_performance_daily, conversion_funnel_daily, customer_cohort_metrics, seller_health_summary_daily, platform_alerts | No (read model) |
| **rules-engine** | policy, policy_rule, policy_rule_metadata, policy_activity_log, decisions, decision_metadata, fact_definition | Yes |
| **cconfig** | cconfig | No |
| **fulfillment** | fulfillment_orders, fulfillment_line_items, pick_lists, pack_slips, delivery_attempts, fulfillment_order_activity | Yes |
| **warehouse** | warehouses, warehouse_locations, warehouse_inventory, warehouse_staff, warehouse_staff_shifts | No |
| **reconciliation** | reconciliation_batches, reconciliation_activity, gateway_transactions, system_transactions, transaction_matches, transaction_mismatches | Yes |
| **saga** | fulfillment_saga, fulfillment_saga_activity, return_processing_saga, return_processing_saga_activity, supplier_lifecycle_saga, supplier_lifecycle_saga_activity, return_inspections, return_dispositions, return_labels, supplier_performance_metrics, supplier_probation_history, supplier_appeal_requests | Yes |
| **gift-card** | gift_cards, gift_card_codes, gift_card_transactions, gift_card_redemptions, store_credits, store_credit_transactions, gift_card_activity_log | Yes |
| **wallet** | wallets, wallet_transactions, stored_payment_methods, payment_method_details, installment_plans, pay_later_agreements, installment_schedules | Yes |
| **subscription** | subscriptions, subscription_items, subscription_schedules, subscription_deliveries, subscription_pauses, subscription_plans, subscription_payment_methods, subscription_activity_log | Yes |
| **deals** | deals, deal_products, deal_claims, deal_schedules, coupons, coupon_usage | Yes |
| **address** | addresses, address_verifications, delivery_zones, serviceability_pincodes, delivery_carriers | No |
| **messaging** | conversations, messages, message_attachments, conversation_participants, auto_responses | Yes |
| **loyalty** | loyalty_programs, loyalty_members, loyalty_tiers, points_transactions, rewards, reward_redemptions, membership_plans, membership_subscriptions, behavior_triggers, loyalty_activity_log | Yes |
| **fraud** | fraud_rules, fraud_signals, fraud_assessments, blocked_entities, device_fingerprints, velocity_checks | Yes |
| **experimentation** | experiments, experiment_variants, experiment_assignments, experiment_results, feature_flags, feature_flag_rules, feature_flag_overrides | Yes |
| **i18n** | currencies, exchange_rates, countries, country_settings, locale_languages, locale_translations, import_duties, tax_jurisdictions_international | No |
| **advertising** | ad_campaigns, ad_groups, ads, ad_placements, ad_impressions, ad_clicks, ad_budgets, bid_strategies | Yes |
| **affiliate** | affiliates, affiliate_links, affiliate_clicks, affiliate_conversions, commission_rules, commission_payouts, affiliate_performance | Yes |
| **seller-analytics** | seller_metrics, seller_scorecards, seller_violations, seller_reports, performance_thresholds, seller_benchmarks | Yes |
| **brand-registry** | brands, brand_registrations, brand_trademarks, ip_violations, brand_stores, authorized_sellers, brand_analytics | Yes |
| **warranty** | warranty_plans, warranty_coverages, warranty_purchases, warranty_claims, warranty_claim_items, service_providers | Yes |
| **digital-delivery** | digital_products, digital_licenses, download_links, download_history, license_activations, digital_libraries | Yes |
| **cross-cutting** | audit_log, price_lock_records, payment_attempts, integration_log, customer_segments, customer_segment_members, order_shipment_groups, risk_signals, gst_reconciliation_periods, supplier_kyc_documents | No |

## UI-Driven Column Additions (Post-Prototype Audit)

Added after auditing all 52 admin prototype pages against schema:

| Table | New Columns | Source |
|---|---|---|
| `categories` | `commission_rate_bps`, `is_gated`, `return_policy_days`, `max_shipping_weight_grams`, `listing_min_images`, `listing_min_desc_length` | `db.changelog-product-ui-columns.xml` |
| `product_variants` | `ean`, `model_number` + index on `ean` | `db.changelog-product-ui-columns.xml` |
| `supplier` | `payout_frequency`, `bank_account_verified`, `bank_verification_date` | `db.changelog-supplier-ui-columns.xml` |
| `user_profiles` | `last_active_at`, `last_purchase_date` + index on `last_active_at` | `db.changelog-user-ui-columns.xml` |
| `banners` | `impressions` (for CTR = click_count / impressions) | `db.changelog-cms-ui-columns.xml` |
| `warehouse_staff` | NEW TABLE: employee_code, name, role, phone, is_active, joined_date | `db.changelog-warehouse-staff.xml` |
| `warehouse_staff_shifts` | NEW TABLE: shift_date, shift_code, clock_in/out, items_picked/packed, tasks_completed, errors_count, efficiency_pct | `db.changelog-warehouse-staff.xml` |

## Schema Hardening (Applied After All Domain Tables)

- **13 foreign keys** on child tables (cart_activity→cart, user_address→user_profiles, etc.)
- **3 unique constraints** (product_variants.sku, supplier.tax_id, inventory_item.sku)
- **12 composite indexes** for high-volume queries
- **Money fields** fixed from BIGINT to DECIMAL(19,2) on cart/checkout tables
- **Soft delete** (`deleted_at`) on 8 master entity tables
- **Tenant NOT NULL CHECK** constraints on ~80 tables
- All in `infrastructure/db.changelog-schema-hardening.xml` and `infrastructure/db.changelog-missing-tables.xml`

## Module Readiness Status

### Launch-Critical (must be production-ready)

| Module | Status | What's Missing |
|--------|--------|---------------|
| user | READY | - |
| product | READY | - |
| inventory | READY | - |
| offer | READY | - |
| cart | READY | - |
| pricing | PARTIAL | No STM (stateless OWIZ pipeline — OK by design) |
| promo | READY | - |
| checkout | READY | - |
| order | READY | - |
| payment | PARTIAL | No REST controller (event-driven — OK), verify Kafka wiring |
| shipping | READY | - |
| notification | READY | - |

### Post-Launch (can deploy without these)

| Module | Status | Notes |
|--------|--------|-------|
| fulfillment | READY | DB migrations done |
| return-processing | READY | DB migrations done (saga + inspection/disposition/labels) |
| returnrequest | READY | - |
| settlement | READY | - |
| supplier | READY | - |
| onboarding | READY | - |
| supplier-lifecycle | READY | DB migrations done (saga + performance/probation/appeals) |
| support | READY | - |
| review | READY | - |
| reconciliation | READY | DB migrations done |
| analytics | PARTIAL | Read model only |
| cms | PARTIAL | Read model only |
| catalog | SHELL | Refactored to query-only |
| rules-engine | READY | - |
| cconfig | READY | DB migrations done |
| tax | PARTIAL | Missing STM (stateless OWIZ — OK) |
| warehouse | READY | DB migrations done |
