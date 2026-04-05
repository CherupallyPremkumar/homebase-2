package com.homebase.ecom.catalog.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CategoryRuleRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String categoryId;
    private String categoryName;
    private int categoryLevel;
    private BigDecimal commissionRate;
    private BigDecimal gstRate;
    private String hsnCode;
    private int returnDays;
    private String returnType;
    private BigDecimal maxWeightKg;
    private String shippingType;
    private String restrictionType;
    private String requiredLicense;
    private int minImages;
    private int minDescriptionLength;
    private boolean eanRequired;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public int getCategoryLevel() { return categoryLevel; }
    public void setCategoryLevel(int categoryLevel) { this.categoryLevel = categoryLevel; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public BigDecimal getGstRate() { return gstRate; }
    public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
    public int getReturnDays() { return returnDays; }
    public void setReturnDays(int returnDays) { this.returnDays = returnDays; }
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    public BigDecimal getMaxWeightKg() { return maxWeightKg; }
    public void setMaxWeightKg(BigDecimal maxWeightKg) { this.maxWeightKg = maxWeightKg; }
    public String getShippingType() { return shippingType; }
    public void setShippingType(String shippingType) { this.shippingType = shippingType; }
    public String getRestrictionType() { return restrictionType; }
    public void setRestrictionType(String restrictionType) { this.restrictionType = restrictionType; }
    public String getRequiredLicense() { return requiredLicense; }
    public void setRequiredLicense(String requiredLicense) { this.requiredLicense = requiredLicense; }
    public int getMinImages() { return minImages; }
    public void setMinImages(int minImages) { this.minImages = minImages; }
    public int getMinDescriptionLength() { return minDescriptionLength; }
    public void setMinDescriptionLength(int minDescriptionLength) { this.minDescriptionLength = minDescriptionLength; }
    public boolean isEanRequired() { return eanRequired; }
    public void setEanRequired(boolean eanRequired) { this.eanRequired = eanRequired; }
}
