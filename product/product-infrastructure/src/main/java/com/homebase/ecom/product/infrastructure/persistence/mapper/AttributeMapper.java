package com.homebase.ecom.product.infrastructure.persistence.mapper;

import com.homebase.ecom.product.domain.model.AttributeDefinition;
import com.homebase.ecom.product.domain.model.AttributeOption;
import com.homebase.ecom.product.infrastructure.persistence.entity.AttributeDefinitionEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.AttributeOptionEntity;

import java.util.stream.Collectors;

public class AttributeMapper {

    public AttributeDefinition toModel(AttributeDefinitionEntity entity) {
        if (entity == null) return null;
        AttributeDefinition model = new AttributeDefinition();
        model.setId(entity.getId());
        model.setCode(entity.getCode());
        model.setLabel(entity.getLabel());
        model.setInputType(entity.getInputType());
        model.setFilterable(entity.isFilterable());
        model.setSearchable(entity.isSearchable());
        model.setRequired(entity.isRequired());
        model.setDisplayOrder(entity.getDisplayOrder());
        model.setTenant(entity.tenant);

        if (entity.getOptions() != null) {
            model.setOptions(entity.getOptions().stream()
                .map(this::optionToModel)
                .collect(Collectors.toList()));
        }
        return model;
    }

    public AttributeDefinitionEntity toEntity(AttributeDefinition model) {
        if (model == null) return null;
        AttributeDefinitionEntity entity = new AttributeDefinitionEntity();
        entity.setId(model.getId());
        entity.setCode(model.getCode());
        entity.setLabel(model.getLabel());
        entity.setInputType(model.getInputType());
        entity.setFilterable(model.isFilterable());
        entity.setSearchable(model.isSearchable());
        entity.setRequired(model.isRequired());
        entity.setDisplayOrder(model.getDisplayOrder());
        entity.tenant = model.getTenant();

        if (model.getOptions() != null) {
            entity.setOptions(model.getOptions().stream()
                .map(this::optionToEntity)
                .collect(Collectors.toList()));
        }
        return entity;
    }

    private AttributeOption optionToModel(AttributeOptionEntity entity) {
        AttributeOption model = new AttributeOption();
        model.setId(entity.getId());
        model.setAttributeId(entity.getAttributeId());
        model.setValue(entity.getValue());
        model.setLabel(entity.getLabel());
        model.setColorSwatch(entity.getColorSwatch());
        model.setSortOrder(entity.getSortOrder());
        return model;
    }

    private AttributeOptionEntity optionToEntity(AttributeOption model) {
        AttributeOptionEntity entity = new AttributeOptionEntity();
        entity.setId(model.getId());
        entity.setAttributeId(model.getAttributeId());
        entity.setValue(model.getValue());
        entity.setLabel(model.getLabel());
        entity.setColorSwatch(model.getColorSwatch());
        entity.setSortOrder(model.getSortOrder());
        return entity;
    }
}
