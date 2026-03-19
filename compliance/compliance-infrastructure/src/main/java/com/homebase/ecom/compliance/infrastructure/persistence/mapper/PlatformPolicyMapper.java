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
}
