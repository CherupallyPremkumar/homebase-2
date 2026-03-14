package com.homebase.ecom.order.model;

import com.homebase.ecom.shared.Money;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import java.time.LocalDateTime;

public class Order extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    private String user_Id;
    private String gatewaySessionId;
    private String gatewayTransactionId;
    private OrderStatus status;
    private Money totalAmount;
    private Money taxAmount;
    private Money shippingAmount;
    private String idempotencyKey;
    private List<OrderItem> items = new ArrayList<>();
    private String metadata;
    private LocalDateTime webhookProcessedAt;
    private String shippingAddress;
    private String billingAddress;
    private String appliedPromoCode;
    private Double discountAmount;
    private String cartId;
    private Integer retryCount = 0;
    private String previousFailedOrderId;
    private LocalDateTime deliveryDate;
    public String description;
    private Date slaYellowDate;
    private Date slaRedDate;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getPreviousFailedOrderId() { return previousFailedOrderId; }
    public void setPreviousFailedOrderId(String previousFailedOrderId) { this.previousFailedOrderId = previousFailedOrderId; }

    public String getUser_Id() { return user_Id; }
    public void setUser_Id(String user_Id) { this.user_Id = user_Id; }

    public String getGatewaySessionId() { return gatewaySessionId; }
    public void setGatewaySessionId(String gatewaySessionId) { this.gatewaySessionId = gatewaySessionId; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Money getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Money totalAmount) { this.totalAmount = totalAmount; }

    public Money getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Money taxAmount) { this.taxAmount = taxAmount; }

    public Money getShippingAmount() { return shippingAmount; }
    public void setShippingAmount(Money shippingAmount) { this.shippingAmount = shippingAmount; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public LocalDateTime getWebhookProcessedAt() { return webhookProcessedAt; }
    public void setWebhookProcessedAt(LocalDateTime webhookProcessedAt) { this.webhookProcessedAt = webhookProcessedAt; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getAppliedPromoCode() { return appliedPromoCode; }
    public void setAppliedPromoCode(String appliedPromoCode) { this.appliedPromoCode = appliedPromoCode; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public Date getSlaYellowDate() { return slaYellowDate; }
    public void setSlaYellowDate(Date slaYellowDate) { this.slaYellowDate = slaYellowDate; }

    public Date getSlaRedDate() { return slaRedDate; }
    public void setSlaRedDate(Date slaRedDate) { this.slaRedDate = slaRedDate; }

    public void cancelItem(String orderItemId) {
        OrderItem item = findItem(orderItemId);
        item.requestCancellation();
    }

    public void refundItem(String orderItemId) {
        OrderItem item = findItem(orderItemId);
        item.requestRefund();
    }

    private OrderItem findItem(String orderItemId) {
        return items.stream()
                .filter(i -> i.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + orderItemId));
    }

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
