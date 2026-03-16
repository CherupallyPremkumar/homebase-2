package com.homebase.ecom.shared.event;

import com.homebase.ecom.shared.Address;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Kafka event DTO published when a cart checkout is initiated.
 * Used for early inventory reservation and early order creation.
 */
public class CartCheckoutInitiatedEvent implements Serializable {

    public static final String EVENT_TYPE = "CART_CHECKOUT_INITIATED";

    private String cartId;
    private String userId;
    private long totalAmount;
    private String currency;
    private List<CartItemPayload> items;
    private Address shippingAddress;
    private Address billingAddress;
    private String promoCode;
    private long discountAmount;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public CartCheckoutInitiatedEvent() {
    }

    public CartCheckoutInitiatedEvent(String cartId, String userId, List<CartItemPayload> items,
            LocalDateTime timestamp) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.timestamp = timestamp;
    }

    // --- Getters & Setters ---

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<CartItemPayload> getItems() {
        return items;
    }

    public void setItems(List<CartItemPayload> items) {
        this.items = items;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Lightweight item payload for Kafka event.
     */
    public static class CartItemPayload implements Serializable {
        private String productId;
        private Integer quantity;
        private long unitPrice;
        private String productName;
        private String supplierId;

        public CartItemPayload() {
        }

        public CartItemPayload(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }



        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        public long getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(long unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
