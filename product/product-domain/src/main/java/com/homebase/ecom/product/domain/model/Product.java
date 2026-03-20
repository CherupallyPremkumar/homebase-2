package com.homebase.ecom.product.domain.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

import java.util.*;

public class Product extends AbstractExtendedStateEntity 
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String name;
    private String description;
    private String shortDescription;
    private String brand;
    private String categoryId;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private List<String> tags = new ArrayList<>();
    private List<ProductAttributeValue> attributes = new ArrayList<>();
    private List<ProductMedia> media = new ArrayList<>();
    private List<Variant> variants = new ArrayList<>();
    private List<ProductActivityLog> activities = new ArrayList<>();
    private String tenant;
    private TransientMap transientMap = new TransientMap();

    // Amazon-standard fields (product-014)
    private Integer weightGrams;
    private String dimensionsJson;
    private String hsnCode;
    private String countryOfOrigin;
    private Integer warrantyMonths;
    private boolean returnable = true;
    private Integer returnWindowDays = 7;
    private java.math.BigDecimal basePrice;
    private String taxCategory;
    private boolean active = true;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

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

    public List<ProductAttributeValue> getAttributes() { return attributes; }
    public void setAttributes(List<ProductAttributeValue> attributes) { this.attributes = attributes; }

    public List<ProductMedia> getMedia() { return media; }
    public void setMedia(List<ProductMedia> media) { this.media = media; }

    public List<Variant> getVariants() { return variants; }
    public void setVariants(List<Variant> variants) { this.variants = variants; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    public List<ProductActivityLog> getActivities() { return activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ProductActivityLog log = new ProductActivityLog();
        log.setActivityName(eventId);
        log.setActivityComment(comment);
        log.setActivitySuccess(true);
        activities.add(log);
        return log;
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

    public Integer getWeightGrams() { return weightGrams; }
    public void setWeightGrams(Integer weightGrams) { this.weightGrams = weightGrams; }

    public String getDimensionsJson() { return dimensionsJson; }
    public void setDimensionsJson(String dimensionsJson) { this.dimensionsJson = dimensionsJson; }

    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }

    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }

    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    public boolean isReturnable() { return returnable; }
    public void setReturnable(boolean returnable) { this.returnable = returnable; }

    public Integer getReturnWindowDays() { return returnWindowDays; }
    public void setReturnWindowDays(Integer returnWindowDays) { this.returnWindowDays = returnWindowDays; }

    public java.math.BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(java.math.BigDecimal basePrice) { this.basePrice = basePrice; }

    public String getTaxCategory() { return taxCategory; }
    public void setTaxCategory(String taxCategory) { this.taxCategory = taxCategory; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
