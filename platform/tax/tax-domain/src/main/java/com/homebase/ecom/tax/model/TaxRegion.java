package com.homebase.ecom.tax.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * Indian state/UT codes for GST jurisdiction.
 * Used to determine INTRA vs INTER state supply.
 *
 * State code is the first 2 digits of GSTIN.
 * Example: 29 = Karnataka, 27 = Maharashtra, 07 = Delhi
 */
public class TaxRegion extends BaseEntity {

    private String stateCode;              // 2-digit: 29, 27, 07
    private String stateName;              // Karnataka, Maharashtra, Delhi
    private String stateAlpha;             // KA, MH, DL
    private boolean unionTerritory;        // true for Chandigarh, Ladakh, etc.
    private boolean active;

    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
    public String getStateAlpha() { return stateAlpha; }
    public void setStateAlpha(String stateAlpha) { this.stateAlpha = stateAlpha; }
    public boolean isUnionTerritory() { return unionTerritory; }
    public void setUnionTerritory(boolean unionTerritory) { this.unionTerritory = unionTerritory; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
