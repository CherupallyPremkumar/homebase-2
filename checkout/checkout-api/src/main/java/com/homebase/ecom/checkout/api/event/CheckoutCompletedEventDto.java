package com.homebase.ecom.checkout.api.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Published contract: event emitted when checkout completes.
 * Consumers: cart (completeCheckout), order (create), analytics.
 */
public class CheckoutCompletedEventDto implements Serializable {

    public static final String EVENT_TYPE = "CHECKOUT_COMPLETED";

    private String eventType;
    private String checkoutId;
    private String cartId;
    private String orderId;
    private String customerId;
    private String paymentId;
    private long totalAmount;
    private String currency;
    private LocalDateTime timestamp;

    public CheckoutCompletedEventDto() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getCheckoutId() { return checkoutId; }
    public void setCheckoutId(String checkoutId) { this.checkoutId = checkoutId; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(long totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
