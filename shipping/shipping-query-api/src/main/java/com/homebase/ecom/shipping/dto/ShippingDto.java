package com.homebase.ecom.shipping.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for Shipping query responses.
 */
public class ShippingDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private String carrier;
    private String trackingNumber;
    private String status;
    private String stateId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
