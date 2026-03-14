package com.homebase.ecom.policy.infrastructure.mapper;

import com.homebase.ecom.policy.api.dto.PolicyDto;
import com.homebase.ecom.policy.api.dto.RuleDto;
import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.model.Rule;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

public class PolicyDtoMapper {

    public PolicyDto toDto(Policy policy) {
        if (policy == null) return null;

        PolicyDto dto = new PolicyDto();
        dto.setId(policy.getId());
        dto.setName(policy.getName());
        dto.setDescription(policy.getDescription());
        dto.setActive(policy.isActive());
        dto.setDefaultEffect(policy.getDefaultEffect());
        dto.setTargetModule(policy.getTargetModule());
        
        if (policy.getCurrentState() != null) {
            dto.setCurrentState(policy.getCurrentState().getStateId());
        }

        if (policy.getRules() != null) {
            dto.setRules(policy.getRules().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Policy toModel(PolicyDto dto) {
        if (dto == null) return null;

        Policy policy = new Policy();
        policy.setId(dto.getId());
        policy.setName(dto.getName());
        policy.setDescription(dto.getDescription());
        policy.setActive(dto.isActive());
        policy.setDefaultEffect(dto.getDefaultEffect());
        policy.setTargetModule(dto.getTargetModule());

        if (dto.getRules() != null) {
            policy.setRules(dto.getRules().stream()
                    .map(this::toModel)
                    .collect(Collectors.toList()));
        }

        return policy;
    }

    public RuleDto toDto(Rule rule) {
        if (rule == null) return null;

        RuleDto dto = new RuleDto();
        dto.setId(rule.getId());
        dto.setName(rule.getName());
        dto.setExpression(rule.getExpression());
        dto.setEffect(rule.getEffect());
        dto.setPriority(rule.getPriority());

        return dto;
    }

    public Rule toModel(RuleDto dto) {
        if (dto == null) return null;

        Rule rule = new Rule();
        rule.setId(dto.getId());
        rule.setName(dto.getName());
        rule.setExpression(dto.getExpression());
        rule.setEffect(dto.getEffect());
        rule.setPriority(dto.getPriority());

        return rule;
    }
}
