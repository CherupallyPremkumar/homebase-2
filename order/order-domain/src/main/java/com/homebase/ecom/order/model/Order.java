package com.homebase.ecom.order.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order aggregate root — domain model for the Order bounded context.
 *
 * Fields per spec item #11:
 * - id, orderNumber, customerId, items, subtotal, taxAmount, shippingAmount,
 *   totalAmount, currency, shippingAddressId, billingAddressId, paymentMethodId,
 *   notes, cancelReason, stateId, flowId (from AbstractExtendedStateEntity)
 *
 * Removed: gateway fields, webhook fields, metadata, promo fields, retry fields,
 *          delivery tracking, SLA fields — those belong in Payment/Shipping/Fulfillment BCs.
 */
public class Order extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

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

    /** Flag set by requestCancellation action for CHECK_CANCELLATION_WINDOW auto-state */
    private boolean cancellationAllowed;

    public String description;
    private Date slaYellowDate;
    private Date slaRedDate;

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

    public boolean isCancellationAllowed() { return cancellationAllowed; }
    public void setCancellationAllowed(boolean cancellationAllowed) { this.cancellationAllowed = cancellationAllowed; }

    public Date getSlaYellowDate() { return slaYellowDate; }
    public void setSlaYellowDate(Date slaYellowDate) { this.slaYellowDate = slaYellowDate; }

    public Date getSlaRedDate() { return slaRedDate; }
    public void setSlaRedDate(Date slaRedDate) { this.slaRedDate = slaRedDate; }

    public TransientMap getTransientMap() { return this.transientMap; }

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
