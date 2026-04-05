package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class GstSummaryQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taxType;
    private BigDecimal taxRate;
    private BigDecimal totalTaxAmount;
    private long lineCount;

    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public BigDecimal getTotalTaxAmount() { return totalTaxAmount; }
    public void setTotalTaxAmount(BigDecimal totalTaxAmount) { this.totalTaxAmount = totalTaxAmount; }
    public long getLineCount() { return lineCount; }
    public void setLineCount(long lineCount) { this.lineCount = lineCount; }
}
