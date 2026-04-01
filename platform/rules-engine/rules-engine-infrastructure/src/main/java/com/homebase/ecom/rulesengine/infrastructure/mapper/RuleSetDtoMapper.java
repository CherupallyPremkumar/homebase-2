package com.homebase.ecom.rulesengine.infrastructure.mapper;

import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import com.homebase.ecom.rulesengine.api.dto.RuleDto;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.model.Rule;

import java.util.stream.Collectors;

public class RuleSetDtoMapper {

    public RuleSetDto toDto(RuleSet ruleSet) {
        if (ruleSet == null) return null;

        RuleSetDto dto = new RuleSetDto();
        dto.setId(ruleSet.getId());
        dto.setName(ruleSet.getName());
        dto.setDescription(ruleSet.getDescription());
        dto.setActive(ruleSet.isActive());
        dto.setDefaultEffect(ruleSet.getDefaultEffect());
        dto.setTargetModule(ruleSet.getTargetModule());

        if (ruleSet.getCurrentState() != null) {
            dto.setCurrentState(ruleSet.getCurrentState().getStateId());
        }

        if (ruleSet.getRules() != null) {
            dto.setRules(ruleSet.getRules().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public RuleSet toModel(RuleSetDto dto) {
        if (dto == null) return null;

        RuleSet ruleSet = new RuleSet();
        ruleSet.setId(dto.getId());
        ruleSet.setName(dto.getName());
        ruleSet.setDescription(dto.getDescription());
        ruleSet.setActive(dto.isActive());
        ruleSet.setDefaultEffect(dto.getDefaultEffect());
        ruleSet.setTargetModule(dto.getTargetModule());

        if (dto.getRules() != null) {
            ruleSet.setRules(dto.getRules().stream()
                    .map(this::toModel)
                    .collect(Collectors.toList()));
        }

        return ruleSet;
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
