package com.homebase.ecom.fulfillment.dto;

import java.io.Serializable;

/**
 * Response DTO returned after the fulfillment flow completes.
 */
public class FulfillmentResponse implements Serializable {

    private String orderId;
    private String shipmentId;
    private String trackingNumber;
    private String status;

    public FulfillmentResponse() {
    }

    public FulfillmentResponse(String orderId, String shipmentId, String trackingNumber, String status) {
        this.orderId = orderId;
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
