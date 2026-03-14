package com.homebase.ecom.shipping.infrastructure.persistence.entity;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "shipments")
public class ShippingEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String carrier;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "tracking_url")
    private String trackingUrl;

    @Column(name = "shipped_at")
    private Date shippedAt;

    @Column(name = "estimated_delivery")
    private Date estimatedDelivery;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "shipping_address", columnDefinition = "jsonb")
    private String shippingAddress;

    @Column(name = "delivered_at")
    private Date deliveredAt;

    private String description;

    @Column(name = "delivery_proof")
    private String deliveryProof;

    @Column(name = "return_reason")
    private String returnReason;

    @Column(name = "return_tracking_number")
    private String returnTrackingNumber;

    @Column(name = "current_location")
    private String currentLocation;

    @Transient
    public TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "shipping_id")
    public List<ShippingActivityLogEntity> activities = new ArrayList<>();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public Date getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(Date shippedAt) {
        this.shippedAt = shippedAt;
    }

    public Date getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(Date estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliveryProof() { return deliveryProof; }
    public void setDeliveryProof(String deliveryProof) { this.deliveryProof = deliveryProof; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public String getReturnTrackingNumber() { return returnTrackingNumber; }
    public void setReturnTrackingNumber(String returnTrackingNumber) { this.returnTrackingNumber = returnTrackingNumber; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    public List<ShippingActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<ShippingActivityLogEntity> activities) {
        this.activities = activities;
    }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        for (ActivityLog a : activities) {
            acts.add(a);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ShippingActivityLogEntity activityLog = new ShippingActivityLogEntity();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
