package com.homebase.ecom.catalog.port.client;

import java.math.BigDecimal;
import java.util.List;

/**
 * ACL value object — catalog's own view of a product.
 * Decouples catalog-domain from product-api.
 */
public class ProductSnapshot {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String brand;
    private String imageUrl;
    private String category;
    private BigDecimal price;
    private Boolean active;
    private List<String> categoryIds;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public List<String> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<String> categoryIds) { this.categoryIds = categoryIds; }
}
