package com.homebase.ecom.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "attribute_options")
public class AttributeOptionEntity {
    @Id
    private String id;

    @Column(name = "attribute_id")
    private String attributeId;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String label;

    @Column(name = "color_swatch")
    private String colorSwatch;

    @Column(name = "sort_order")
    private int sortOrder;

    // Getters and Setters
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
