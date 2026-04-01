package com.homebase.ecom.rulesengine.service.impl;

import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import com.homebase.ecom.rulesengine.api.dto.RuleDto;
import com.homebase.ecom.rulesengine.api.service.RuleSetService;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Read-only service for RuleSet.
 * All mutations go through the controller → STM processById() path.
 */
public class RuleSetServiceImpl implements RuleSetService {
    private final RuleSetRepository ruleSetRepository;

    public RuleSetServiceImpl(RuleSetRepository ruleSetRepository) {
        this.ruleSetRepository = ruleSetRepository;
    }

    @Override
    public RuleSetDto getRuleSet(String id) {
        return ruleSetRepository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public List<RuleSetDto> listRuleSets() {
        return ruleSetRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<RuleSetDto> findByTargetModule(String targetModule) {
        return ruleSetRepository.findByTargetModuleAndActiveTrue(targetModule).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    private RuleSetDto mapToDto(RuleSet entity) {
        RuleSetDto dto = new RuleSetDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setDefaultEffect(entity.getDefaultEffect());
        dto.setTargetModule(entity.getTargetModule());
        if (entity.getCurrentState() != null) {
            dto.setCurrentState(entity.getCurrentState().getStateId());
        }
        if (entity.getRules() != null) {
            dto.setRules(entity.getRules().stream().map(this::mapRuleToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    private RuleDto mapRuleToDto(Rule entity) {
        RuleDto dto = new RuleDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setExpression(entity.getExpression());
        dto.setEffect(entity.getEffect());
        dto.setPriority(entity.getPriority());
        dto.setActive(entity.isActive());
        return dto;
    }
}
