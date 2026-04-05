package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

public class FilterQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;
    private String label;
    private String filterType;

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getFilterType() { return filterType; }
    public void setFilterType(String filterType) { this.filterType = filterType; }
}
