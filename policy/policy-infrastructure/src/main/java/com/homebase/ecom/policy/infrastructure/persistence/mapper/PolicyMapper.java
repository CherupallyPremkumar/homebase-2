package com.homebase.ecom.policy.infrastructure.persistence.mapper;

import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.model.Rule;
import com.homebase.ecom.policy.infrastructure.persistence.entity.PolicyEntity;
import com.homebase.ecom.policy.infrastructure.persistence.entity.RuleEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

public class PolicyMapper {

    public Policy toModel(PolicyEntity entity) {
        if (entity == null) return null;

        Policy model = new Policy();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDefaultEffect(entity.getDefaultEffect());
        model.setActive(entity.isActive());
        model.setTargetModule(entity.getTargetModule());
        // State is managed by AbstractJpaStateEntity and AbstractExtendedStateEntity
        
        if (entity.getRules() != null) {
            model.setRules(entity.getRules().stream()
                    .map(this::toModel)
                    .collect(Collectors.toList()));
        }

        return model;
    }

    public PolicyEntity toEntity(Policy model) {
        if (model == null) return null;

        PolicyEntity entity = new PolicyEntity();
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

        if (model.getRules() != null) {
            entity.setRules(model.getRules().stream()
                    .map(r -> {
                        RuleEntity re = toEntity(r);
                        re.setPolicy(entity);
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
        entity.setName(model.getName());
        entity.setExpression(model.getExpression());
        entity.setEffect(model.getEffect());
        entity.setPriority(model.getPriority());

        return entity;
    }
}
