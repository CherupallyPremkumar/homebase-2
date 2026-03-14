package com.homebase.ecom.product.infrastructure.persistence.entity;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "products")
public class ProductEntity extends AbstractJpaStateEntity 
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "brand")
    private String brand;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "sku", length = 100)
    private String sku;

    @Column(name = "weight", precision = 10, scale = 3)
    private java.math.BigDecimal weight;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "dimensions", columnDefinition = "jsonb")
    private String dimensions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductAttributeValueEntity> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductMediaEntity> media = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<VariantEntity> variants = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductActivityLogEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public java.math.BigDecimal getWeight() { return weight; }
    public void setWeight(java.math.BigDecimal weight) { this.weight = weight; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public List<ProductAttributeValueEntity> getAttributes() { return attributes; }
    public void setAttributes(List<ProductAttributeValueEntity> attributes) { this.attributes = attributes; }

    public List<ProductMediaEntity> getMedia() { return media; }
    public void setMedia(List<ProductMediaEntity> media) { this.media = media; }

    public List<VariantEntity> getVariants() { return variants; }
    public void setVariants(List<VariantEntity> variants) { this.variants = variants; }

    public List<ProductActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<ProductActivityLogEntity> activities) { this.activities = activities; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ProductActivityLogEntity log = new ProductActivityLogEntity();
        log.setActivityName(eventId);
        log.setActivityComment(comment);
        log.setActivitySuccess(true);
        activities.add(log);
        return log;
    }
}
