package com.homebase.ecom.settlement.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "settlement_lines", indexes = {
        @Index(name = "idx_settlement_lines_order", columnList = "order_id")
})
public class SettlementLineItemEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id", nullable = false)
    private SettlementEntity settlement;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "order_item_id", nullable = false)
    private String orderItemId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "item_sales_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money itemSalesAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "item_commission_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money itemCommissionAmount;

    // --- Getters & Setters ---

    public SettlementEntity getSettlement() {
        return settlement;
    }

    public void setSettlement(SettlementEntity settlement) {
        this.settlement = settlement;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Money getItemSalesAmount() {
        return itemSalesAmount;
    }

    public void setItemSalesAmount(Money itemSalesAmount) {
        this.itemSalesAmount = itemSalesAmount;
    }

    public Money getItemCommissionAmount() {
        return itemCommissionAmount;
    }

    public void setItemCommissionAmount(Money itemCommissionAmount) {
        this.itemCommissionAmount = itemCommissionAmount;
    }
}
