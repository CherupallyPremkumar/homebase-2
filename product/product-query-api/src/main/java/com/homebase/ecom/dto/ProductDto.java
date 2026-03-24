package com.homebase.ecom.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Product DTO for query layer.
 * Used by both list views (flat fields only) and detail views (with nested variants/media/tags).
 * List queries populate flat fields; getById populates nested collections via MyBatis sub-selects.
 */
public class ProductDto implements Serializable {
    private static final long serialVersionUID = 1L;

    // Identity
    private String id;
    private String name;
    private String slug;
    private String stateId;
    private String flowId;

    // Content
    private String description;
    private String shortDescription;
    private String brand;
    private String metaTitle;
    private String metaDescription;

    // Classification
    private String categoryId;
    private String categoryName;

    // Pricing
    private BigDecimal basePrice;
    private BigDecimal sellingPrice;
    private BigDecimal mrp;

    // Product attributes
    private String hsnCode;
    private String countryOfOrigin;
    private Integer weightGrams;
    private String taxCategory;
    private Integer warrantyMonths;
    private Boolean isActive;
    private Boolean isReturnable;
    private Integer returnWindowDays;

    // Audit
    private String createdBy;
    private String lastModifiedBy;
    private Date createdTime;
    private Date lastModifiedTime;

    // Nested collections (populated only for detail/getById queries)
    private List<VariantDto> variants;
    private List<ProductMediaDto> media;
    private List<String> tags;

    // --- Getters and Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }

    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }

    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }

    public Integer getWeightGrams() { return weightGrams; }
    public void setWeightGrams(Integer weightGrams) { this.weightGrams = weightGrams; }

    public String getTaxCategory() { return taxCategory; }
    public void setTaxCategory(String taxCategory) { this.taxCategory = taxCategory; }

    public Integer getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(Integer warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsReturnable() { return isReturnable; }
    public void setIsReturnable(Boolean isReturnable) { this.isReturnable = isReturnable; }

    public Integer getReturnWindowDays() { return returnWindowDays; }
    public void setReturnWindowDays(Integer returnWindowDays) { this.returnWindowDays = returnWindowDays; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }

    public List<VariantDto> getVariants() { return variants != null ? variants : new ArrayList<>(); }
    public void setVariants(List<VariantDto> variants) { this.variants = variants; }

    public List<ProductMediaDto> getMedia() { return media != null ? media : new ArrayList<>(); }
    public void setMedia(List<ProductMediaDto> media) { this.media = media; }

    public List<String> getTags() { return tags != null ? tags : new ArrayList<>(); }
    public void setTags(List<String> tags) { this.tags = tags; }
}
