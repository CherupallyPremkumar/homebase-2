package com.homebase.ecom.payment.infrastructure.persistence.entity;

import com.homebase.ecom.payment.infrastructure.enums.RefundRequestStatus;
import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import java.util.*;
import java.util.Locale;

@Entity
@Table(name = "refund_requests")
public class RefundRequest extends AbstractJpaStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money amount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType = "stripe";

    @Column(name = "gateway_refund_id")
    private String gatewayRefundId;

    @Transient
    public TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "refund_request_id")
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    @Transient
    public RefundRequestStatus getStatusEnum() {
        String s = getStatus();
        return s != null ? RefundRequestStatus.valueOf(s.toUpperCase(Locale.ROOT)) : null;
    }

    public void setStatus(RefundRequestStatus status) {
        setStatus(status != null ? status.name() : null);
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
}
