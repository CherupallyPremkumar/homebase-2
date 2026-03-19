package com.homebase.ecom.compliance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "regulations")
public class RegulationEntity extends BaseJpaEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "regulation_type")
    private String regulationType;

    @Column(name = "jurisdiction")
    private String jurisdiction;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "reference_url", length = 1024)
    private String referenceUrl;

    @Column(name = "active")
    private boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegulationType() { return regulationType; }
    public void setRegulationType(String regulationType) { this.regulationType = regulationType; }
    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReferenceUrl() { return referenceUrl; }
    public void setReferenceUrl(String referenceUrl) { this.referenceUrl = referenceUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
