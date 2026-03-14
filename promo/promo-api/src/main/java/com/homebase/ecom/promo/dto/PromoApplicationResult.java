package com.homebase.ecom.promo.dto;

import java.util.Map;

public class PromoApplicationResult {
    private boolean isValid;
    private Double discountAmount;
    private Map<String, Double> itemWiseDiscount;
    private String reasonIfInvalid;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Map<String, Double> getItemWiseDiscount() {
        return itemWiseDiscount;
    }

    public void setItemWiseDiscount(Map<String, Double> itemWiseDiscount) {
        this.itemWiseDiscount = itemWiseDiscount;
    }

    public String getReasonIfInvalid() {
        return reasonIfInvalid;
    }

    public void setReasonIfInvalid(String reasonIfInvalid) {
        this.reasonIfInvalid = reasonIfInvalid;
    }
}
