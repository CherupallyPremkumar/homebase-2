package com.homebase.ecom.payment.domain;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import java.util.*;

@Entity
@Table(name = "refunds", indexes = {
        @Index(name = "idx_refunds_gateway_refund_id", columnList = "gateway_refund_id"),
        @Index(name = "idx_refunds_charge_id", columnList = "payment_transaction_id")
})
public class Refund extends AbstractJpaStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "payment_transaction_id", nullable = false)
    private String paymentTransactionId;

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType = "stripe";

    @Column(name = "gateway_refund_id", unique = true, nullable = false)
    private String gatewayRefundId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money amount;

    @Column(name = "reason")
    private String reason;

    @Transient
    public TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "refund_id")
    public List<PaymentActivityLog> activities = new ArrayList<>();

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        PaymentActivityLog activityLog = new PaymentActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }

    @Override
    public TransientMap getTransientMap() {
        return transientMap;
    }

    // --- Getters & Setters ---

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getGatewayRefundId() {
        return gatewayRefundId;
    }

    public void setGatewayRefundId(String gatewayRefundId) {
        this.gatewayRefundId = gatewayRefundId;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return getCurrentState() != null ? getCurrentState().getStateId() : null;
    }

    public void setStatus(String status) {
        if (getCurrentState() == null) {
            setCurrentState(new org.chenile.stm.State());
        }
        getCurrentState().setStateId(status);
    }
}
