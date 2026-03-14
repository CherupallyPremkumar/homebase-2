package com.homebase.ecom.policy.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "fact_definition")
public class FactDefinitionEntity extends BaseJpaEntity {

    @Column(name = "module_name", nullable = false)
    private String module;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "attribute", nullable = false)
    private String attribute;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    // Getters and Setters


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
