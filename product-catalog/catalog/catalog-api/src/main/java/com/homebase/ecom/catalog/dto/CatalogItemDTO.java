package com.homebase.ecom.catalog.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CatalogItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String productId;
    private String name;
    private BigDecimal price;
    private Boolean featured;
    private Integer displayOrder;
    private Boolean active;
    private LocalDateTime visibilityStartDate;
    private LocalDateTime visibilityEndDate;
    private List<String> tags = new ArrayList<>();
    private List<String> categoryIds = new ArrayList<>();
    private List<String> collectionIds = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getVisibilityStartDate() { return visibilityStartDate; }
    public void setVisibilityStartDate(LocalDateTime visibilityStartDate) { this.visibilityStartDate = visibilityStartDate; }

    public LocalDateTime getVisibilityEndDate() { return visibilityEndDate; }
    public void setVisibilityEndDate(LocalDateTime visibilityEndDate) { this.visibilityEndDate = visibilityEndDate; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<String> categoryIds) { this.categoryIds = categoryIds; }

    public List<String> getCollectionIds() { return collectionIds; }
    public void setCollectionIds(List<String> collectionIds) { this.collectionIds = collectionIds; }
}
