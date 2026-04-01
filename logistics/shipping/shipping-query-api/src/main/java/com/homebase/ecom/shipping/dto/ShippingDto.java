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
    private String customerId;
    private String carrier;
    private String trackingNumber;
    private String shippingMethod;
    private String stateId;
    private int deliveryAttempts;
    private Date estimatedDeliveryDate;
    private Date actualDeliveryDate;
    private String currentLocation;
    private String fromAddress;
    private String toAddress;
    private String weight;
    private String dimensions;
    private String deliveryInstructions;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public int getDeliveryAttempts() { return deliveryAttempts; }
    public void setDeliveryAttempts(int deliveryAttempts) { this.deliveryAttempts = deliveryAttempts; }

    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }

    public Date getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(Date actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }

    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
