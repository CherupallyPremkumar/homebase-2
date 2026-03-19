package com.homebase.ecom.compliance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "compliance_mappings")
public class ComplianceMappingEntity extends BaseJpaEntity {

    @Column(name = "regulation_id")
    private String regulationId;

    @Column(name = "mapping_type")
    private String mappingType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "description", length = 1000)
    private String description;

    public String getRegulationId() { return regulationId; }
    public void setRegulationId(String regulationId) { this.regulationId = regulationId; }
    public String getMappingType() { return mappingType; }
    public void setMappingType(String mappingType) { this.mappingType = mappingType; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
