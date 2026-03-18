package com.homebase.ecom.product.infrastructure.persistence.mapper;

import com.homebase.ecom.product.domain.model.Category;
import com.homebase.ecom.product.infrastructure.persistence.entity.CategoryEntity;

public class CategoryMapper {

    public Category toModel(CategoryEntity entity) {
        if (entity == null) return null;
        Category model = new Category();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setSlug(entity.getSlug());
        model.setParentId(entity.getParentId());
        model.setPath(entity.getPath());
        model.setDepth(entity.getDepth());
        model.setDisplayOrder(entity.getDisplayOrder());
        model.setActive(entity.isActive());
        model.setAttributeIds(entity.getAttributeIds());
        model.setTenant(entity.tenant);
        return model;
    }

    public CategoryEntity toEntity(Category model) {
        if (model == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setSlug(model.getSlug());
        entity.setParentId(model.getParentId());
        entity.setPath(model.getPath());
        entity.setDepth(model.getDepth());
        entity.setDisplayOrder(model.getDisplayOrder());
        entity.setActive(model.isActive());
        entity.setAttributeIds(model.getAttributeIds());
        entity.tenant = model.getTenant();
        return entity;
    }
}
