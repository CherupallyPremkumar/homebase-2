package com.homebase.ecom.order.infrastructure.persistence.entity;

import com.homebase.ecom.order.model.OrderStatus;
import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "gateway_session_id", unique = true)
    private String gatewaySessionId;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money totalAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "tax_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money taxAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "shipping_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money shippingAmount;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "webhook_processed_at")
    private LocalDateTime webhookProcessedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "shipping_address")
    private String shippingAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "billing_address")
    private String billingAddress;

    @Column(name = "applied_promo_code")
    private String appliedPromoCode;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "previous_failed_order_id")
    private String previousFailedOrderId;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(length = 2000)
    private String description;

    @Transient
    private TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderActivityLogEntity> activities = new ArrayList<>();

    // --- Getters and Setters ---

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

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

    public List<OrderItemEntity> getItems() { return items; }
    public void setItems(List<OrderItemEntity> items) { this.items = items; }

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

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getPreviousFailedOrderId() { return previousFailedOrderId; }
    public void setPreviousFailedOrderId(String previousFailedOrderId) { this.previousFailedOrderId = previousFailedOrderId; }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

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
