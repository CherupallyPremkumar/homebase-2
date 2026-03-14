package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.homebase.ecom.shared.Address;

/**
 * Kafka event DTO published when a cart checkout is completed.
 */
public class CartCheckoutCompletedEvent implements Serializable {

    public static final String EVENT_TYPE = "CART_CHECKOUT_COMPLETED";

    private String cartId;
    private String userId;
    private BigDecimal totalAmount;
    private String currency;
    private List<CartItemPayload> items;
    private String paymentId;
    private String paymentGateway;
    private String paymentTransactionId;
    private Address shippingAddress;
    private Address billingAddress;
    private String promoCode;
    private BigDecimal discountAmount;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public CartCheckoutCompletedEvent() {
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
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
        private String sku;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private String supplierId;

        public CartItemPayload() {
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }
    }
}
