package com.homebase.ecom.compliance.infrastructure.persistence.mapper;

import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.model.AgreementActivity;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.AgreementEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.AgreementActivityEntity;

public class AgreementMapper {

    public Agreement toModel(AgreementEntity entity) {
        if (entity == null) return null;
        Agreement model = new Agreement();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setAgreementType(entity.getAgreementType());
        model.setVersionLabel(entity.getVersionLabel());
        model.setContentUrl(entity.getContentUrl());
        model.setContentHash(entity.getContentHash());
        model.setSupersededById(entity.getSupersededById());
        model.setEffectiveDate(entity.getEffectiveDate());
        model.setExpiryDate(entity.getExpiryDate());
        model.setMandatoryAcceptance(entity.isMandatoryAcceptance());
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());
        if (entity.getActivities() != null) {
            for (AgreementActivityEntity ae : entity.getActivities()) {
                AgreementActivity a = new AgreementActivity();
                a.setName(ae.getActivityName());
                a.setSuccess(ae.isActivitySuccess());
                a.setComment(ae.getActivityComment());
                model.obtainActivities().add(a);
            }
        }
        return model;
    }

    public AgreementEntity toEntity(Agreement model) {
        if (model == null) return null;
        AgreementEntity entity = new AgreementEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setAgreementType(model.getAgreementType());
        entity.setVersionLabel(model.getVersionLabel());
        entity.setContentUrl(model.getContentUrl());
        entity.setContentHash(model.getContentHash());
        entity.setSupersededById(model.getSupersededById());
        entity.setEffectiveDate(model.getEffectiveDate());
        entity.setExpiryDate(model.getExpiryDate());
        entity.setMandatoryAcceptance(model.isMandatoryAcceptance());
        entity.setCurrentState(model.getCurrentState());
        if (model.getStateEntryTime() != null) entity.setStateEntryTime(model.getStateEntryTime());
        if (model.getCreatedTime() != null) entity.setCreatedTime(model.getCreatedTime());
        if (model.getLastModifiedTime() != null) entity.setLastModifiedTime(model.getLastModifiedTime());
        if (model.getVersion() != null) entity.setVersion(model.getVersion());
        if (model.getActivities() != null) {
            for (AgreementActivity a : model.getActivities()) {
                AgreementActivityEntity ae = new AgreementActivityEntity();
                ae.setActivityName(a.getName());
                ae.setActivitySuccess(a.getSuccess());
                ae.setActivityComment(a.getComment());
                entity.getActivities().add(ae);
            }
        }
        return entity;
    }
}
