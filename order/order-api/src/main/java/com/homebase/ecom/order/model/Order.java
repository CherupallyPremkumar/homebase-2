package com.homebase.ecom.order.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import com.homebase.ecom.order.model.OrderActivityLog;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import java.time.LocalDateTime;
import com.homebase.ecom.shared.model.Money;

@Entity
@Table(name = "orders")
public class Order extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {
    @Column(name = "user_id", nullable = false)
    private String user_Id;

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
    private List<OrderItem> items = new ArrayList<>();

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "webhook_processed_at")
    private LocalDateTime webhookProcessedAt;

    @Column(length = 2000)
    public String description;

    @Transient
    public TransientMap transientMap = new TransientMap();

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getGatewaySessionId() {
        return gatewaySessionId;
    }

    public void setGatewaySessionId(String gatewaySessionId) {
        this.gatewaySessionId = gatewaySessionId;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Money taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Money getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Money shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public java.time.LocalDateTime getWebhookProcessedAt() {
        return webhookProcessedAt;
    }

    public void setWebhookProcessedAt(java.time.LocalDateTime webhookProcessedAt) {
        this.webhookProcessedAt = webhookProcessedAt;
    }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    public List<OrderActivityLog> activities = new ArrayList<>();

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
        OrderActivityLog activityLog = new OrderActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
