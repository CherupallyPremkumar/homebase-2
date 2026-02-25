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
