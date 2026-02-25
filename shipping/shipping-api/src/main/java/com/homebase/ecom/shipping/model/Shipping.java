package com.homebase.ecom.shipping.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import com.homebase.ecom.shipping.model.ShippingActivityLog;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

@Entity
@Table(name = "shipments")
public class Shipping extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {
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

    public String description;
    @Transient
    public TransientMap transientMap = new TransientMap();

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

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "shipping_id")
    public List<ShippingActivityLog> activities = new ArrayList<>();

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
        ShippingActivityLog activityLog = new ShippingActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
