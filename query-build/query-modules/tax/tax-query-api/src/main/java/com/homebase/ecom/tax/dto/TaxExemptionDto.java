package com.homebase.ecom.tax.dto;

import java.io.Serializable;

public class TaxExemptionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String exemptionType;
    private String value;
    private String description;
    private boolean fullExemption;
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
