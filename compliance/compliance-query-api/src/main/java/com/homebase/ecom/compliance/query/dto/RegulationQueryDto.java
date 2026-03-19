package com.homebase.ecom.compliance.query.dto;

import java.time.LocalDate;

public class RegulationQueryDto {
    private String id;
    private String actName;
    private String section;
    private String requirementSummary;
    private String jurisdiction;
    private String applicableTo;
    private LocalDate enforcementDate;
    private String category;
    private boolean isActive;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getActName() { return actName; }
    public void setActName(String actName) { this.actName = actName; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getRequirementSummary() { return requirementSummary; }
    public void setRequirementSummary(String requirementSummary) { this.requirementSummary = requirementSummary; }
    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }
    public String getApplicableTo() { return applicableTo; }
    public void setApplicableTo(String applicableTo) { this.applicableTo = applicableTo; }
    public LocalDate getEnforcementDate() { return enforcementDate; }
    public void setEnforcementDate(LocalDate enforcementDate) { this.enforcementDate = enforcementDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
