package com.homebase.ecom.cms.infrastructure.persistence.mapper;

import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.infrastructure.persistence.entity.BannerEntity;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsPageEntity;

import java.util.UUID;

public class CmsMapper {

    public CmsPage toModel(CmsPageEntity entity) {
        if (entity == null) return null;

        CmsPage model = new CmsPage();
        model.setId(entity.getId());
        model.setSlug(entity.getSlug());
        model.setTitle(entity.getTitle());
        model.setBody(entity.getBody());
        model.setPageType(entity.getPageType());
        model.setMetaTitle(entity.getMetaTitle());
        model.setMetaDescription(entity.getMetaDescription());
        model.setPublished(entity.isPublished());
        model.setPublishedAt(entity.getPublishedAt());
        return model;
    }

    public CmsPageEntity toEntity(CmsPage model) {
        if (model == null) return null;

        CmsPageEntity entity = new CmsPageEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setSlug(model.getSlug());
        entity.setTitle(model.getTitle());
        entity.setBody(model.getBody());
        entity.setPageType(model.getPageType());
        entity.setMetaTitle(model.getMetaTitle());
        entity.setMetaDescription(model.getMetaDescription());
        entity.setPublished(model.isPublished());
        entity.setPublishedAt(model.getPublishedAt());
        return entity;
    }

    public Banner toModel(BannerEntity entity) {
        if (entity == null) return null;

        Banner model = new Banner();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setImageUrl(entity.getImageUrl());
        model.setMobileImageUrl(entity.getMobileImageUrl());
        model.setLinkUrl(entity.getLinkUrl());
        model.setPosition(entity.getPosition());
        model.setDisplayOrder(entity.getDisplayOrder());
        model.setActiveFrom(entity.getActiveFrom());
        model.setActiveTo(entity.getActiveTo());
        model.setActive(entity.isActive());
        return model;
    }

    public BannerEntity toEntity(Banner model) {
        if (model == null) return null;

        BannerEntity entity = new BannerEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setImageUrl(model.getImageUrl());
        entity.setMobileImageUrl(model.getMobileImageUrl());
        entity.setLinkUrl(model.getLinkUrl());
        entity.setPosition(model.getPosition());
        entity.setDisplayOrder(model.getDisplayOrder());
        entity.setActiveFrom(model.getActiveFrom());
        entity.setActiveTo(model.getActiveTo());
        entity.setActive(model.isActive());
        return entity;
    }
}
