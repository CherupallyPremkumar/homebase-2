package com.homebase.ecom.product.domain.model;

import java.util.ArrayList;
import java.util.List;

public class AttributeDefinition {
    public enum InputType {
        TEXT, SELECT, MULTISELECT, BOOLEAN, NUMBER, DATE
    }

    private String id;
    private String code;
    private String label;
    private InputType inputType;
    private boolean filterable;
    private boolean searchable;
    private boolean required;
    private int displayOrder;
    private List<AttributeOption> options = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public InputType getInputType() { return inputType; }
    public void setInputType(InputType inputType) { this.inputType = inputType; }

    public boolean isFilterable() { return filterable; }
    public void setFilterable(boolean filterable) { this.filterable = filterable; }

    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public List<AttributeOption> getOptions() { return options; }
    public void setOptions(List<AttributeOption> options) { this.options = options; }
}
