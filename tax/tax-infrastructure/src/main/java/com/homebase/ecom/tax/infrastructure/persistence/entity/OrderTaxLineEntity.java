package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "order_tax_lines", indexes = {
        @Index(name = "idx_order_tax_lines_order", columnList = "order_id")
})
public class OrderTaxLineEntity extends BaseJpaEntity {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "order_item_id", nullable = false)
    private String orderItemId;

    @Column(name = "tax_rate_id", nullable = false)
    private String taxRateId;

    @Column(name = "tax_type", nullable = false)
    private String taxType;

    @Column(name = "taxable_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxableAmount;

    @Column(name = "tax_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "rate_applied", nullable = false, precision = 10, scale = 4)
    private BigDecimal rateApplied;

    // Getters and Setters

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

    public String getTaxRateId() {
        return taxRateId;
    }

    public void setTaxRateId(String taxRateId) {
        this.taxRateId = taxRateId;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getRateApplied() {
        return rateApplied;
    }

    public void setRateApplied(BigDecimal rateApplied) {
        this.rateApplied = rateApplied;
    }
}
