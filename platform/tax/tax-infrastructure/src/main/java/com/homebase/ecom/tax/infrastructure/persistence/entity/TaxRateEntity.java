package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tax_rates")
public class TaxRateEntity extends BaseJpaEntity {

    @Column(name = "hsn_code", nullable = false) private String hsnCode;
    @Column(name = "description") private String description;
    @Column(name = "gst_rate", nullable = false, precision = 5, scale = 2) private BigDecimal gstRate;
    @Column(name = "cess_rate", precision = 5, scale = 2) private BigDecimal cessRate;
    @Column(name = "tcs_rate", precision = 5, scale = 2) private BigDecimal tcsRate;
    @Column(name = "effective_from") private LocalDate effectiveFrom;
    @Column(name = "effective_to") private LocalDate effectiveTo;
    @Column(name = "active") private boolean active = true;

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
