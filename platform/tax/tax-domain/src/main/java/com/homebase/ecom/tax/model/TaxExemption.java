package com.homebase.ecom.tax.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * Tax exemption rules — B2B, exports, SEZ, essential goods.
 *
 * Example:
 *   type=CATEGORY, value=FOOD_ESSENTIAL → 0% GST
 *   type=SUPPLY_TYPE, value=EXPORT → 0% GST (zero-rated)
 *   type=SUPPLY_TYPE, value=SEZ → 0% GST
 *   type=BUYER_TYPE, value=REGISTERED_B2B → reverse charge eligible
 */
public class TaxExemption extends BaseEntity {

    private String exemptionType;         // CATEGORY, SUPPLY_TYPE, BUYER_TYPE, HSN_CODE
    private String value;                  // FOOD_ESSENTIAL, EXPORT, SEZ, REGISTERED_B2B
    private String description;
    private boolean fullExemption;         // true = 0% tax, false = reduced rate
    private boolean active;

    public String getExemptionType() { return exemptionType; }
    public void setExemptionType(String exemptionType) { this.exemptionType = exemptionType; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isFullExemption() { return fullExemption; }
    public void setFullExemption(boolean fullExemption) { this.fullExemption = fullExemption; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
