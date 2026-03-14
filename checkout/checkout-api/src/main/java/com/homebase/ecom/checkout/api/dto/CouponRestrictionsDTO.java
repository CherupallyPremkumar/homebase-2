package com.homebase.ecom.checkout.api.dto;

import java.util.List;

/**
 * Coupon Restrictions DTO
 */
public class CouponRestrictionsDTO {

    private List<String> applicableCategories;
    private List<String> excludedCategories;
    private List<String> applicableSkus;
    private List<String> excludedSkus;
    private Integer maxUsagePerUser;
    private Boolean firstOrderOnly;

    // Getters and Setters
    public List<String> getApplicableCategories() {
        return applicableCategories;
    }

    public void setApplicableCategories(List<String> applicableCategories) {
        this.applicableCategories = applicableCategories;
    }

    public List<String> getExcludedCategories() {
        return excludedCategories;
    }

    public void setExcludedCategories(List<String> excludedCategories) {
        this.excludedCategories = excludedCategories;
    }

    public List<String> getApplicableSkus() {
        return applicableSkus;
    }

    public void setApplicableSkus(List<String> applicableSkus) {
        this.applicableSkus = applicableSkus;
    }

    public List<String> getExcludedSkus() {
        return excludedSkus;
    }

    public void setExcludedSkus(List<String> excludedSkus) {
        this.excludedSkus = excludedSkus;
    }

    public Integer getMaxUsagePerUser() {
        return maxUsagePerUser;
    }

    public void setMaxUsagePerUser(Integer maxUsagePerUser) {
        this.maxUsagePerUser = maxUsagePerUser;
    }

    public Boolean getFirstOrderOnly() {
        return firstOrderOnly;
    }

    public void setFirstOrderOnly(Boolean firstOrderOnly) {
        this.firstOrderOnly = firstOrderOnly;
    }
}
