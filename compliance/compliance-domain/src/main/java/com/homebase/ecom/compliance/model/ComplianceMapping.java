package com.homebase.ecom.compliance.model;

public class ComplianceMapping {
    private String id;
    private String regulationId;
    private String mappingType;
    private String targetId;
    private String description;
    private String tenant;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRegulationId() { return regulationId; }
    public void setRegulationId(String regulationId) { this.regulationId = regulationId; }
    public String getMappingType() { return mappingType; }
    public void setMappingType(String mappingType) { this.mappingType = mappingType; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
