package com.homebase.ecom.tax.dto;

import java.math.BigDecimal;

/**
 * Per-item tax breakdown.
 * For INTRA_STATE: CGST + SGST (each = rate/2)
 * For INTER_STATE: IGST (= full rate)
 * Cess is additional (luxury items).
 */
public class TaxLineItemDTO {

    private String variantId;
    private String hsnCode;
    private long taxableAmount;      // minor units

    // Intra-state: CGST + SGST
    private BigDecimal cgstRate;     // null if inter-state
    private long cgstAmount;
    private BigDecimal sgstRate;     // null if inter-state
    private long sgstAmount;

    // Inter-state: IGST
    private BigDecimal igstRate;     // null if intra-state
    private long igstAmount;

    // Cess (luxury items)
    private BigDecimal cessRate;     // null if no cess
    private long cessAmount;

    private long totalTax;           // sum of all components

    public TaxLineItemDTO() {}

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }

    public long getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(long taxableAmount) { this.taxableAmount = taxableAmount; }

    public BigDecimal getCgstRate() { return cgstRate; }
    public void setCgstRate(BigDecimal cgstRate) { this.cgstRate = cgstRate; }

    public long getCgstAmount() { return cgstAmount; }
    public void setCgstAmount(long cgstAmount) { this.cgstAmount = cgstAmount; }

    public BigDecimal getSgstRate() { return sgstRate; }
    public void setSgstRate(BigDecimal sgstRate) { this.sgstRate = sgstRate; }

    public long getSgstAmount() { return sgstAmount; }
    public void setSgstAmount(long sgstAmount) { this.sgstAmount = sgstAmount; }

    public BigDecimal getIgstRate() { return igstRate; }
    public void setIgstRate(BigDecimal igstRate) { this.igstRate = igstRate; }

    public long getIgstAmount() { return igstAmount; }
    public void setIgstAmount(long igstAmount) { this.igstAmount = igstAmount; }

    public BigDecimal getCessRate() { return cessRate; }
    public void setCessRate(BigDecimal cessRate) { this.cessRate = cessRate; }

    public long getCessAmount() { return cessAmount; }
    public void setCessAmount(long cessAmount) { this.cessAmount = cessAmount; }

    public long getTotalTax() { return totalTax; }
    public void setTotalTax(long totalTax) { this.totalTax = totalTax; }
}
