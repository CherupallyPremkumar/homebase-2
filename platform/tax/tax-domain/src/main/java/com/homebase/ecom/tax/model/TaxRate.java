package com.homebase.ecom.tax.model;

import org.chenile.utils.entity.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * GST rate for a specific HSN code.
 * One HSN code can have different rates over time (effective dates).
 *
 * Example:
 *   HSN 6109 (T-shirts) → 5% GST from 2017-07-01
 *   HSN 8517 (Phones)   → 18% GST from 2017-07-01
 *   HSN 2402 (Tobacco)  → 28% GST + 36% Cess from 2017-07-01
 */
public class TaxRate extends BaseEntity {
    private String hsnCode;                  // 4-8 digit HSN/SAC code
    private String description;              // "Cotton T-shirts", "Mobile phones"
    private BigDecimal gstRate;              // Total GST rate (5, 12, 18, 28)
    private BigDecimal cessRate;             // Additional cess (0 for most items)
    private BigDecimal tcsRate;              // TCS rate for marketplace (typically 1%)
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;           // null = currently active
    private boolean active;

    // Computed from gstRate based on INTRA/INTER state
    // INTRA: CGST = gstRate/2, SGST = gstRate/2
    // INTER: IGST = gstRate
    public BigDecimal getCgstRate() { return gstRate != null ? gstRate.divide(BigDecimal.valueOf(2)) : BigDecimal.ZERO; }
    public BigDecimal getSgstRate() { return getCgstRate(); }
    public BigDecimal getIgstRate() { return gstRate != null ? gstRate : BigDecimal.ZERO; }

    // Getters and Setters
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getGstRate() { return gstRate; }
    public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }
    public BigDecimal getCessRate() { return cessRate; }
    public void setCessRate(BigDecimal cessRate) { this.cessRate = cessRate; }
    public BigDecimal getTcsRate() { return tcsRate; }
    public void setTcsRate(BigDecimal tcsRate) { this.tcsRate = tcsRate; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
