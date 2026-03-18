package com.homebase.ecom.product.infrastructure.persistence.mapper;

import com.homebase.ecom.product.domain.model.Variant;
import com.homebase.ecom.product.domain.model.VariantMedia;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantMediaEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class VariantMapper {

    public Variant toModel(VariantEntity entity) {
        if (entity == null) return null;
        Variant model = new Variant();
        model.setId(entity.getId());
        model.setSku(entity.getSku());
        model.setAttributes(entity.getAttributes());
        model.setTenant(entity.tenant);
        if (entity.getMedia() != null) {
            model.setMedia(entity.getMedia().stream()
                    .map(this::toMediaModel)
                    .collect(Collectors.toList()));
        }
        return model;
    }

    public VariantEntity toEntity(Variant model) {
        if (model == null) return null;
        VariantEntity entity = new VariantEntity();
        entity.setId(model.getId());
        entity.setSku(model.getSku());
        entity.setAttributes(model.getAttributes());
        entity.tenant = model.getTenant();
        if (model.getMedia() != null) {
            entity.setMedia(model.getMedia().stream()
                    .map(this::toMediaEntity)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    private VariantMedia toMediaModel(VariantMediaEntity entity) {
        VariantMedia model = new VariantMedia();
        model.setVariantId(entity.getVariantId());
        model.setAssetId(entity.getAssetId());
        model.setPrimary(entity.isPrimary());
        model.setSortOrder(entity.getSortOrder());
        return model;
    }

    private VariantMediaEntity toMediaEntity(VariantMedia model) {
        VariantMediaEntity entity = new VariantMediaEntity();
        entity.setVariantId(model.getVariantId());
        entity.setAssetId(model.getAssetId());
        entity.setPrimary(model.isPrimary());
        entity.setSortOrder(model.getSortOrder());
        return entity;
    }
}
