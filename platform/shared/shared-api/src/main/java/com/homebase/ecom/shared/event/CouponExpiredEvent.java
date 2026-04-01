package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published by Promo service when a coupon expires.
 * Cart-jobs translates this into cart STM events for affected carts.
 */
public class CouponExpiredEvent implements Serializable {

    public static final String EVENT_TYPE = "COUPON_EXPIRED";

    private String couponCode;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public CouponExpiredEvent() {}

    public CouponExpiredEvent(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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
}
