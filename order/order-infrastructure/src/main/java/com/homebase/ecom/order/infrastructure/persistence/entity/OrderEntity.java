package com.homebase.ecom.order.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JPA entity for Order -- maps to the 'orders' table.
 * All columns aligned with db-migrations/order/db.changelog-order.xml.
 */
@Entity
@Table(name = "orders")
public class OrderEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "shipping_amount", precision = 12, scale = 2)
    private BigDecimal shippingAmount;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency = "INR";

    @Column(name = "shipping_address_id")
    private String shippingAddressId;

    @Column(name = "billing_address_id")
    private String billingAddressId;

    @Column(name = "payment_method_id")
    private String paymentMethodId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "item_count")
    private int itemCount;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    // --- Cross-BC reference fields (order-004 changeset) ---

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "checkout_id")
    private String checkoutId;

    @Column(name = "invoice_number", length = 100)
    private String invoiceNumber;

    @Column(name = "invoice_url", length = 500)
    private String invoiceUrl;

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "carrier", length = 100)
    private String carrier;

    @Column(name = "coupon_codes", length = 500)
    private String couponCodes;

    @Transient
    private TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderActivityLogEntity> activities = new ArrayList<>();

    // --- Getters and Setters ---

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<OrderItemEntity> getItems() { return items; }
    public void setItems(List<OrderItemEntity> items) { this.items = items; }

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

    public List<OrderActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<OrderActivityLogEntity> activities) { this.activities = activities; }

    // --- Workflow Support ---

    @Override
    public TransientMap getTransientMap() { return this.transientMap; }

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
        OrderActivityLogEntity activityLog = new OrderActivityLogEntity();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
