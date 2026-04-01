package com.homebase.ecom.compliance.infrastructure.persistence.mapper;

import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.model.PlatformPolicyActivity;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.PlatformPolicyEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.PlatformPolicyActivityEntity;

public class PlatformPolicyMapper {

    public PlatformPolicy toModel(PlatformPolicyEntity entity) {
        if (entity == null) return null;
        PlatformPolicy model = new PlatformPolicy();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setPolicyCategory(entity.getPolicyCategory());
        model.setVersionLabel(entity.getVersionLabel());
        model.setContentUrl(entity.getContentUrl());
        model.setContentHash(entity.getContentHash());
        model.setSummaryText(entity.getSummaryText());
        model.setEffectiveDate(entity.getEffectiveDate());
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());
        if (entity.getActivities() != null) {
            for (PlatformPolicyActivityEntity ae : entity.getActivities()) {
                PlatformPolicyActivity a = new PlatformPolicyActivity();
                a.setName(ae.getActivityName());
                a.setSuccess(ae.isActivitySuccess());
                a.setComment(ae.getActivityComment());
                model.obtainActivities().add(a);
            }
        }
        return model;
    }

    public PlatformPolicyEntity toEntity(PlatformPolicy model) {
        if (model == null) return null;
        PlatformPolicyEntity entity = new PlatformPolicyEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setPolicyCategory(model.getPolicyCategory());
        entity.setVersionLabel(model.getVersionLabel());
        entity.setContentUrl(model.getContentUrl());
        entity.setContentHash(model.getContentHash());
        entity.setSummaryText(model.getSummaryText());
        entity.setEffectiveDate(model.getEffectiveDate());
        entity.setCurrentState(model.getCurrentState());
        if (model.getStateEntryTime() != null) entity.setStateEntryTime(model.getStateEntryTime());
        if (model.getCreatedTime() != null) entity.setCreatedTime(model.getCreatedTime());
        if (model.getLastModifiedTime() != null) entity.setLastModifiedTime(model.getLastModifiedTime());
        if (model.getVersion() != null) entity.setVersion(model.getVersion());
        if (model.getActivities() != null) {
            for (PlatformPolicyActivity a : model.getActivities()) {
                PlatformPolicyActivityEntity ae = new PlatformPolicyActivityEntity();
                ae.setActivityName(a.getName());
                ae.setActivitySuccess(a.getSuccess());
                ae.setActivityComment(a.getComment());
                entity.getActivities().add(ae);
            }
        }
        return entity;
    }

    /**
     * Merges incoming domain model fields onto an existing JPA entity.
     * Only overwrites non-null fields from the source model, preserving
     * existing values for fields not provided in the update.
     * Used during STM transitions where only a subset of fields may change.
     */
    public void mergeEntity(PlatformPolicy source, PlatformPolicyEntity target) {
        if (source == null || target == null) return;
        if (source.getTitle() != null) target.setTitle(source.getTitle());
        if (source.getPolicyCategory() != null) target.setPolicyCategory(source.getPolicyCategory());
        if (source.getVersionLabel() != null) target.setVersionLabel(source.getVersionLabel());
        if (source.getContentUrl() != null) target.setContentUrl(source.getContentUrl());
        if (source.getContentHash() != null) target.setContentHash(source.getContentHash());
        if (source.getSummaryText() != null) target.setSummaryText(source.getSummaryText());
        if (source.getEffectiveDate() != null) target.setEffectiveDate(source.getEffectiveDate());
        if (source.getCurrentState() != null) target.setCurrentState(source.getCurrentState());
        if (source.getStateEntryTime() != null) target.setStateEntryTime(source.getStateEntryTime());
        if (source.getVersion() != null) target.setVersion(source.getVersion());
        // Activities are managed by JPA cascade — clear and re-add from source
        if (source.getActivities() != null) {
            target.getActivities().clear();
            for (PlatformPolicyActivity a : source.getActivities()) {
                PlatformPolicyActivityEntity ae = new PlatformPolicyActivityEntity();
                ae.setActivityName(a.getName());
                ae.setActivitySuccess(a.getSuccess());
                ae.setActivityComment(a.getComment());
                target.getActivities().add(ae);
            }
        }
    }
}
