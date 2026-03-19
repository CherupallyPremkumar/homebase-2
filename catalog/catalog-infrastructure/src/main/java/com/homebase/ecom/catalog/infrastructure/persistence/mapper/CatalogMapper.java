package com.homebase.ecom.catalog.infrastructure.persistence.mapper;

import com.homebase.ecom.catalog.infrastructure.persistence.entity.*;
import com.homebase.ecom.catalog.model.*;
import com.homebase.ecom.catalog.model.Collection;
import org.springframework.stereotype.Component;

@Component
public class CatalogMapper {

    // --- CatalogItem ---

    public CatalogItemEntity toEntity(CatalogItem domain) {
        if (domain == null) return null;
        CatalogItemEntity entity = new CatalogItemEntity();
        entity.setId(domain.getId());
        entity.setProductId(domain.getProductId());
        entity.setFeatured(domain.getFeatured());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setActive(domain.getActive());
        entity.setVisibilityStartDate(domain.getVisibilityStartDate());
        entity.setVisibilityEndDate(domain.getVisibilityEndDate());
        entity.setName(domain.getName());
        entity.setPrice(domain.getPrice());
        entity.setDescription(domain.getDescription());
        entity.setBrand(domain.getBrand());
        entity.setImageUrl(domain.getImageUrl());
        entity.setInStock(domain.getInStock());
        entity.setAvailableQty(domain.getAvailableQty());
        entity.setAverageRating(domain.getAverageRating());
        entity.setReviewCount(domain.getReviewCount());
        entity.setTags(domain.getTags());
        entity.tenant = domain.getTenant();
        return entity;
    }

    public CatalogItem toDomain(CatalogItemEntity entity) {
        if (entity == null) return null;
        CatalogItem domain = new CatalogItem();
        domain.setId(entity.getId());
        domain.setProductId(entity.getProductId());
        domain.setFeatured(entity.getFeatured());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setActive(entity.getActive());
        domain.setVisibilityStartDate(entity.getVisibilityStartDate());
        domain.setVisibilityEndDate(entity.getVisibilityEndDate());
        domain.setName(entity.getName());
        domain.setPrice(entity.getPrice());
        domain.setDescription(entity.getDescription());
        domain.setBrand(entity.getBrand());
        domain.setImageUrl(entity.getImageUrl());
        domain.setInStock(entity.getInStock());
        domain.setAvailableQty(entity.getAvailableQty());
        domain.setAverageRating(entity.getAverageRating());
        domain.setReviewCount(entity.getReviewCount());
        domain.setTags(entity.getTags());
        domain.setTenant(entity.tenant);
        return domain;
    }

    // --- Category ---

    public CategoryEntity toCategoryEntity(Category domain) {
        if (domain == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug());
        entity.setDescription(domain.getDescription());
        entity.setParentId(domain.getParentId());
        entity.setLevel(domain.getLevel());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setImageUrl(domain.getImageUrl());
        entity.setIcon(domain.getIcon());
        entity.setActive(domain.getActive());
        entity.setFeatured(domain.getFeatured());
        entity.setProductCount(domain.getProductCount());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setMetadata(toEntity(domain.getMetadata()));
        entity.tenant = domain.getTenant();
        return entity;
    }

    public Category toCategoryDomain(CategoryEntity entity) {
        if (entity == null) return null;
        Category domain = new Category();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setSlug(entity.getSlug());
        domain.setDescription(entity.getDescription());
        domain.setParentId(entity.getParentId());
        domain.setLevel(entity.getLevel());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setImageUrl(entity.getImageUrl());
        domain.setIcon(entity.getIcon());
        domain.setActive(entity.getActive());
        domain.setFeatured(entity.getFeatured());
        domain.setProductCount(entity.getProductCount());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        domain.setMetadata(toDomain(entity.getMetadata()));
        domain.setTenant(entity.tenant);
        return domain;
    }

    private CategoryMetadataEntity toEntity(CategoryMetadata domain) {
        if (domain == null) return null;
        CategoryMetadataEntity entity = new CategoryMetadataEntity();
        entity.setMetaTitle(domain.getMetaTitle());
        entity.setMetaDescription(domain.getMetaDescription());
        entity.setMetaKeywords(domain.getMetaKeywords());
        entity.setBannerImageUrl(domain.getBannerImageUrl());
        entity.setShowInMenu(domain.getShowInMenu());
        entity.setShowInFooter(domain.getShowInFooter());
        return entity;
    }

    private CategoryMetadata toDomain(CategoryMetadataEntity entity) {
        if (entity == null) return null;
        CategoryMetadata domain = new CategoryMetadata();
        domain.setMetaTitle(entity.getMetaTitle());
        domain.setMetaDescription(entity.getMetaDescription());
        domain.setMetaKeywords(entity.getMetaKeywords());
        domain.setBannerImageUrl(entity.getBannerImageUrl());
        domain.setShowInMenu(entity.getShowInMenu());
        domain.setShowInFooter(entity.getShowInFooter());
        return domain;
    }

    // --- Collection ---

    public CollectionEntity toCollectionEntity(com.homebase.ecom.catalog.model.Collection domain) {
        if (domain == null) return null;
        CollectionEntity entity = new CollectionEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug());
        entity.setDescription(domain.getDescription());
        entity.setType(domain.getType());
        entity.setImageUrl(domain.getImageUrl());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setActive(domain.getActive());
        entity.setFeatured(domain.getFeatured());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setAutoUpdate(domain.getAutoUpdate());
        entity.setRuleExpression(domain.getRuleExpression());
        entity.setProductCount(domain.getProductCount());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.tenant = domain.getTenant();
        return entity;
    }

    public Collection toCollectionDomain(CollectionEntity entity) {
        if (entity == null) return null;
        Collection domain = new Collection();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setSlug(entity.getSlug());
        domain.setDescription(entity.getDescription());
        domain.setType(entity.getType());
        domain.setImageUrl(entity.getImageUrl());
        domain.setStartDate(entity.getStartDate());
        domain.setEndDate(entity.getEndDate());
        domain.setActive(entity.getActive());
        domain.setFeatured(entity.getFeatured());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setAutoUpdate(entity.getAutoUpdate());
        domain.setRuleExpression(entity.getRuleExpression());
        domain.setProductCount(entity.getProductCount());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        domain.setTenant(entity.tenant);
        return domain;
    }

    // --- Mappings ---

    public CategoryProductMappingEntity toCategoryProductMappingEntity(CategoryProductMapping domain) {
        if (domain == null) return null;
        CategoryProductMappingEntity entity = new CategoryProductMappingEntity();
        entity.setId(domain.getId());
        entity.setCategoryId(domain.getCategoryId());
        entity.setProductId(domain.getProductId());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setAddedBy(domain.getAddedBy());
        entity.tenant = domain.getTenant();
        return entity;
    }

    public CategoryProductMapping toCategoryProductMappingDomain(CategoryProductMappingEntity entity) {
        if (entity == null) return null;
        CategoryProductMapping domain = new CategoryProductMapping();
        domain.setId(entity.getId());
        domain.setCategoryId(entity.getCategoryId());
        domain.setProductId(entity.getProductId());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setAddedBy(entity.getAddedBy());
        domain.setTenant(entity.tenant);
        return domain;
    }

    public CollectionProductMappingEntity toCollectionProductMappingEntity(CollectionProductMapping domain) {
        if (domain == null) return null;
        CollectionProductMappingEntity entity = new CollectionProductMappingEntity();
        entity.setId(domain.getId());
        entity.setCollectionId(domain.getCollectionId());
        entity.setProductId(domain.getProductId());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setAddedBy(domain.getAddedBy());
        entity.tenant = domain.getTenant();
        return entity;
    }

    public CollectionProductMapping toCollectionProductMappingDomain(CollectionProductMappingEntity entity) {
        if (entity == null) return null;
        CollectionProductMapping domain = new CollectionProductMapping();
        domain.setId(entity.getId());
        domain.setCollectionId(entity.getCollectionId());
        domain.setProductId(entity.getProductId());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setAddedBy(entity.getAddedBy());
        domain.setTenant(entity.tenant);
        return domain;
    }
}
