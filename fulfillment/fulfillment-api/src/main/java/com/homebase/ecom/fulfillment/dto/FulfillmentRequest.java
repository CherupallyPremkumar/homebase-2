package com.homebase.ecom.fulfillment.dto;

import java.io.Serializable;

/**
 * Request DTO to initiate the fulfillment flow for a paid order.
 */
public class FulfillmentRequest implements Serializable {

    private String orderId;
    private String userId;

    public FulfillmentRequest() {
    }

    public FulfillmentRequest(String orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
