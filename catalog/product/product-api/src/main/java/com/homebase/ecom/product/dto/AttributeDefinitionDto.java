package com.homebase.ecom.product.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttributeDefinitionDto implements Serializable {
    public enum InputTypeDto {
        TEXT, SELECT, MULTISELECT, BOOLEAN, NUMBER, DATE
    }

    private String id;
    private String code;
    private String label;
    private InputTypeDto inputType;
    private boolean filterable;
    private boolean searchable;
    private boolean required;
    private int displayOrder;
    private List<AttributeOptionDto> options = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public InputTypeDto getInputType() { return inputType; }
    public void setInputType(InputTypeDto inputType) { this.inputType = inputType; }

    public boolean isFilterable() { return filterable; }
    public void setFilterable(boolean filterable) { this.filterable = filterable; }

    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public List<AttributeOptionDto> getOptions() { return options; }
    public void setOptions(List<AttributeOptionDto> options) { this.options = options; }
}
