package com.homebase.ecom.product.domain.model;

import org.chenile.utils.entity.model.BaseEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Variant extends BaseEntity {
    private String sku;
    private Map<String, String> attributes = new HashMap<>(); // Standard attributes
    private List<VariantMedia> media = new ArrayList<>();
    private String tenant;

    // Getters and Setters


    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    public List<VariantMedia> getMedia() { return media; }
    public void setMedia(List<VariantMedia> media) { this.media = media; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
