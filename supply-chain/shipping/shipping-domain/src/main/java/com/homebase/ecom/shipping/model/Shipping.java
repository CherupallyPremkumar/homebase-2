package com.homebase.ecom.shipping.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.math.BigDecimal;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

/**
 * Shipping bounded context domain model.
 * Tracks shipment lifecycle from label creation through delivery or return.
 * Maps to the 'shipments' table (DB source of truth).
 */
public class Shipping extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    // Core identifiers
    private String orderId;
    private String customerId;
    private String trackingNumber;
    private String carrier;

    // Shipping method
    private String shippingMethod; // STANDARD, EXPRESS, OVERNIGHT

    // Addresses (stored as TEXT in DB)
    private String fromAddress;
    private String toAddress;

    // Package details (legacy columns from migration-001)
    private String weight;
    private String dimensions;

    // Delivery tracking
    private Date estimatedDeliveryDate;
    private Date actualDeliveryDate;
    private int deliveryAttempts;
    private String deliveryInstructions;
    private String currentLocation;

    // Description
    private String description;
    private String tenant;

    // ── Fields from migration-003 (Amazon-standard schema) ──

    private BigDecimal shippingCost;
    private String labelUrl;
    private String returnLabelUrl;
    private String podImageUrl;
    private String carrierTrackingUrl;
    private Integer packageWeightGrams;
    private String packageDimensionsJson;
    private BigDecimal insuranceAmount;
    private boolean signatureRequired;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

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

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

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

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

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
