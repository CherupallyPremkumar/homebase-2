package com.homebase.ecom.product.dto;

import java.io.Serializable;

public class AttributeOptionDto implements Serializable {
    private String id;
    private String attributeId;
    private String value;
    private String label;
    private String colorSwatch;
    private int sortOrder;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAttributeId() { return attributeId; }
    public void setAttributeId(String attributeId) { this.attributeId = attributeId; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getColorSwatch() { return colorSwatch; }
    public void setColorSwatch(String colorSwatch) { this.colorSwatch = colorSwatch; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
