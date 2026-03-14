package com.homebase.ecom.policy.infrastructure.persistence.mapper;

import com.homebase.ecom.policy.domain.model.Decision;
import com.homebase.ecom.policy.infrastructure.persistence.entity.DecisionEntity;

import java.util.HashMap;

public class DecisionMapper {

    public Decision toModel(DecisionEntity entity) {
        if (entity == null) return null;
        Decision model = new Decision();
        model.setId(entity.getId());
        model.setTenant(entity.tenant);
        model.setCreatedTime(entity.getCreatedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        
        model.setPolicyId(entity.getPolicyId());
        model.setSubjectId(entity.getSubjectId());
        model.setResource(entity.getResource());
        model.setAction(entity.getAction());
        model.setEffect(entity.getEffect());
        model.setReasons(entity.getReasons());
        model.setTargetModule(entity.getTargetModule());
        model.setTimestamp(entity.getTimestamp());
        
        if (entity.getMetadata() != null) {
            model.setMetadata(new HashMap<>(entity.getMetadata()));
        }
        return model;
    }

    public DecisionEntity toEntity(Decision model) {
        if (model == null) return null;
        DecisionEntity entity = new DecisionEntity();
        entity.setId(model.getId());
        entity.tenant = model.getTenant();
        entity.setCreatedTime(model.getCreatedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());

        entity.setPolicyId(model.getPolicyId());
        entity.setSubjectId(model.getSubjectId());
        entity.setResource(model.getResource());
        entity.setAction(model.getAction());
        entity.setEffect(model.getEffect());
        entity.setReasons(model.getReasons());
        entity.setTargetModule(model.getTargetModule());
        entity.setTimestamp(model.getTimestamp());
        
        if (model.getMetadata() != null) {
            entity.setMetadata(new HashMap<>(model.getMetadata()));
        }
        return entity;
    }
}
