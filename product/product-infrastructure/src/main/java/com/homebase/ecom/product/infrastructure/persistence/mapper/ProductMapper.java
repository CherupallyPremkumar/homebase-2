package com.homebase.ecom.product.infrastructure.persistence.mapper;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.model.Variant;
import com.homebase.ecom.product.domain.model.ProductActivityLog;
import com.homebase.ecom.product.domain.model.ProductAttributeValue;
import com.homebase.ecom.product.domain.model.ProductMedia;
import com.homebase.ecom.product.domain.model.VariantMedia;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductActivityLogEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductAttributeValueEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductMediaEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantMediaEntity;

import java.util.stream.Collectors;
import java.util.ArrayList;

public class ProductMapper {

    public Product toModel(ProductEntity entity) {
        if (entity == null) return null;
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setShortDescription(entity.getShortDescription());
        product.setBrand(entity.getBrand());
        product.setCategoryId(entity.getCategoryId());
        product.setSlug(entity.getSlug());
        product.setMetaTitle(entity.getMetaTitle());
        product.setMetaDescription(entity.getMetaDescription());
        product.setSupplierId(entity.getSupplierId());
        product.setTags(entity.getTags() != null ? new ArrayList<>(entity.getTags()) : new ArrayList<>());
        product.setCurrentState(entity.getCurrentState());
        product.setTenant(entity.tenant);
        // Amazon-standard fields
        product.setWeightGrams(entity.getWeightGrams());
        product.setDimensionsJson(entity.getDimensionsJson());
        product.setHsnCode(entity.getHsnCode());
        product.setCountryOfOrigin(entity.getCountryOfOrigin());
        product.setWarrantyMonths(entity.getWarrantyMonths());
        product.setReturnable(entity.isReturnable());
        product.setReturnWindowDays(entity.getReturnWindowDays());
        product.setBasePrice(entity.getBasePrice());
        product.setTaxCategory(entity.getTaxCategory());
        product.setActive(entity.isActive());
        // Base entity fields
        product.setVersion(entity.getVersion());
        product.setCreatedTime(entity.getCreatedTime());
        product.setLastModifiedTime(entity.getLastModifiedTime());
        product.setLastModifiedBy(entity.getLastModifiedBy());
        product.setCreatedBy(entity.getCreatedBy());
        product.setStateEntryTime(entity.getStateEntryTime());
        product.setSlaLate(entity.getSlaLate());
        product.setSlaTendingLate(entity.getSlaTendingLate());

        if (entity.getVariants() != null) {
            product.setVariants(entity.getVariants().stream()
                .map(this::variantToModel)
                .collect(Collectors.toList()));
        }

        if (entity.getAttributes() != null) {
            product.setAttributes(entity.getAttributes().stream()
                .map(this::attributeValueToModel)
                .collect(Collectors.toList()));
        }

        if (entity.getMedia() != null) {
            product.setMedia(entity.getMedia().stream()
                .map(this::productMediaToModel)
                .collect(Collectors.toList()));
        }

        if (entity.getActivities() != null) {
            for (ProductActivityLogEntity logEntity : entity.getActivities()) {
                product.addActivity(logEntity.activityName, logEntity.activityComment);
            }
        }

        return product;
    }

    public ProductEntity toEntity(Product model) {
        if (model == null) return null;
        ProductEntity entity = new ProductEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setShortDescription(model.getShortDescription());
        entity.setBrand(model.getBrand());
        entity.setCategoryId(model.getCategoryId());
        entity.setSlug(model.getSlug());
        entity.setMetaTitle(model.getMetaTitle());
        entity.setMetaDescription(model.getMetaDescription());
        entity.setSupplierId(model.getSupplierId());
        entity.setTags(model.getTags() != null ? new ArrayList<>(model.getTags()) : new ArrayList<>());
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();
        // Amazon-standard fields
        entity.setWeightGrams(model.getWeightGrams());
        entity.setDimensionsJson(model.getDimensionsJson());
        entity.setHsnCode(model.getHsnCode());
        entity.setCountryOfOrigin(model.getCountryOfOrigin());
        entity.setWarrantyMonths(model.getWarrantyMonths());
        entity.setReturnable(model.isReturnable());
        entity.setReturnWindowDays(model.getReturnWindowDays());
        entity.setBasePrice(model.getBasePrice());
        entity.setTaxCategory(model.getTaxCategory());
        entity.setActive(model.isActive());
        // Base entity fields
        if (model.getVersion() != null) entity.setVersion(model.getVersion());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaLate(model.getSlaLate());
        entity.setSlaTendingLate(model.getSlaTendingLate());

        if (model.getVariants() != null) {
            entity.setVariants(model.getVariants().stream()
                .map(this::variantToEntity)
                .collect(Collectors.toList()));
        }

        if (model.getAttributes() != null) {
            entity.setAttributes(model.getAttributes().stream()
                .map(this::attributeValueToEntity)
                .collect(Collectors.toList()));
        }

        if (model.getMedia() != null) {
            entity.setMedia(model.getMedia().stream()
                .map(this::productMediaToEntity)
                .collect(Collectors.toList()));
        }

        if (model.obtainActivities() != null) {
            var activityEntities = new ArrayList<ProductActivityLogEntity>();
            for (var log : model.obtainActivities()) {
                ProductActivityLogEntity logEntity = new ProductActivityLogEntity();
                logEntity.setActivityName(log.getName());
                logEntity.setActivityComment(log.getComment());
                logEntity.setActivitySuccess(log.getSuccess());
                activityEntities.add(logEntity);
            }
            entity.setActivities(activityEntities);
        }

        return entity;
    }

    private Variant variantToModel(VariantEntity entity) {
        Variant variant = new Variant();
        variant.setId(entity.getId());
        variant.setSku(entity.getSku());
        variant.setAttributes(entity.getAttributes());
        if (entity.getMedia() != null) {
            variant.setMedia(entity.getMedia().stream()
                .map(this::variantMediaToModel)
                .collect(Collectors.toList()));
        }
        return variant;
    }

    private VariantEntity variantToEntity(Variant model) {
        VariantEntity entity = new VariantEntity();
        entity.setId(model.getId());
        entity.setSku(model.getSku());
        entity.setAttributes(model.getAttributes());
        if (model.getMedia() != null) {
            entity.setMedia(model.getMedia().stream()
                .map(this::variantMediaToEntity)
                .collect(Collectors.toList()));
        }
        return entity;
    }

    private ProductAttributeValue attributeValueToModel(ProductAttributeValueEntity entity) {
        ProductAttributeValue model = new ProductAttributeValue();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setAttributeId(entity.getAttributeId());
        model.setOptionId(entity.getOptionId());
        model.setRawValue(entity.getRawValue());
        return model;
    }

    private ProductAttributeValueEntity attributeValueToEntity(ProductAttributeValue model) {
        ProductAttributeValueEntity entity = new ProductAttributeValueEntity();
        entity.setId(model.getId());
        entity.setProductId(model.getProductId());
        entity.setAttributeId(model.getAttributeId());
        entity.setOptionId(model.getOptionId());
        entity.setRawValue(model.getRawValue());
        return entity;
    }

    private ProductMedia productMediaToModel(ProductMediaEntity entity) {
        ProductMedia model = new ProductMedia();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setAssetId(entity.getAssetId());
        model.setPrimary(entity.isPrimary());
        model.setSortOrder(entity.getSortOrder());
        return model;
    }

    private ProductMediaEntity productMediaToEntity(ProductMedia model) {
        ProductMediaEntity entity = new ProductMediaEntity();
        entity.setId(model.getId());
        entity.setProductId(model.getProductId());
        entity.setAssetId(model.getAssetId());
        entity.setPrimary(model.isPrimary());
        entity.setSortOrder(model.getSortOrder());
        return entity;
    }

    private VariantMedia variantMediaToModel(VariantMediaEntity entity) {
        VariantMedia model = new VariantMedia();
        model.setVariantId(entity.getVariantId());
        model.setAssetId(entity.getAssetId());
        model.setPrimary(entity.isPrimary());
        model.setSortOrder(entity.getSortOrder());
        return model;
    }

    private VariantMediaEntity variantMediaToEntity(VariantMedia model) {
        VariantMediaEntity entity = new VariantMediaEntity();
        entity.setVariantId(model.getVariantId());
        entity.setAssetId(model.getAssetId());
        entity.setPrimary(model.isPrimary());
        entity.setSortOrder(model.getSortOrder());
        return entity;
    }

    /**
     * Merges incoming (partial) data into an existing entity, preserving fields
     * that the incoming model does not set (null-safe for nullable columns).
     */
    public ProductEntity mergeEntity(Product incoming, ProductEntity existing) {
        if (incoming == null || existing == null) return existing;

        if (incoming.getSupplierId() != null) existing.setSupplierId(incoming.getSupplierId());
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getShortDescription() != null) existing.setShortDescription(incoming.getShortDescription());
        if (incoming.getBrand() != null) existing.setBrand(incoming.getBrand());
        if (incoming.getCategoryId() != null) existing.setCategoryId(incoming.getCategoryId());
        if (incoming.getSlug() != null) existing.setSlug(incoming.getSlug());
        if (incoming.getMetaTitle() != null) existing.setMetaTitle(incoming.getMetaTitle());
        if (incoming.getMetaDescription() != null) existing.setMetaDescription(incoming.getMetaDescription());
        if (incoming.getTags() != null && !incoming.getTags().isEmpty()) {
            existing.setTags(new ArrayList<>(incoming.getTags()));
        }
        // Amazon-standard fields
        if (incoming.getWeightGrams() != null) existing.setWeightGrams(incoming.getWeightGrams());
        if (incoming.getDimensionsJson() != null) existing.setDimensionsJson(incoming.getDimensionsJson());
        if (incoming.getHsnCode() != null) existing.setHsnCode(incoming.getHsnCode());
        if (incoming.getCountryOfOrigin() != null) existing.setCountryOfOrigin(incoming.getCountryOfOrigin());
        if (incoming.getWarrantyMonths() != null) existing.setWarrantyMonths(incoming.getWarrantyMonths());
        if (incoming.getReturnWindowDays() != null) existing.setReturnWindowDays(incoming.getReturnWindowDays());
        if (incoming.getBasePrice() != null) existing.setBasePrice(incoming.getBasePrice());
        if (incoming.getTaxCategory() != null) existing.setTaxCategory(incoming.getTaxCategory());

        // STM fields are managed by the framework, not merged here
        return existing;
    }
}
