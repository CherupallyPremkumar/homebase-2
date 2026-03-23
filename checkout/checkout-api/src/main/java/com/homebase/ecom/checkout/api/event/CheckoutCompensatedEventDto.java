package com.homebase.ecom.checkout.api.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Published contract: event emitted when checkout is compensated (rolled back).
 * Consumers: cart (cancelCheckout — restores cart to ACTIVE).
 */
public class CheckoutCompensatedEventDto implements Serializable {

    public static final String EVENT_TYPE = "CHECKOUT_COMPENSATED";

    private String eventType;
    private String checkoutId;
    private String cartId;
    private String reason;
    private LocalDateTime timestamp;

    public CheckoutCompensatedEventDto() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getCheckoutId() { return checkoutId; }
    public void setCheckoutId(String checkoutId) { this.checkoutId = checkoutId; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
