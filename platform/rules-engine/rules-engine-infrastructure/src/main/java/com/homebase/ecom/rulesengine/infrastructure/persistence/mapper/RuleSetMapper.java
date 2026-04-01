package com.homebase.ecom.rulesengine.infrastructure.persistence.mapper;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.RuleSetEntity;
import com.homebase.ecom.rulesengine.infrastructure.persistence.entity.RuleEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

public class RuleSetMapper {

    public RuleSet toModel(RuleSetEntity entity) {
        if (entity == null) return null;

        RuleSet model = new RuleSet();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDefaultEffect(entity.getDefaultEffect());
        model.setActive(entity.isActive());
        model.setTargetModule(entity.getTargetModule());
        model.setTenant(entity.tenant);
        // STM and base entity fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());

        if (entity.getRules() != null) {
            model.setRules(entity.getRules().stream()
                    .map(this::toModel)
                    .collect(Collectors.toList()));
        }

        return model;
    }

    public RuleSetEntity toEntity(RuleSet model) {
        if (model == null) return null;

        RuleSetEntity entity = new RuleSetEntity();
        if (model.getId() == null || model.getId().trim().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setDefaultEffect(model.getDefaultEffect());
        entity.setActive(model.isActive());
        entity.setTargetModule(model.getTargetModule());
        entity.tenant = model.getTenant();
        // STM and base entity fields
        entity.setCurrentState(model.getCurrentState());
        if (model.getStateEntryTime() != null) entity.setStateEntryTime(model.getStateEntryTime());
        if (model.getCreatedTime() != null) entity.setCreatedTime(model.getCreatedTime());
        if (model.getLastModifiedTime() != null) entity.setLastModifiedTime(model.getLastModifiedTime());
        if (model.getVersion() != null) entity.setVersion(model.getVersion());

        if (model.getRules() != null) {
            entity.setRules(model.getRules().stream()
                    .map(r -> {
                        RuleEntity re = toEntity(r);
                        re.setRuleSet(entity);
                        return re;
                    })
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    public Rule toModel(RuleEntity entity) {
        if (entity == null) return null;

        Rule model = new Rule();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setExpression(entity.getExpression());
        model.setEffect(entity.getEffect());
        model.setPriority(entity.getPriority());
        model.setMetadata(entity.getMetadata());
        model.setTenant(entity.tenant);
        model.setVersion(entity.getVersion());

        return model;
    }

    public RuleEntity toEntity(Rule model) {
        if (model == null) return null;

        RuleEntity entity = new RuleEntity();
        if (model.getId() == null || model.getId().trim().isEmpty() || model.getId().startsWith("temp_")) {
            entity.setId(UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        if (model.getVersion() != null) entity.setVersion(model.getVersion());
        entity.setName(model.getName());
        entity.setExpression(model.getExpression());
        entity.setEffect(model.getEffect());
        entity.setPriority(model.getPriority());
        entity.setMetadata(model.getMetadata());
        entity.tenant = model.getTenant();

        return entity;
    }
}
