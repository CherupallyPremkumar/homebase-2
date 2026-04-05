package com.homebase.ecom.product.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttributeDefinitionDTO implements Serializable {
    private String id;
    private String code;
    private String label;
    private String inputType;
    private boolean filterable;
    private boolean searchable;
    private boolean required;
    private int displayOrder;
    private List<AttributeOptionDTO> options = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getInputType() { return inputType; }
    public void setInputType(String inputType) { this.inputType = inputType; }

    public boolean isFilterable() { return filterable; }
    public void setFilterable(boolean filterable) { this.filterable = filterable; }

    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public List<AttributeOptionDTO> getOptions() { return options; }
    public void setOptions(List<AttributeOptionDTO> options) { this.options = options; }
}
