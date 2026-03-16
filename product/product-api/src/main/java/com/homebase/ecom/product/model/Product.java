package com.homebase.ecom.product.model;

import jakarta.validation.constraints.Size;
import org.chenile.stm.State;
import java.io.Serializable;
import java.util.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @Size(max = 500, message = "Product name must not exceed 500 characters")
    private String name;

    @Size(max = 10000, message = "Description must not exceed 10000 characters")
    private String description;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    @Size(max = 200, message = "Brand must not exceed 200 characters")
    private String brand;

    private String categoryId;

    @Size(max = 300, message = "Slug must not exceed 300 characters")
    private String slug;

    @Size(max = 200, message = "Meta title must not exceed 200 characters")
    private String metaTitle;

    @Size(max = 500, message = "Meta description must not exceed 500 characters")
    private String metaDescription;

    private List<String> tags = new ArrayList<>();
    private List<ProductAttributeValueDTO> attributes = new ArrayList<>();
    private List<ProductMediaDTO> media = new ArrayList<>();
    private List<VariantDTO> variants = new ArrayList<>();
    private State currentState;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }

    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<ProductAttributeValueDTO> getAttributes() { return attributes; }
    public void setAttributes(List<ProductAttributeValueDTO> attributes) { this.attributes = attributes; }

    public List<ProductMediaDTO> getMedia() { return media; }
    public void setMedia(List<ProductMediaDTO> media) { this.media = media; }

    public List<VariantDTO> getVariants() { return variants; }
    public void setVariants(List<VariantDTO> variants) { this.variants = variants; }

    public State getCurrentState() { return currentState; }
    public void setCurrentState(State currentState) { this.currentState = currentState; }
}
