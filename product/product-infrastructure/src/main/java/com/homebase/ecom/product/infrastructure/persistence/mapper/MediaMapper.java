package com.homebase.ecom.product.infrastructure.persistence.mapper;

import com.homebase.ecom.product.domain.model.MediaAsset;
import com.homebase.ecom.product.infrastructure.persistence.entity.MediaAssetEntity;

public class MediaMapper {

    public MediaAsset toModel(MediaAssetEntity entity) {
        if (entity == null) return null;
        MediaAsset model = new MediaAsset();
        model.setId(entity.getId());
        model.setOriginalUrl(entity.getOriginalUrl());
        model.setCdnUrl(entity.getCdnUrl());
        model.setType(entity.getType());
        model.setMimeType(entity.getMimeType());
        model.setFileSizeBytes(entity.getFileSizeBytes());
        model.setWidth(entity.getWidth());
        model.setHeight(entity.getHeight());
        model.setAltText(entity.getAltText());
        return model;
    }

    public MediaAssetEntity toEntity(MediaAsset model) {
        if (model == null) return null;
        MediaAssetEntity entity = new MediaAssetEntity();
        entity.setId(model.getId());
        entity.setOriginalUrl(model.getOriginalUrl());
        entity.setCdnUrl(model.getCdnUrl());
        entity.setType(model.getType());
        entity.setMimeType(model.getMimeType());
        entity.setFileSizeBytes(model.getFileSizeBytes());
        entity.setWidth(model.getWidth());
        entity.setHeight(model.getHeight());
        entity.setAltText(model.getAltText());
        return entity;
    }
}
