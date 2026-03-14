package com.homebase.ecom.dto;

import java.io.Serializable;

public class ProductDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private String status;
    private String stateId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
}
