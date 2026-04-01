package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "tax_exemptions")
public class TaxExemptionEntity extends BaseJpaEntity {

    @Column(name = "exemption_type", nullable = false) private String exemptionType;
    @Column(name = "exemption_value", nullable = false) private String value;
    @Column(name = "description") private String description;
    @Column(name = "full_exemption") private boolean fullExemption;
    @Column(name = "active") private boolean active = true;

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
