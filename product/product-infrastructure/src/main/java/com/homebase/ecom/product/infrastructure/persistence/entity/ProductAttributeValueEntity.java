package com.homebase.ecom.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_attribute_values")
public class ProductAttributeValueEntity {
    @Id
    private String id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "attribute_id")
    private String attributeId;

    @Column(name = "option_id")
    private String optionId;

    @Column(name = "raw_value")
    private String rawValue;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getAttributeId() { return attributeId; }
    public void setAttributeId(String attributeId) { this.attributeId = attributeId; }

    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }

    public String getRawValue() { return rawValue; }
    public void setRawValue(String rawValue) { this.rawValue = rawValue; }
}
