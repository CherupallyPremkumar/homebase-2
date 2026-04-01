package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "order_tax_lines")
public class OrderTaxLineEntity extends BaseJpaEntity {

    @Column(name = "order_id", nullable = false) private String orderId;
    @Column(name = "variant_id") private String variantId;
    @Column(name = "hsn_code") private String hsnCode;
    @Column(name = "tax_type", nullable = false) private String taxType;
    @Column(name = "taxable_amount", nullable = false, precision = 19, scale = 2) private BigDecimal taxableAmount;
    @Column(name = "gst_rate", precision = 5, scale = 2) private BigDecimal gstRate;
    @Column(name = "cgst_amount", precision = 19, scale = 2) private BigDecimal cgstAmount;
    @Column(name = "sgst_amount", precision = 19, scale = 2) private BigDecimal sgstAmount;
    @Column(name = "igst_amount", precision = 19, scale = 2) private BigDecimal igstAmount;
    @Column(name = "cess_amount", precision = 19, scale = 2) private BigDecimal cessAmount;
    @Column(name = "tcs_amount", precision = 19, scale = 2) private BigDecimal tcsAmount;
    @Column(name = "total_tax", nullable = false, precision = 19, scale = 2) private BigDecimal totalTax;

    public String getOrderId() { return orderId; }
    public void setOrderId(String v) { this.orderId = v; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String v) { this.variantId = v; }
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String v) { this.hsnCode = v; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String v) { this.taxType = v; }
    public BigDecimal getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(BigDecimal v) { this.taxableAmount = v; }
    public BigDecimal getGstRate() { return gstRate; }
    public void setGstRate(BigDecimal v) { this.gstRate = v; }
    public BigDecimal getCgstAmount() { return cgstAmount; }
    public void setCgstAmount(BigDecimal v) { this.cgstAmount = v; }
    public BigDecimal getSgstAmount() { return sgstAmount; }
    public void setSgstAmount(BigDecimal v) { this.sgstAmount = v; }
    public BigDecimal getIgstAmount() { return igstAmount; }
    public void setIgstAmount(BigDecimal v) { this.igstAmount = v; }
    public BigDecimal getCessAmount() { return cessAmount; }
    public void setCessAmount(BigDecimal v) { this.cessAmount = v; }
    public BigDecimal getTcsAmount() { return tcsAmount; }
    public void setTcsAmount(BigDecimal v) { this.tcsAmount = v; }
    public BigDecimal getTotalTax() { return totalTax; }
    public void setTotalTax(BigDecimal v) { this.totalTax = v; }
}
