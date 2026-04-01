package com.homebase.ecom.shipping.infrastructure.persistence.entity;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(nullable = false)
    private String carrier;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "from_address", columnDefinition = "TEXT")
    private String fromAddress;

    @Column(name = "to_address", columnDefinition = "TEXT")
    private String toAddress;

    @Column
    private String weight;

    @Column
    private String dimensions;

    @Column(name = "estimated_delivery_date")
    private Date estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private Date actualDeliveryDate;

    @Column(name = "delivery_attempts")
    private int deliveryAttempts;

    @Column(name = "delivery_instructions")
    private String deliveryInstructions;

    @Column(name = "current_location")
    private String currentLocation;

    @Column
    private String description;

    // ── Fields from migration-003 (Amazon-standard schema) ──

    @Column(name = "shipping_cost", precision = 12, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "label_url", length = 500)
    private String labelUrl;

    @Column(name = "return_label_url", length = 500)
    private String returnLabelUrl;

    @Column(name = "pod_image_url", length = 500)
    private String podImageUrl;

    @Column(name = "carrier_tracking_url", length = 500)
    private String carrierTrackingUrl;

    @Column(name = "package_weight_grams")
    private Integer packageWeightGrams;

    @Column(name = "package_dimensions_json", columnDefinition = "TEXT")
    private String packageDimensionsJson;

    @Column(name = "insurance_amount", precision = 12, scale = 2)
    private BigDecimal insuranceAmount;

    @Column(name = "signature_required")
    private boolean signatureRequired;

    @Transient
    private TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "shipping_id")
    private List<ShippingActivityLogEntity> activities = new ArrayList<>();

    // ── Getters and Setters ──

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }

    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }

    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }

    public Date getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(Date actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }

    public int getDeliveryAttempts() { return deliveryAttempts; }
    public void setDeliveryAttempts(int deliveryAttempts) { this.deliveryAttempts = deliveryAttempts; }

    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }

    public String getLabelUrl() { return labelUrl; }
    public void setLabelUrl(String labelUrl) { this.labelUrl = labelUrl; }

    public String getReturnLabelUrl() { return returnLabelUrl; }
    public void setReturnLabelUrl(String returnLabelUrl) { this.returnLabelUrl = returnLabelUrl; }

    public String getPodImageUrl() { return podImageUrl; }
    public void setPodImageUrl(String podImageUrl) { this.podImageUrl = podImageUrl; }

    public String getCarrierTrackingUrl() { return carrierTrackingUrl; }
    public void setCarrierTrackingUrl(String carrierTrackingUrl) { this.carrierTrackingUrl = carrierTrackingUrl; }

    public Integer getPackageWeightGrams() { return packageWeightGrams; }
    public void setPackageWeightGrams(Integer packageWeightGrams) { this.packageWeightGrams = packageWeightGrams; }

    public String getPackageDimensionsJson() { return packageDimensionsJson; }
    public void setPackageDimensionsJson(String packageDimensionsJson) { this.packageDimensionsJson = packageDimensionsJson; }

    public BigDecimal getInsuranceAmount() { return insuranceAmount; }
    public void setInsuranceAmount(BigDecimal insuranceAmount) { this.insuranceAmount = insuranceAmount; }

    public boolean isSignatureRequired() { return signatureRequired; }
    public void setSignatureRequired(boolean signatureRequired) { this.signatureRequired = signatureRequired; }

    public TransientMap getTransientMap() { return this.transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    public List<ShippingActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<ShippingActivityLogEntity> activities) { this.activities = activities; }

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
        activityLog.setActivityName(eventId);
        activityLog.setActivityComment(comment);
        activityLog.setActivitySuccess(true);
        activities.add(activityLog);
        return activityLog;
    }
}
