package com.homebase.ecom.tax.dto;

import java.math.BigDecimal;

public class TaxRateDto {

    private String id;
    private String hsnCode;
    private String description;
    private BigDecimal gstRate;
    private BigDecimal cessRate;
    private BigDecimal tcsRate;
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
