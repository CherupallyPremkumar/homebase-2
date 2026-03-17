package com.homebase.ecom.rulesengine.api.dto;

import java.io.Serializable;

public class FactDefinitionDto implements Serializable {
    private String id;
    private String module;      // e.g., "Catalog"
    private String entityName;  // e.g., "Saree"
    private String displayName; // e.g., "Saree Price"
    private String attribute;   // e.g., "price"
    private String dataType;    // e.g., "NUMBER", "STRING", "BOOLEAN"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
