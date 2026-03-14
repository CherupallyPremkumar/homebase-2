package com.homebase.ecom.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for PromoCode query responses.
 */
public class PromoCodeDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private Date expiryDate;
    private Integer usageCount;
    private String stateId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
}
