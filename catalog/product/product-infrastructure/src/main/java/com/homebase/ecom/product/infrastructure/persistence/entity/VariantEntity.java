package com.homebase.ecom.product.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "product_variants")
public class VariantEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String sku;

    @ElementCollection
    @CollectionTable(name = "variant_attributes", joinColumns = @JoinColumn(name = "variant_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "variant_id")
    private List<VariantMediaEntity> media = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    public List<VariantMediaEntity> getMedia() { return media; }
    public void setMedia(List<VariantMediaEntity> media) { this.media = media; }
}
