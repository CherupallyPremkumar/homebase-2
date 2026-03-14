package com.homebase.ecom.tax.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TaxRateQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String regionCode;
    private String taxType;
    private BigDecimal rate;
    private boolean active;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
