package com.homebase.ecom.product.model;

import java.io.Serializable;
import java.util.*;

public class VariantDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String sku;
    private Map<String, String> attributes = new HashMap<>();
    private List<VariantMediaDTO> media = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    public List<VariantMediaDTO> getMedia() { return media; }
    public void setMedia(List<VariantMediaDTO> media) { this.media = media; }
}
