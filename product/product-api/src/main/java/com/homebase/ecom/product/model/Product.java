package com.homebase.ecom.product.model;

import org.chenile.stm.State;
import java.io.Serializable;
import java.util.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String brand;
    private String categoryId;
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

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public List<ProductAttributeValueDTO> getAttributes() { return attributes; }
    public void setAttributes(List<ProductAttributeValueDTO> attributes) { this.attributes = attributes; }

    public List<ProductMediaDTO> getMedia() { return media; }
    public void setMedia(List<ProductMediaDTO> media) { this.media = media; }

    public List<VariantDTO> getVariants() { return variants; }
    public void setVariants(List<VariantDTO> variants) { this.variants = variants; }

    public State getCurrentState() { return currentState; }
    public void setCurrentState(State currentState) { this.currentState = currentState; }
}
