# State Machine Analysis Summary

This document provides a comprehensive overview of the Chenile state machines implemented across all modules in the HomeBase ecosystem.

## Core Modules

### 1. Order Module (`order-flow`)
Manages the lifecycle of a customer order from creation to completion or refund.
- **States**: `CREATED`, `PAYMENT_CONFIRMED`, `PROCESSING`, `PICKED`, `SHIPPED`, `DELIVERED`, `COMPLETED`, `RETURN_INITIATED`, `REFUND_INITIATED`, `REFUNDED`, `CANCELLED`.
- **Key Transitions**: `processPayment`, `startProcessing`, `itemsPicked`, `courierPickup`, `deliverOrder`, `confirmDelivery`, `cancelOrder`, `initiateReturn`, `approveReturn`, `refundComplete`.
- **Roles**: `SYSTEM`, `CUSTOMER`, `WAREHOUSE`, `ADMIN`.

### 2. Cart Module (`cart-flow`)
Tracks the shopping cart lifecycle.
- **States**: `CREATED`, `ACTIVE`, `CHECKOUT_INITIATED`, `ORDER_CREATED`, `ABANDONED`, `EXPIRED`.
- **Key Transitions**: `addItem`, `removeItem`, `updateQuantity`, `initiateCheckout`, `completePayment`, `abandonCart`, `abandonCheckout`.

### 3. Inventory Module (`inventory-flow`)
Manages stock inspection, storage, and reservation.
- **States**: `STOCK_PENDING`, `STOCK_INSPECTION`, `STOCK_APPROVED`, `IN_WAREHOUSE`, `PARTIALLY_RESERVED`, `OUT_OF_STOCK`, `PARTIAL_DAMAGE`, `DAMAGED_AT_WAREHOUSE`, `RETURN_TO_SUPPLIER`, `RETURNED_TO_SUPPLIER`, `STOCK_REJECTED`, `DISCARDED`.
- **Key Transitions**: `inspectStock`, `approveStock`, `rejectStock`, `damageFound`, `allocateToWarehouse`, `reserveStock`, `sellAllStock`, `returnDamaged`, `repairDamaged`, `discardDamaged`, `restockArrive`.

### 4. Product Module (`product-flow`)
Handles product catalog management and review.
- **States**: `DRAFT`, `UNDER_REVIEW`, `PUBLISHED`, `OUT_OF_STOCK`, `DISABLED`, `DISCONTINUED`.
- **Key Transitions**: `submitForReview`, `approveProduct`, `rejectProduct`, `markOutOfStock`, `disableProduct`, `discontinueProduct`, `enableProduct`, `restockProduct`.

## Support Modules

### 5. Shipping Module (`shipping-flow`)
Tracks the physical movement of goods.
- **States**: `AWAITING_PICKUP`, `PICKED_UP`, `IN_TRANSIT`, `OUT_FOR_DELIVERY`, `DELIVERED`, `RETURN_REQUESTED`, `RETURNED`.
- **Key Transitions**: `courierAssigned`, `inTransit`, `outForDelivery`, `markDelivered`, `returnRequested`, `returnPickup`.

### 6. Supplier Module (`supplier-flow`)
Manages supplier account status.
- **States**: `ACTIVE`, `INACTIVE`, `SUSPENDED`, `BLACKLISTED`.
- **Key Transitions**: `suspendSupplier`, `pauseSupplier`, `blacklistSupplier`, `reactivateSupplier`.

### 7. SupplierProduct Module (`supplier-product-flow`)
Handles the onboarding and listing of products from suppliers.
- **States**: `PENDING_DELIVERY`, `QUALITY_CHECK`, `APPROVED`, `AT_WAREHOUSE`, `ACTIVE`, `OUT_OF_STOCK`, `DISCONTINUED`.
- **Key Transitions**: `productDelivered`, `cancelProduct`, `approveQuality`, `rejectQuality`, `publishProduct`, `listProduct`, `stockSoldOut`, `discontinueProduct`, `restockProduct`.

### 8. User Module (`user-flow`)
Manages user account lifecycle, including verification and supplier upgrades.
- **States**: `REGISTERED`, `EMAIL_VERIFICATION_PENDING`, `EMAIL_VERIFIED`, `PROFILE_INCOMPLETE`, `PROFILE_COMPLETE`, `ACTIVE`, `INACTIVE`, `SUSPENDED`, `BANNED`, `VERIFICATION_EXPIRED`, `CANCELLED`, `SUPPLIER_APPROVAL_PENDING`, `SUPPLIER_APPROVED`, `DELETED`.
- **Key Transitions**: `sendVerificationEmail`, `verifyEmail`, `completeProfile`, `activateAccount`, `deactivateAccount`, `suspendAccount`, `banAccount`, `upgradeToSupplier`, `approveSupplier`, `rejectSupplier`.

### 9. ReturnRequest Module (`return-request-flow`)
Manages returns and quality checks for returned items.
- **States**: `REQUESTED`, `UNDER_REVIEW`, `APPROVED`, `IN_TRANSIT_BACK`, `RECEIVED`, `REFUNDED`, `REJECTED`.
- **Key Transitions**: `inspectReturn`, `approveReturn`, `rejectReturn`, `pickupInitiated`, `itemReceived`, `processRefund`.

### 10. Settlement Module (`settlement-flow`)
Handles financial settlements with sellers.
- **States**: `PENDING`, `PROCESSING`, `READY_FOR_PAYMENT`, `SETTLED`, `MANUAL_REVIEW`, `FAILED`.
- **Key Transitions**: `monthEnd`, `calculateSettlement`, `transferFunds`, `holdForReview`, `manualApprove`, `rejectSettlement`, `retrySettlement`.
- **Conditions**: `hasValidBankAccount`.
  erDiagram
  %% ===========================
  %% USER AGGREGATE
  %% ===========================
  USER {
  string id PK
  string email UK
  string phone
  string name
  string address
  string city
  string pin_code
  string user_type
  string status
  datetime created_at
  }

  %% ===========================
  %% SUPPLIER AGGREGATE
  %% ===========================
  USER ||--o{ SUPPLIER : "can be supplier"
  SUPPLIER ||--o{ SUPPLIER_PRODUCT : "supplies"
  SUPPLIER ||--o{ SUPPLIER_SETTLEMENT : "receives payment"
  SUPPLIER ||--o{ RETURN_REQUEST : "handles"
  SUPPLIER {
  string id PK
  string user_id FK
  string name
  string gst_number UK
  string phone
  string email
  string bank_account_number
  string ifsc_code
  string bank_holder_name
  string shop_description
  string status
  float rating
  int total_products
  datetime onboarded_at
  datetime updated_at
  }

  %% ===========================
  %% PRODUCT AGGREGATE
  %% ===========================
  SUPPLIER_PRODUCT ||--o{ INVENTORY : "tracks warehouse stock"
  SUPPLIER_PRODUCT ||--o{ CART_ITEM : "added to cart"
  SUPPLIER_PRODUCT ||--o{ ORDER_ITEM : "ordered by customer"
  SUPPLIER_PRODUCT ||--o{ QUALITY_CHECK : "inspected before sale"
  SUPPLIER_PRODUCT {
  string id PK
  string supplier_id FK
  string product_name
  string description
  float price
  string saree_type
  string material
  string color
  string pattern
  string size
  string care_instructions
  string status
  datetime created_at
  datetime updated_at
  }

  %% ===========================
  %% INVENTORY AGGREGATE
  %% ===========================
  INVENTORY {
  string id PK
  string supplier_product_id FK
  int total_quantity
  int at_warehouse
  int reserved_for_orders
  int damaged_count
  int returned_count
  datetime received_date
  datetime last_stock_check
  }

  QUALITY_CHECK {
  string id PK
  string supplier_product_id FK
  string check_status
  string inspection_notes
  string checked_by
  datetime checked_at
  }

  %% ===========================
  %% CART AGGREGATE
  %% ===========================
  USER ||--o{ CART_AGGREGATE : "has"
  CART_AGGREGATE ||--o{ CART_ITEM : "contains"
  CART_AGGREGATE {
  string id PK
  string user_id FK
  string state
  datetime created_at
  datetime expires_at
  datetime updated_at
  }

  CART_ITEM {
  string id PK
  string cart_id FK
  string supplier_product_id FK
  int quantity
  float price_at_add
  datetime added_at
  }

  %% ===========================
  %% ORDER AGGREGATE
  %% ===========================
  USER ||--o{ ORDER_AGGREGATE : "places"
  ORDER_AGGREGATE ||--o{ ORDER_ITEM : "contains"
  ORDER_AGGREGATE ||--o{ PAYMENT_AGGREGATE : "has payment"
  ORDER_AGGREGATE ||--o{ SHIPPING_AGGREGATE : "has shipping"
  ORDER_AGGREGATE {
  string id PK
  string user_id FK
  string cart_id FK
  float total_amount
  float commission_amount
  float supplier_payout_total
  string state
  datetime created_at
  datetime updated_at
  }

  ORDER_ITEM {
  string id PK
  string order_id FK
  string supplier_product_id FK
  int quantity
  float price_per_unit
  float total_price
  float commission_per_item
  string status
  datetime created_at
  }

  %% ===========================
  %% PAYMENT AGGREGATE - WITH RAZORPAY
  %% ===========================
  PAYMENT_AGGREGATE ||--o{ RAZORPAY_TRANSACTION : "creates"
  PAYMENT_AGGREGATE ||--o{ SUPPLIER_SETTLEMENT : "initiates settlement"
  PAYMENT_AGGREGATE {
  string id PK
  string order_id FK
  string razorpay_order_id FK
  float amount
  float refund_amount
  string currency
  string status
  string payment_method
  string transaction_id
  string refund_transaction_id
  string refund_reason
  datetime created_at
  datetime updated_at
  }

  %% ===========================
  %% RAZORPAY TRANSACTION (NEW)
  %% ===========================
  RAZORPAY_TRANSACTION {
  string id PK
  string payment_id FK
  string razorpay_order_id UK
  string razorpay_payment_id UK
  string razorpay_signature
  float amount
  string currency
  string status
  string payment_method
  string card_id
  string vpa
  string email
  string phone
  string description
  string notes
  string receipt_id
  datetime created_at
  datetime updated_at
  }

  %% ===========================
  %% RAZORPAY WEBHOOK LOG
  %% ===========================
  RAZORPAY_WEBHOOK_LOG {
  string id PK
  string event_type
  string razorpay_order_id
  string razorpay_payment_id
  string webhook_payload
  string webhook_signature
  string status
  datetime received_at
  datetime processed_at
  }

  %% ===========================
  %% SHIPPING AGGREGATE
  %% ===========================
  SHIPPING_AGGREGATE {
  string id PK
  string order_id FK
  string courier_name
  string tracking_number
  string state
  string shipping_address
  string pin_code
  datetime shipped_at
  datetime delivered_at
  datetime updated_at
  }

  %% ===========================
  %% SETTLEMENT AGGREGATE
  %% ===========================
  SUPPLIER_SETTLEMENT ||--o{ SETTLEMENT_ITEM : "contains"
  SUPPLIER_SETTLEMENT {
  string id PK
  string supplier_id FK
  int settlement_month
  int settlement_year
  float total_sales_amount
  float total_commission_amount
  float payable_amount
  int total_orders
  int returned_orders
  string status
  datetime settlement_date
  datetime paid_date
  datetime created_at
  }

  SETTLEMENT_ITEM {
  string id PK
  string settlement_id FK
  string order_id FK
  float order_amount
  float commission_amount
  float payout_amount
  }

  %% ===========================
  %% RETURN AGGREGATE
  %% ===========================
  ORDER_AGGREGATE ||--o{ RETURN_REQUEST : "initiates"
  RETURN_REQUEST {
  string id PK
  string order_id FK
  string order_item_id FK
  string supplier_product_id FK
  string supplier_id FK
  string reason
  string description
  string status
  datetime requested_at
  datetime resolved_at
  }

  %% ===========================
  %% MARKETPLACE CONFIGURATION
  %% ===========================
  MARKETPLACE_CONFIG {
  string id PK
  float commission_percentage
  float platform_fee
  string currency
  string payment_gateway
  string razorpay_key_id
  string razorpay_key_secret
  int settlement_cycle_days
  int return_window_days
  string warehouse_location
  string support_email
  datetime created_at
  datetime updated_at
  }