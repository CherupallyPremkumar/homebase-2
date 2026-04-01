package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "tax_regions")
public class TaxRegionEntity extends BaseJpaEntity {

    @Column(name = "state_code", nullable = false, unique = true) private String stateCode;
    @Column(name = "state_name", nullable = false) private String stateName;
    @Column(name = "state_alpha", nullable = false) private String stateAlpha;
    @Column(name = "union_territory") private boolean unionTerritory;
    @Column(name = "active") private boolean active = true;

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
