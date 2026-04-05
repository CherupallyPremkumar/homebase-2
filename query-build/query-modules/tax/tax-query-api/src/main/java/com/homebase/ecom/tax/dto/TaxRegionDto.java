package com.homebase.ecom.tax.dto;

import java.io.Serializable;

public class TaxRegionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String stateCode;
    private String stateName;
    private String stateAlpha;
    private boolean unionTerritory;
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
