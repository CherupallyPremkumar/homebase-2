package com.homebase.ecom.policy.infrastructure.persistence.mapper;

import com.homebase.ecom.policy.domain.model.FactDefinition;
import com.homebase.ecom.policy.infrastructure.persistence.entity.FactDefinitionEntity;
import java.util.UUID;

public class FactDefinitionMapper {
    public FactDefinition toModel(FactDefinitionEntity entity) {
        if (entity == null) return null;
        FactDefinition model = new FactDefinition();
        model.setId(entity.getId());
        model.setModule(entity.getModule());
        model.setEntityName(entity.getEntityName());
        model.setDisplayName(entity.getDisplayName());
        model.setAttribute(entity.getAttribute());
        model.setDataType(entity.getDataType());
        return model;
    }

    public FactDefinitionEntity toEntity(FactDefinition model) {
        if (model == null) return null;
        FactDefinitionEntity entity = new FactDefinitionEntity();
        if (model.getId() == null || model.getId().isEmpty()) {
            entity.setId("fact_" + UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setModule(model.getModule());
        entity.setEntityName(model.getEntityName());
        entity.setDisplayName(model.getDisplayName());
        entity.setAttribute(model.getAttribute());
        entity.setDataType(model.getDataType());
        return entity;
    }
}
