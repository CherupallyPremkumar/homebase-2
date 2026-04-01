package com.homebase.ecom.payment.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

@Entity
@Table(name = "payouts", indexes = {
        @Index(name = "idx_payouts_gateway_payout_at", columnList = "gateway_type,payout_at")
})
public class Payout extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType;

    @Column(name = "provider_payout_id", nullable = false, length = 255)
    private String providerPayoutId;

    @Column(name = "payout_at")
    private LocalDateTime payoutAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "gross_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money grossAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "fee_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money feeAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "net_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money netAmount;

    @Transient
    public TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "payout_id")
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

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getProviderPayoutId() {
        return providerPayoutId;
    }

    public void setProviderPayoutId(String providerPayoutId) {
        this.providerPayoutId = providerPayoutId;
    }

    public LocalDateTime getPayoutAt() {
        return payoutAt;
    }

    public void setPayoutAt(LocalDateTime payoutAt) {
        this.payoutAt = payoutAt;
    }

    public Money getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Money grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Money getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Money feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Money getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Money netAmount) {
        this.netAmount = netAmount;
    }
}
