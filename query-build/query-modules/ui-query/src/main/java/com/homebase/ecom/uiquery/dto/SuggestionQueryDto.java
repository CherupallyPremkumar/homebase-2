package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

public class SuggestionQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String text;
    private String type;
    private String productId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}
