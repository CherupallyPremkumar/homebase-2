package com.homebase.ecom.shipping.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

public class Shipping extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    private String orderId;
    private String carrier;
    private String trackingNumber;
    private String trackingUrl;
    private Date shippedAt;
    private Date estimatedDelivery;
    private String shippingAddress;
    private Date deliveredAt;
    public String description;
    private String deliveryProof;
    private String returnReason;
    private String returnTrackingNumber;
    private String currentLocation;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getTrackingUrl() { return trackingUrl; }
    public void setTrackingUrl(String trackingUrl) { this.trackingUrl = trackingUrl; }

    public Date getShippedAt() { return shippedAt; }
    public void setShippedAt(Date shippedAt) { this.shippedAt = shippedAt; }

    public Date getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(Date estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public Date getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Date deliveredAt) { this.deliveredAt = deliveredAt; }

    public String getDeliveryProof() { return deliveryProof; }
    public void setDeliveryProof(String deliveryProof) { this.deliveryProof = deliveryProof; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public String getReturnTrackingNumber() { return returnTrackingNumber; }
    public void setReturnTrackingNumber(String returnTrackingNumber) { this.returnTrackingNumber = returnTrackingNumber; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public TransientMap getTransientMap() { return this.transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ShippingActivityLog activityLog = new ShippingActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
