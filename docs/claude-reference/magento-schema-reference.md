Now I have all the data needed. Let me compile the comprehensive reference document.

Here is the complete Magento 2 database schema reference for all 68 requested product catalog tables, compiled from the official Magento 2 `db_schema.xml` source files on the `2.4-develop` branch of `github.com/magento/magento2`.

---

# MAGENTO 2 PRODUCT CATALOG DATABASE SCHEMA -- COMPLETE COLUMN REFERENCE

All data sourced from the official Magento 2 declarative schema files (`db_schema.xml`) in the following modules:
- `Magento/Catalog`
- `Magento/Eav`
- `Magento/CatalogRule`
- `Magento/Bundle`
- `Magento/ConfigurableProduct`
- `Magento/CatalogInventory`
- `Magento/ProductVideo`

Engine: InnoDB for all tables.

---

## SECTION 1: CORE PRODUCT EAV (7 tables)

### 1. catalog_product_entity
*Catalog Product Table*

| Column | Type | Unsigned | Nullable | Identity | Default | Comment |
|--------|------|----------|----------|----------|---------|---------|
| entity_id | INT | YES | NO | YES | auto | Entity ID (PK) |
| attribute_set_id | SMALLINT | YES | NO | NO | 0 | Attribute Set ID |
| type_id | VARCHAR(32) | - | NO | NO | 'simple' | Type ID |
| sku | VARCHAR(64) | - | NO | NO | - | SKU |
| has_options | SMALLINT | NO | NO | NO | 0 | Has Options |
| required_options | SMALLINT | YES | NO | NO | 0 | Required Options |
| created_at | TIMESTAMP | - | NO | NO | CURRENT_TIMESTAMP | Creation Time |
| updated_at | TIMESTAMP | - | NO | NO | CURRENT_TIMESTAMP (on_update) | Update Time |

**PK:** `entity_id`
**Indexes:** `attribute_set_id` (btree), `sku` (btree)

---

### 2. catalog_product_entity_datetime
*Catalog Product Datetime Attribute Backend Table*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | DATETIME | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `attribute_id`, `store_id` (btree)

---

### 3. catalog_product_entity_decimal
*Catalog Product Decimal Attribute Backend Table*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | DECIMAL(20,6) | NO | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id`, `attribute_id` (btree)

---

### 4. catalog_product_entity_int
*Catalog Product Integer Attribute Backend Table*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | INT | NO | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `attribute_id`, (`attribute_id`, `entity_id`), `store_id`, (`attribute_id`, `store_id`, `value`) (btree)

---

### 5. catalog_product_entity_text
*Catalog Product Text Attribute Backend Table*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | MEDIUMTEXT | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `attribute_id`, `store_id` (btree)

---

### 6. catalog_product_entity_varchar
*Catalog Product Varchar Attribute Backend Table*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `attribute_id`, `store_id` (btree)

---

### 7. catalog_product_entity_gallery
*Catalog Product Gallery Attribute Backend Table (legacy)*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| position | INT | NO | NO | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `entity_id`, `attribute_id`, `store_id` (btree)

---

## SECTION 2: EAV METADATA (9 tables)

### 8. eav_entity_type

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_type_id | SMALLINT | YES | NO | YES | auto |
| entity_type_code | VARCHAR(50) | - | NO | NO | - |
| entity_model | VARCHAR(255) | - | NO | NO | - |
| attribute_model | VARCHAR(255) | - | YES | NO | NULL |
| entity_table | VARCHAR(255) | - | YES | NO | NULL |
| value_table_prefix | VARCHAR(255) | - | YES | NO | NULL |
| entity_id_field | VARCHAR(255) | - | YES | NO | NULL |
| is_data_sharing | SMALLINT | YES | NO | NO | 1 |
| data_sharing_key | VARCHAR(100) | - | YES | NO | 'default' |
| default_attribute_set_id | SMALLINT | YES | NO | NO | 0 |
| increment_model | VARCHAR(255) | - | YES | NO | NULL |
| increment_per_store | SMALLINT | YES | NO | NO | 0 |
| increment_pad_length | SMALLINT | YES | NO | NO | 8 |
| increment_pad_char | VARCHAR(1) | - | NO | NO | '0' |
| additional_attribute_table | VARCHAR(255) | - | YES | NO | NULL |
| entity_attribute_collection | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `entity_type_id`
**Indexes:** `entity_type_code` (btree)

---

### 9. eav_attribute

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| attribute_id | SMALLINT | YES | NO | YES | auto |
| entity_type_id | SMALLINT | YES | NO | NO | 0 |
| attribute_code | VARCHAR(255) | - | NO | NO | - |
| attribute_model | VARCHAR(255) | - | YES | NO | NULL |
| backend_model | VARCHAR(255) | - | YES | NO | NULL |
| backend_type | VARCHAR(8) | - | NO | NO | 'static' |
| backend_table | VARCHAR(255) | - | YES | NO | NULL |
| frontend_model | VARCHAR(255) | - | YES | NO | NULL |
| frontend_input | VARCHAR(50) | - | YES | NO | NULL |
| frontend_label | VARCHAR(255) | - | YES | NO | NULL |
| frontend_class | VARCHAR(255) | - | YES | NO | NULL |
| source_model | VARCHAR(255) | - | YES | NO | NULL |
| is_required | SMALLINT | YES | NO | NO | 0 |
| is_user_defined | SMALLINT | YES | NO | NO | 0 |
| default_value | TEXT | - | YES | NO | NULL |
| is_unique | SMALLINT | YES | NO | NO | 0 |
| note | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `attribute_id`
**UNIQUE:** (`entity_type_id`, `attribute_code`)
**FK:** `entity_type_id` -> `eav_entity_type.entity_type_id` CASCADE
**Indexes:** (`frontend_input`, `entity_type_id`, `is_user_defined`) (btree)

---

### 10. eav_attribute_set

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| attribute_set_id | SMALLINT | YES | NO | YES | auto |
| entity_type_id | SMALLINT | YES | NO | NO | 0 |
| attribute_set_name | VARCHAR(255) | - | YES | NO | NULL |
| sort_order | SMALLINT | NO | NO | NO | 0 |

**PK:** `attribute_set_id`
**UNIQUE:** (`entity_type_id`, `attribute_set_name`)
**FK:** `entity_type_id` -> `eav_entity_type.entity_type_id` CASCADE
**Indexes:** (`entity_type_id`, `sort_order`) (btree)

---

### 11. eav_attribute_group

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| attribute_group_id | SMALLINT | YES | NO | YES | auto |
| attribute_set_id | SMALLINT | YES | NO | NO | 0 |
| attribute_group_name | VARCHAR(255) | - | YES | NO | NULL |
| sort_order | SMALLINT | NO | NO | NO | 0 |
| default_id | SMALLINT | YES | YES | NO | 0 |
| attribute_group_code | VARCHAR(255) | - | NO | NO | - |
| tab_group_code | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `attribute_group_id`
**UNIQUE:** (`attribute_set_id`, `attribute_group_name`), (`attribute_set_id`, `attribute_group_code`)
**FK:** `attribute_set_id` -> `eav_attribute_set.attribute_set_id` CASCADE
**Indexes:** (`attribute_set_id`, `sort_order`) (btree)

---

### 12. eav_entity_attribute

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_attribute_id | INT | YES | NO | YES | auto |
| entity_type_id | SMALLINT | YES | NO | NO | 0 |
| attribute_set_id | SMALLINT | YES | NO | NO | 0 |
| attribute_group_id | SMALLINT | YES | NO | NO | 0 |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| sort_order | SMALLINT | NO | NO | NO | 0 |

**PK:** `entity_attribute_id`
**UNIQUE:** (`attribute_set_id`, `attribute_id`), (`attribute_group_id`, `attribute_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `attribute_group_id` -> `eav_attribute_group.attribute_group_id` CASCADE
**Indexes:** (`attribute_set_id`, `sort_order`), `attribute_id` (btree)

---

### 13. eav_attribute_label

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| attribute_label_id | INT | YES | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `attribute_label_id`
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id`, (`attribute_id`, `store_id`) (btree)

---

### 14. eav_attribute_option

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_id | INT | YES | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| sort_order | SMALLINT | YES | NO | NO | 0 |

**PK:** `option_id`
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE
**Indexes:** `attribute_id` (btree)

---

### 15. eav_attribute_option_value

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| option_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`store_id`, `option_id`)
**FK:** `option_id` -> `eav_attribute_option.option_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `option_id`, `store_id` (btree)

---

### 16. catalog_eav_attribute
*Extended EAV attribute configuration specific to Catalog*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| attribute_id | SMALLINT | YES | NO | NO | - |
| frontend_input_renderer | VARCHAR(255) | - | YES | NO | NULL |
| is_global | SMALLINT | YES | NO | NO | 1 |
| is_visible | SMALLINT | YES | NO | NO | 1 |
| is_searchable | SMALLINT | YES | NO | NO | 0 |
| is_filterable | SMALLINT | YES | NO | NO | 0 |
| is_comparable | SMALLINT | YES | NO | NO | 0 |
| is_visible_on_front | SMALLINT | YES | NO | NO | 0 |
| is_html_allowed_on_front | SMALLINT | YES | NO | NO | 0 |
| is_used_for_price_rules | SMALLINT | YES | NO | NO | 0 |
| is_filterable_in_search | SMALLINT | YES | NO | NO | 0 |
| used_in_product_listing | SMALLINT | YES | NO | NO | 0 |
| used_for_sort_by | SMALLINT | YES | NO | NO | 0 |
| apply_to | VARCHAR(255) | - | YES | NO | NULL |
| is_visible_in_advanced_search | SMALLINT | YES | NO | NO | 0 |
| position | INT | NO | NO | NO | 0 |
| is_wysiwyg_enabled | SMALLINT | YES | NO | NO | 0 |
| is_used_for_promo_rules | SMALLINT | YES | NO | NO | 0 |
| is_required_in_admin_store | SMALLINT | YES | NO | NO | 0 |
| is_used_in_grid | SMALLINT | YES | NO | NO | 0 |
| is_visible_in_grid | SMALLINT | YES | NO | NO | 0 |
| is_filterable_in_grid | SMALLINT | YES | NO | NO | 0 |

**PK:** `attribute_id`
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE
**Indexes:** `used_for_sort_by`, `used_in_product_listing` (btree)

---

## SECTION 3: MEDIA (5 tables)

### 17. catalog_product_entity_media_gallery

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |
| media_type | VARCHAR(32) | - | NO | NO | 'image' |
| disabled | SMALLINT | YES | NO | NO | 0 |

**PK:** `value_id`
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE
**Indexes:** `attribute_id` (btree)

---

### 18. catalog_product_entity_media_gallery_value

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| label | VARCHAR(255) | - | YES | NO | NULL |
| position | INT | YES | YES | NO | NULL |
| disabled | SMALLINT | YES | YES | NO | 0 |
| record_id | INT | YES | NO | YES | auto |

**PK:** `record_id`
**FK:** `value_id` -> `catalog_product_entity_media_gallery.value_id` CASCADE, `store_id` -> `store.store_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `store_id`, `entity_id`, `value_id`, (`entity_id`, `value_id`, `store_id`) (btree)

---

### 19. catalog_product_entity_media_gallery_value_to_entity
*Link Media value to Product entity table*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | NO | - |
| entity_id | INT | YES | NO | NO | - |

**UNIQUE:** (`value_id`, `entity_id`)
**FK:** `value_id` -> `catalog_product_entity_media_gallery.value_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE

---

### 20. catalog_product_entity_media_gallery_value_video
*(Defined in Magento/ProductVideo module)*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | NO | - |
| store_id | SMALLINT | YES | NO | NO | 0 |
| provider | VARCHAR(32) | - | YES | NO | NULL |
| url | TEXT | - | YES | NO | NULL |
| title | VARCHAR(255) | - | YES | NO | NULL |
| description | TEXT | - | YES | NO | NULL |
| metadata | TEXT | - | YES | NO | NULL |

**PK:** (`value_id`, `store_id`)
**FK:** `value_id` -> `catalog_product_entity_media_gallery.value_id` CASCADE, `store_id` -> `store.store_id` CASCADE

---

### 21. catalog_product_entity_gallery
*(Listed above as table #7 -- legacy table, included here for completeness)*

---

## SECTION 4: PRICING (5 tables)

### 22. catalog_product_entity_tier_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| entity_id | INT | YES | NO | NO | 0 |
| all_groups | SMALLINT | YES | NO | NO | 1 |
| customer_group_id | INT | YES | NO | NO | 0 |
| qty | DECIMAL(12,4) | NO | NO | NO | 1 |
| value | DECIMAL(20,6) | NO | NO | NO | 0 |
| website_id | SMALLINT | YES | NO | NO | - |
| percentage_value | DECIMAL(5,2) | NO | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `all_groups`, `customer_group_id`, `qty`, `website_id`)
**FK:** `customer_group_id` -> `customer_group.customer_group_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `website_id` -> `store_website.website_id` CASCADE
**Indexes:** `customer_group_id`, `website_id` (btree)

---

### 23. catalog_product_index_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | NO | - |
| customer_group_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |
| tax_class_id | SMALLINT | YES | YES | NO | 0 |
| price | DECIMAL(20,6) | NO | YES | NO | NULL |
| final_price | DECIMAL(20,6) | NO | YES | NO | NULL |
| min_price | DECIMAL(20,6) | NO | YES | NO | NULL |
| max_price | DECIMAL(20,6) | NO | YES | NO | NULL |
| tier_price | DECIMAL(20,6) | NO | YES | NO | NULL |

**PK:** (`entity_id`, `customer_group_id`, `website_id`)
**Indexes:** `customer_group_id`, `min_price`, (`website_id`, `customer_group_id`, `min_price`) (btree)

---

### 24. catalogrule
*(Defined in Magento/CatalogRule module)*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| rule_id | INT | YES | NO | YES | auto |
| name | VARCHAR(255) | - | YES | NO | NULL |
| description | TEXT | - | YES | NO | NULL |
| from_date | DATE | - | YES | NO | NULL |
| to_date | DATE | - | YES | NO | NULL |
| is_active | SMALLINT | NO | NO | NO | 0 |
| conditions_serialized | MEDIUMTEXT | - | YES | NO | NULL |
| actions_serialized | MEDIUMTEXT | - | YES | NO | NULL |
| stop_rules_processing | SMALLINT | NO | NO | NO | 1 |
| sort_order | INT | YES | NO | NO | 0 |
| simple_action | VARCHAR(32) | - | YES | NO | NULL |
| discount_amount | DECIMAL(20,6) | NO | NO | NO | 0 |

**PK:** `rule_id`
**Indexes:** (`is_active`, `sort_order`, `to_date`, `from_date`) (btree)

---

### 25. catalogrule_product

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| rule_product_id | INT | YES | NO | YES | auto |
| rule_id | INT | YES | NO | NO | 0 |
| from_time | INT | YES | NO | NO | 0 |
| to_time | INT | YES | NO | NO | 0 |
| customer_group_id | INT | NO | YES | NO | NULL |
| product_id | INT | YES | NO | NO | 0 |
| action_operator | VARCHAR(10) | - | NO | NO | 'to_fixed' |
| action_amount | DECIMAL(20,6) | NO | NO | NO | 0 |
| action_stop | SMALLINT | NO | NO | NO | 0 |
| sort_order | INT | YES | NO | NO | 0 |
| website_id | SMALLINT | YES | NO | NO | - |

**PK:** `rule_product_id`
**UNIQUE:** (`rule_id`, `from_time`, `to_time`, `website_id`, `customer_group_id`, `product_id`, `sort_order`)
**Indexes:** `customer_group_id`, `website_id`, `from_time`, `to_time`, `product_id` (btree)

---

### 26. catalogrule_product_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| rule_product_price_id | INT | YES | NO | YES | auto |
| rule_date | DATE | - | NO | NO | - |
| customer_group_id | INT | NO | YES | NO | NULL |
| product_id | INT | YES | NO | NO | 0 |
| rule_price | DECIMAL(20,6) | NO | NO | NO | 0 |
| website_id | SMALLINT | YES | NO | NO | - |
| latest_start_date | DATE | - | YES | NO | NULL |
| earliest_end_date | DATE | - | YES | NO | NULL |

**PK:** `rule_product_price_id`
**UNIQUE:** (`rule_date`, `website_id`, `customer_group_id`, `product_id`)
**Indexes:** `customer_group_id`, `website_id`, `product_id` (btree)

---

## SECTION 5: PRODUCT RELATIONSHIPS (6 tables)

### 27. catalog_product_link

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| link_id | INT | YES | NO | YES | auto |
| product_id | INT | YES | NO | NO | 0 |
| linked_product_id | INT | YES | NO | NO | 0 |
| link_type_id | SMALLINT | YES | NO | NO | 0 |

**PK:** `link_id`
**UNIQUE:** (`link_type_id`, `product_id`, `linked_product_id`)
**FK:** `linked_product_id` -> `catalog_product_entity.entity_id` CASCADE, `product_id` -> `catalog_product_entity.entity_id` CASCADE, `link_type_id` -> `catalog_product_link_type.link_type_id` CASCADE
**Indexes:** `product_id`, `linked_product_id` (btree)

---

### 28. catalog_product_link_type

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| link_type_id | SMALLINT | YES | NO | YES | auto |
| code | VARCHAR(32) | - | YES | NO | NULL |

**PK:** `link_type_id`

---

### 29. catalog_product_link_attribute

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| product_link_attribute_id | SMALLINT | YES | NO | YES | auto |
| link_type_id | SMALLINT | YES | NO | NO | 0 |
| product_link_attribute_code | VARCHAR(32) | - | YES | NO | NULL |
| data_type | VARCHAR(32) | - | YES | NO | NULL |

**PK:** `product_link_attribute_id`
**FK:** `link_type_id` -> `catalog_product_link_type.link_type_id` CASCADE
**Indexes:** `link_type_id` (btree)

---

### 30. catalog_product_link_attribute_int

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| product_link_attribute_id | SMALLINT | YES | YES | NO | NULL |
| link_id | INT | YES | NO | NO | - |
| value | INT | NO | NO | NO | 0 |

**PK:** `value_id`
**UNIQUE:** (`product_link_attribute_id`, `link_id`)
**FK:** `link_id` -> `catalog_product_link.link_id` CASCADE, `product_link_attribute_id` -> `catalog_product_link_attribute.product_link_attribute_id` CASCADE
**Indexes:** `link_id` (btree)

---

### 31. catalog_product_link_attribute_decimal

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| product_link_attribute_id | SMALLINT | YES | YES | NO | NULL |
| link_id | INT | YES | NO | NO | - |
| value | DECIMAL(20,6) | NO | NO | NO | 0 |

**PK:** `value_id`
**UNIQUE:** (`product_link_attribute_id`, `link_id`)
**FK:** `link_id` -> `catalog_product_link.link_id` CASCADE, `product_link_attribute_id` -> `catalog_product_link_attribute.product_link_attribute_id` CASCADE
**Indexes:** `link_id` (btree)

---

### 32. catalog_product_link_attribute_varchar

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| product_link_attribute_id | SMALLINT | YES | NO | NO | 0 |
| link_id | INT | YES | NO | NO | - |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`product_link_attribute_id`, `link_id`)
**FK:** `link_id` -> `catalog_product_link.link_id` CASCADE, `product_link_attribute_id` -> `catalog_product_link_attribute.product_link_attribute_id` CASCADE
**Indexes:** `link_id` (btree)

---

## SECTION 6: CUSTOM OPTIONS (6 tables)

### 33. catalog_product_option

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_id | INT | YES | NO | YES | auto |
| product_id | INT | YES | NO | NO | 0 |
| type | VARCHAR(50) | - | YES | NO | NULL |
| is_require | SMALLINT | NO | NO | NO | 1 |
| sku | VARCHAR(64) | - | YES | NO | NULL |
| max_characters | INT | YES | YES | NO | NULL |
| file_extension | VARCHAR(50) | - | YES | NO | NULL |
| image_size_x | SMALLINT | YES | YES | NO | NULL |
| image_size_y | SMALLINT | YES | YES | NO | NULL |
| sort_order | INT | YES | NO | NO | 0 |

**PK:** `option_id`
**FK:** `product_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `product_id` (btree)

---

### 34. catalog_product_option_title

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_title_id | INT | YES | NO | YES | auto |
| option_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| title | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `option_title_id`
**UNIQUE:** (`option_id`, `store_id`)
**FK:** `option_id` -> `catalog_product_option.option_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id` (btree)

---

### 35. catalog_product_option_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_price_id | INT | YES | NO | YES | auto |
| option_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| price | DECIMAL(20,6) | NO | NO | NO | 0 |
| price_type | VARCHAR(7) | - | NO | NO | 'fixed' |

**PK:** `option_price_id`
**UNIQUE:** (`option_id`, `store_id`)
**FK:** `option_id` -> `catalog_product_option.option_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id` (btree)

---

### 36. catalog_product_option_type_value

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_type_id | INT | YES | NO | YES | auto |
| option_id | INT | YES | NO | NO | 0 |
| sku | VARCHAR(64) | - | YES | NO | NULL |
| sort_order | INT | YES | NO | NO | 0 |

**PK:** `option_type_id`
**FK:** `option_id` -> `catalog_product_option.option_id` CASCADE
**Indexes:** `option_id` (btree)

---

### 37. catalog_product_option_type_title

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_type_title_id | INT | YES | NO | YES | auto |
| option_type_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| title | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `option_type_title_id`
**UNIQUE:** (`option_type_id`, `store_id`)
**FK:** `option_type_id` -> `catalog_product_option_type_value.option_type_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id` (btree)

---

### 38. catalog_product_option_type_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_type_price_id | INT | YES | NO | YES | auto |
| option_type_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| price | DECIMAL(20,6) | NO | NO | NO | 0 |
| price_type | VARCHAR(7) | - | NO | NO | 'fixed' |

**PK:** `option_type_price_id`
**UNIQUE:** (`option_type_id`, `store_id`)
**FK:** `option_type_id` -> `catalog_product_option_type_value.option_type_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id` (btree)

---

## SECTION 7: CONFIGURABLE / VARIANTS (4 tables)

### 39. catalog_product_super_attribute
*(Defined in Magento/ConfigurableProduct module)*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| product_super_attribute_id | INT | YES | NO | YES | auto |
| product_id | INT | YES | NO | NO | 0 |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| position | SMALLINT | YES | NO | NO | 0 |

**PK:** `product_super_attribute_id`
**UNIQUE:** (`product_id`, `attribute_id`)
**FK:** `product_id` -> `catalog_product_entity.entity_id` CASCADE

---

### 40. catalog_product_super_attribute_label

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| product_super_attribute_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| use_default | SMALLINT | YES | YES | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`product_super_attribute_id`, `store_id`)
**FK:** `product_super_attribute_id` -> `catalog_product_super_attribute.product_super_attribute_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `store_id` (btree)

---

### 41. catalog_product_super_attribute_pricing
**DOES NOT EXIST IN MAGENTO 2.** This table existed in Magento 1 for configurable product price markups/markdowns. In Magento 2, the pricing model changed -- each simple (child) product carries its own independent price. The parent configurable product price is ignored in favor of the selected child product's price. This table was removed/deprecated in Magento 2.

---

### 42. catalog_product_super_link

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| link_id | INT | YES | NO | YES | auto |
| product_id | INT | YES | NO | NO | 0 |
| parent_id | INT | YES | NO | NO | 0 |

**PK:** `link_id`
**UNIQUE:** (`product_id`, `parent_id`)
**FK:** `product_id` -> `catalog_product_entity.entity_id` CASCADE, `parent_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `parent_id` (btree)

---

## SECTION 8: BUNDLES (6 tables)
*(All defined in Magento/Bundle module)*

### 43. catalog_product_bundle_option

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| option_id | INT | YES | NO | YES | auto |
| parent_id | INT | YES | NO | NO | - |
| required | SMALLINT | YES | NO | NO | 0 |
| position | INT | YES | NO | NO | 0 |
| type | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `option_id`
**FK:** `parent_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `parent_id` (btree)

---

### 44. catalog_product_bundle_option_value

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | YES | NO | YES | auto |
| option_id | INT | YES | NO | NO | - |
| store_id | SMALLINT | YES | NO | NO | - |
| title | VARCHAR(255) | - | YES | NO | NULL |
| parent_product_id | INT | YES | NO | NO | - |

**PK:** `value_id`
**UNIQUE:** (`option_id`, `parent_product_id`, `store_id`)
**FK:** `option_id` -> `catalog_product_bundle_option.option_id` CASCADE

---

### 45. catalog_product_bundle_selection

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| selection_id | INT | YES | NO | YES | auto |
| option_id | INT | YES | NO | NO | - |
| parent_product_id | INT | YES | NO | NO | - |
| product_id | INT | YES | NO | NO | - |
| position | INT | YES | NO | NO | 0 |
| is_default | SMALLINT | YES | NO | NO | 0 |
| selection_price_type | SMALLINT | YES | NO | NO | 0 |
| selection_price_value | DECIMAL(12,4) | NO | NO | NO | 0 |
| selection_qty | DECIMAL(12,4) | NO | YES | NO | NULL |
| selection_can_change_qty | SMALLINT | NO | NO | NO | 0 |

**PK:** `selection_id`
**FK:** `option_id` -> `catalog_product_bundle_option.option_id` CASCADE, `product_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `option_id`, `product_id` (btree)

---

### 46. catalog_product_bundle_selection_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| selection_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |
| selection_price_type | SMALLINT | YES | NO | NO | 0 |
| selection_price_value | DECIMAL(20,6) | NO | NO | NO | 0 |
| parent_product_id | INT | YES | NO | NO | - |

**PK:** (`selection_id`, `parent_product_id`, `website_id`)
**FK:** `website_id` -> `store_website.website_id` CASCADE, `selection_id` -> `catalog_product_bundle_selection.selection_id` CASCADE
**Indexes:** `website_id` (btree)

---

### 47. catalog_product_bundle_price_index

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |
| customer_group_id | INT | YES | NO | NO | - |
| min_price | DECIMAL(20,6) | NO | NO | NO | - |
| max_price | DECIMAL(20,6) | NO | NO | NO | - |

**PK:** (`entity_id`, `website_id`, `customer_group_id`)
**FK:** `customer_group_id` -> `customer_group.customer_group_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `website_id` -> `store_website.website_id` CASCADE
**Indexes:** `website_id`, `customer_group_id` (btree)

---

### 48. catalog_product_bundle_stock_index

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |
| stock_id | SMALLINT | YES | NO | NO | - |
| option_id | INT | YES | NO | NO | 0 |
| stock_status | SMALLINT | NO | YES | NO | 0 |

**PK:** (`entity_id`, `website_id`, `stock_id`, `option_id`)

---

## SECTION 9: CATEGORIES (7 tables)

### 49. catalog_category_entity

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | YES | auto |
| attribute_set_id | SMALLINT | YES | NO | NO | 0 |
| parent_id | INT | YES | NO | NO | 0 |
| created_at | TIMESTAMP | - | NO | NO | CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | - | NO | NO | CURRENT_TIMESTAMP (on_update) |
| path | VARCHAR(255) | - | NO | NO | - |
| position | INT | NO | NO | NO | - |
| level | INT | NO | NO | NO | 0 |
| children_count | INT | NO | NO | NO | - |

**PK:** `entity_id`
**Indexes:** `level`, `path`, (`parent_id`, `level`, `position`) (btree)

---

### 50. catalog_category_entity_datetime

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | DATETIME | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_category_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `entity_id`, `attribute_id`, `store_id` (btree)

---

### 51. catalog_category_entity_decimal

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | DECIMAL(20,6) | NO | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_category_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `entity_id`, `attribute_id`, `store_id` (btree)

---

### 52. catalog_category_entity_int

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | INT | NO | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_category_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `entity_id`, `attribute_id`, `store_id`, (`store_id`, `value`) (btree)

---

### 53. catalog_category_entity_text

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | MEDIUMTEXT | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_category_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `entity_id`, `attribute_id`, `store_id` (btree)

---

### 54. catalog_category_entity_varchar

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| value_id | INT | NO | NO | YES | auto |
| attribute_id | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| entity_id | INT | YES | NO | NO | 0 |
| value | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `value_id`
**UNIQUE:** (`entity_id`, `attribute_id`, `store_id`)
**FK:** `attribute_id` -> `eav_attribute.attribute_id` CASCADE, `entity_id` -> `catalog_category_entity.entity_id` CASCADE, `store_id` -> `store.store_id` CASCADE
**Indexes:** `entity_id`, `attribute_id`, `store_id` (btree)

---

### 55. catalog_category_product

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | NO | NO | YES | auto |
| category_id | INT | YES | NO | NO | 0 |
| product_id | INT | YES | NO | NO | 0 |
| position | INT | NO | NO | NO | 0 |

**PK:** (`entity_id`, `category_id`, `product_id`)
**UNIQUE:** (`category_id`, `product_id`)
**FK:** `product_id` -> `catalog_product_entity.entity_id` CASCADE, `category_id` -> `catalog_category_entity.entity_id` CASCADE
**Indexes:** `product_id`, (`category_id`, `product_id`, `position`) (btree)

---

## SECTION 10: INDEXING (11 tables)

### 56. catalog_product_website

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| product_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |

**PK:** (`product_id`, `website_id`)
**FK:** `website_id` -> `store_website.website_id` CASCADE, `product_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `website_id` (btree)

---

### 57. catalog_product_enabled_index
**NOT DEFINED in db_schema.xml.** This table does not exist in the Magento 2 declarative schema. In Magento 2, product enabled/disabled status is handled through `catalog_product_entity_int` (the `status` attribute) and the `catalog_product_index_eav` table. The flat index and enabled index were deprecated in favor of the EAV index tables.

---

### 58. catalog_product_index_eav

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | NO | - |
| attribute_id | SMALLINT | YES | NO | NO | - |
| store_id | SMALLINT | YES | NO | NO | - |
| value | INT | YES | NO | NO | - |
| source_id | INT | YES | NO | NO | 0 |

**PK:** (`entity_id`, `attribute_id`, `store_id`, `value`, `source_id`)
**Indexes:** `attribute_id`, `store_id`, `value` (btree)

---

### 59. catalog_product_index_eav_decimal

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | NO | - |
| attribute_id | SMALLINT | YES | NO | NO | - |
| store_id | SMALLINT | YES | NO | NO | - |
| value | DECIMAL(12,4) | NO | NO | NO | - |
| source_id | INT | YES | NO | NO | 0 |

**PK:** (`entity_id`, `attribute_id`, `store_id`, `value`, `source_id`)
**Indexes:** `attribute_id`, `store_id`, `value` (btree)

---

### 60. catalog_product_index_website

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| website_id | SMALLINT | YES | NO | NO | - |
| default_store_id | SMALLINT | YES | NO | NO | - |
| website_date | DATE | - | YES | NO | NULL |
| rate | FLOAT | NO | YES | NO | 1 |

**PK:** `website_id`
**FK:** `website_id` -> `store_website.website_id` CASCADE
**Indexes:** `website_date` (btree)

---

### 61. catalog_category_product_index

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| category_id | INT | YES | NO | NO | 0 |
| product_id | INT | YES | NO | NO | 0 |
| position | INT | NO | YES | NO | NULL |
| is_parent | SMALLINT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | NO | NO | 0 |
| visibility | SMALLINT | YES | NO | NO | - |

**PK:** (`category_id`, `product_id`, `store_id`)
**Indexes:** (`product_id`, `store_id`, `category_id`, `visibility`), (`store_id`, `category_id`, `visibility`, `is_parent`, `position`) (btree)

---

### 62. catalog_product_index_tier_price

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| entity_id | INT | YES | NO | NO | - |
| customer_group_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |
| min_price | DECIMAL(20,6) | NO | YES | NO | NULL |

**PK:** (`entity_id`, `customer_group_id`, `website_id`)
**FK:** `customer_group_id` -> `customer_group.customer_group_id` CASCADE, `entity_id` -> `catalog_product_entity.entity_id` CASCADE, `website_id` -> `store_website.website_id` CASCADE
**Indexes:** `customer_group_id`, `website_id` (btree)

---

### 63. catalog_product_flat_1
**NOT DEFINED in db_schema.xml.** Flat catalog tables (`catalog_product_flat_{store_id}`) are dynamically generated by the Magento flat indexer at runtime. They are NOT declared in declarative schema. The table is a denormalized, single-table copy of product EAV data for one specific store view. Its columns are dynamic and depend on which product attributes have `used_in_product_listing = 1` or `used_for_sort_by = 1` in `catalog_eav_attribute`. Standard base columns include:

| Column | Type | Notes |
|--------|------|-------|
| entity_id | INT UNSIGNED | PK, from catalog_product_entity |
| attribute_set_id | SMALLINT UNSIGNED | From catalog_product_entity |
| type_id | VARCHAR(32) | From catalog_product_entity |
| sku | VARCHAR(64) | From catalog_product_entity |
| has_options | SMALLINT | From catalog_product_entity |
| required_options | SMALLINT UNSIGNED | From catalog_product_entity |
| created_at | TIMESTAMP | From catalog_product_entity |
| updated_at | TIMESTAMP | From catalog_product_entity |
| *+ all EAV attributes marked for flat* | *varies* | One column per attribute |

**NOTE:** Flat catalog is deprecated and not recommended. It causes performance and indexing issues at scale.

---

### 64. cataloginventory_stock
*(Defined in Magento/CatalogInventory module)*

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| stock_id | SMALLINT | YES | NO | YES | auto |
| website_id | SMALLINT | YES | NO | NO | - |
| stock_name | VARCHAR(255) | - | YES | NO | NULL |

**PK:** `stock_id`
**Indexes:** `website_id` (btree)

---

### 65. cataloginventory_stock_item

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| item_id | INT | YES | NO | YES | auto |
| product_id | INT | YES | NO | NO | 0 |
| stock_id | SMALLINT | YES | NO | NO | 0 |
| qty | DECIMAL(12,4) | NO | YES | NO | NULL |
| min_qty | DECIMAL(12,4) | NO | NO | NO | 0 |
| use_config_min_qty | SMALLINT | YES | NO | NO | 1 |
| is_qty_decimal | SMALLINT | YES | NO | NO | 0 |
| backorders | SMALLINT | YES | NO | NO | 0 |
| use_config_backorders | SMALLINT | YES | NO | NO | 1 |
| min_sale_qty | DECIMAL(12,4) | NO | NO | NO | 1 |
| use_config_min_sale_qty | SMALLINT | YES | NO | NO | 1 |
| max_sale_qty | DECIMAL(12,4) | NO | NO | NO | 0 |
| use_config_max_sale_qty | SMALLINT | YES | NO | NO | 1 |
| is_in_stock | SMALLINT | YES | NO | NO | 0 |
| low_stock_date | TIMESTAMP | - | YES | NO | NULL |
| notify_stock_qty | DECIMAL(12,4) | NO | YES | NO | NULL |
| use_config_notify_stock_qty | SMALLINT | YES | NO | NO | 1 |
| manage_stock | SMALLINT | YES | NO | NO | 0 |
| use_config_manage_stock | SMALLINT | YES | NO | NO | 1 |
| stock_status_changed_auto | SMALLINT | YES | NO | NO | 0 |
| use_config_qty_increments | SMALLINT | YES | NO | NO | 1 |
| qty_increments | DECIMAL(12,4) | NO | NO | NO | 0 |
| use_config_enable_qty_inc | SMALLINT | YES | NO | NO | 1 |
| enable_qty_increments | SMALLINT | YES | NO | NO | 0 |
| is_decimal_divided | SMALLINT | YES | NO | NO | 0 |
| website_id | SMALLINT | YES | NO | NO | 0 |

**PK:** `item_id`
**UNIQUE:** (`product_id`, `stock_id`)
**FK:** `product_id` -> `catalog_product_entity.entity_id` CASCADE, `stock_id` -> `cataloginventory_stock.stock_id` CASCADE
**Indexes:** `website_id`, (`website_id`, `product_id`), `stock_id` (btree)

---

### 66. cataloginventory_stock_status

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| product_id | INT | YES | NO | NO | - |
| website_id | SMALLINT | YES | NO | NO | - |
| stock_id | SMALLINT | YES | NO | NO | - |
| qty | DECIMAL(12,4) | NO | NO | NO | 0 |
| stock_status | SMALLINT | YES | NO | NO | - |

**PK:** (`product_id`, `website_id`, `stock_id`)
**Indexes:** `stock_id`, `website_id`, `stock_status` (btree)

---

## SECTION 11: ADDITIONAL TABLES FROM CATALOG MODULE

These tables are also defined in `Magento/Catalog/etc/db_schema.xml` and are relevant to the catalog ecosystem:

### 67. catalog_product_relation

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| parent_id | INT | YES | NO | NO | - |
| child_id | INT | YES | NO | NO | - |

**PK:** (`parent_id`, `child_id`)
**FK:** `parent_id` -> `catalog_product_entity.entity_id` CASCADE, `child_id` -> `catalog_product_entity.entity_id` CASCADE
**Indexes:** `child_id` (btree)

---

### 68. catalog_compare_item

| Column | Type | Unsigned | Nullable | Identity | Default |
|--------|------|----------|----------|----------|---------|
| catalog_compare_item_id | INT | YES | NO | YES | auto |
| visitor_id | INT | YES | NO | NO | 0 |
| customer_id | INT | YES | YES | NO | NULL |
| product_id | INT | YES | NO | NO | 0 |
| store_id | SMALLINT | YES | YES | NO | NULL |
| list_id | INT | YES | YES | NO | NULL |

**PK:** `catalog_compare_item_id`
**FK:** `product_id` -> `catalog_product_entity.entity_id` CASCADE, `customer_id` -> `customer_entity.entity_id` CASCADE, `store_id` -> `store.store_id` SET NULL, `list_id` -> `catalog_compare_list.list_id` CASCADE
**Indexes:** `product_id`, `visitor_id`+`product_id`, `customer_id`+`product_id`, `store_id`, `list_id` (btree)

---

## TABLES THAT DO NOT EXIST IN MAGENTO 2

| Requested Table | Status |
|-----------------|--------|
| `catalog_product_super_attribute_pricing` | **Removed in M2.** Existed in Magento 1. Configurable product pricing changed -- each child product has its own price. |
| `catalog_product_enabled_index` | **Does not exist in M2 declarative schema.** Product enabled/disabled status is indexed via `catalog_product_index_eav`. |
| `catalog_product_flat_1` | **Dynamically generated by indexer**, not declared in db_schema.xml. Columns are dynamic based on attribute configuration. Deprecated. |

---

## SUMMARY

| Section | Tables Documented | Source Module |
|---------|-------------------|---------------|
| Core Product EAV | 7 | Magento/Catalog |
| EAV Metadata | 9 (8 from Eav + catalog_eav_attribute) | Magento/Eav, Magento/Catalog |
| Media | 5 | Magento/Catalog, Magento/ProductVideo |
| Pricing | 5 | Magento/Catalog, Magento/CatalogRule |
| Product Relationships | 6 | Magento/Catalog |
| Custom Options | 6 | Magento/Catalog |
| Configurable/Variants | 3 (1 does not exist in M2) | Magento/ConfigurableProduct |
| Bundles | 6 | Magento/Bundle |
| Categories | 7 | Magento/Catalog |
| Indexing/Inventory | 9 (2 not in declarative schema) | Magento/Catalog, Magento/CatalogInventory |
| Additional | 2 | Magento/Catalog |
| **TOTAL** | **65 defined + 3 noted as non-existent/dynamic** | |

Sources:
- [Magento 2 Catalog db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/Catalog/etc/db_schema.xml)
- [Magento 2 Eav db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/Eav/etc/db_schema.xml)
- [Magento 2 CatalogRule db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/CatalogRule/etc/db_schema.xml)
- [Magento 2 Bundle db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/Bundle/etc/db_schema.xml)
- [Magento 2 ConfigurableProduct db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/ConfigurableProduct/etc/db_schema.xml)
- [Magento 2 CatalogInventory db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/CatalogInventory/etc/db_schema.xml)
- [Magento 2 ProductVideo db_schema.xml (2.4-develop)](https://github.com/magento/magento2/blob/2.4-develop/app/code/Magento/ProductVideo/etc/db_schema.xml)
- [Magento Catalog UpgradeSchema.php (media_gallery_value_to_entity)](https://github.com/pepe1518/magento2/blob/master/vendor/magento/module-catalog/Setup/UpgradeSchema.php)
- [Adobe Commerce Declarative Schema Documentation](https://developer.adobe.com/commerce/php/development/components/declarative-schema/configuration)
- [Mage-OS db_schema.xml Reference](https://devdocs.mage-os.org/docs/main/db_schema_xml)
- [BelVG EAV Data Storage Guide](https://belvg.com/blog/how-eav-data-storage-works-in-magento-2.html)
- [BSS Commerce EAV Model Guide](https://bsscommerce.com/magento/blog/magento-2-eav-model/)
- [Adobe Commerce Flat Catalog Documentation](https://experienceleague.adobe.com/en/docs/commerce-admin/catalog/catalog/catalog-flat)
