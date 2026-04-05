package com.homebase.ecom.order.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order aggregate root -- domain model for the Order bounded context.
 *
 * Fields aligned with DB schema in db-migrations/order/db.changelog-order.xml:
 * - orders table: order_number, customer_id, subtotal, tax_amount, shipping_amount,
 *   total_amount, currency, shipping_address_id, billing_address_id, payment_method_id,
 *   notes, cancel_reason, description, item_count, discount_amount, shipping_address (JSON),
 *   payment_id, checkout_id, invoice_number, invoice_url, estimated_delivery_date,
 *   actual_delivery_date, tracking_number, carrier, coupon_codes
 *
 * Inherited from AbstractExtendedStateEntity: id, createdTime, lastModifiedTime,
 *   createdBy, lastModifiedBy, version, currentState (stateId/flowId)
 */
public class Order extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    // --- Core order fields ---
    private String orderNumber;
    private String customerId;
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private String currency = "INR";
    private String shippingAddressId;
    private String billingAddressId;
    private String paymentMethodId;
    private String notes;
    private String cancelReason;
    private String description;
    private String tenant;
    private int itemCount;
    private BigDecimal discountAmount;
    private String shippingAddress; // JSON text of shipping address

    // --- Cross-BC reference fields (order-004) ---
    private String paymentId;
    private String checkoutId;
    private String invoiceNumber;
    private String invoiceUrl;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String trackingNumber;
    private String carrier;
    private String couponCodes;

    // --- SLA fields (from AbstractJpaStateEntity) ---
    private Date slaYellowDate;
    private Date slaRedDate;

    /** Fraud risk score set by payment gateway, checked by CHECK_FRAUD auto-state */
    private Integer riskScore;

    /** Flag set by requestCancellation action for CHECK_CANCELLATION_WINDOW auto-state */
    private boolean cancellationAllowed;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // --- Getters & Setters ---

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getShippingAmount() { return shippingAmount; }
    public void setShippingAmount(BigDecimal shippingAmount) { this.shippingAmount = shippingAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(String shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public String getBillingAddressId() { return billingAddressId; }
    public void setBillingAddressId(String billingAddressId) { this.billingAddressId = billingAddressId; }

    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getCheckoutId() { return checkoutId; }
    public void setCheckoutId(String checkoutId) { this.checkoutId = checkoutId; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getInvoiceUrl() { return invoiceUrl; }
    public void setInvoiceUrl(String invoiceUrl) { this.invoiceUrl = invoiceUrl; }

    public LocalDateTime getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }

    public LocalDateTime getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(LocalDateTime actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getCouponCodes() { return couponCodes; }
    public void setCouponCodes(String couponCodes) { this.couponCodes = couponCodes; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public boolean isCancellationAllowed() { return cancellationAllowed; }
    public void setCancellationAllowed(boolean cancellationAllowed) { this.cancellationAllowed = cancellationAllowed; }

    public Date getSlaYellowDate() { return slaYellowDate; }
    public void setSlaYellowDate(Date slaYellowDate) { this.slaYellowDate = slaYellowDate; }

    public Date getSlaRedDate() { return slaRedDate; }
    public void setSlaRedDate(Date slaRedDate) { this.slaRedDate = slaRedDate; }

    public TransientMap getTransientMap() { return this.transientMap; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

    /** Direct getter for the activities list (used by mapper). */
    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        OrderActivityLog activityLog = new OrderActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
