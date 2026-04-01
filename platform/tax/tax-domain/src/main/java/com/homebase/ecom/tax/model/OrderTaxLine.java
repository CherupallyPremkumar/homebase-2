package com.homebase.ecom.tax.model;

import org.chenile.utils.entity.model.BaseEntity;
import java.math.BigDecimal;

/**
 * Historical tax record per order line item.
 * Stored at order creation time — immutable snapshot of tax applied.
 */
public class OrderTaxLine extends BaseEntity {

    private String orderId;
    private String variantId;
    private String hsnCode;
    private String taxType;              // INTRA_STATE, INTER_STATE
    private BigDecimal taxableAmount;
    private BigDecimal gstRate;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal igstAmount;
    private BigDecimal cessAmount;
    private BigDecimal tcsAmount;
    private BigDecimal totalTax;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public BigDecimal getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(BigDecimal taxableAmount) { this.taxableAmount = taxableAmount; }
    public BigDecimal getGstRate() { return gstRate; }
    public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }
    public BigDecimal getCgstAmount() { return cgstAmount; }
    public void setCgstAmount(BigDecimal cgstAmount) { this.cgstAmount = cgstAmount; }
    public BigDecimal getSgstAmount() { return sgstAmount; }
    public void setSgstAmount(BigDecimal sgstAmount) { this.sgstAmount = sgstAmount; }
    public BigDecimal getIgstAmount() { return igstAmount; }
    public void setIgstAmount(BigDecimal igstAmount) { this.igstAmount = igstAmount; }
    public BigDecimal getCessAmount() { return cessAmount; }
    public void setCessAmount(BigDecimal cessAmount) { this.cessAmount = cessAmount; }
    public BigDecimal getTcsAmount() { return tcsAmount; }
    public void setTcsAmount(BigDecimal tcsAmount) { this.tcsAmount = tcsAmount; }
    public BigDecimal getTotalTax() { return totalTax; }
    public void setTotalTax(BigDecimal totalTax) { this.totalTax = totalTax; }
}
