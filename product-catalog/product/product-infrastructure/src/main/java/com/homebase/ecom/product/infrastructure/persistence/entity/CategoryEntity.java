package com.homebase.ecom.product.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(name = "parent_id")
    private String parentId;

    private String path;
    private int depth;

    @Column(name = "display_order")
    private int displayOrder;

    private boolean active;

    @ElementCollection
    @CollectionTable(name = "category_attributes", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "attribute_id")
    private List<String> attributeIds = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<String> getAttributeIds() { return attributeIds; }
    public void setAttributeIds(List<String> attributeIds) { this.attributeIds = attributeIds; }
}
