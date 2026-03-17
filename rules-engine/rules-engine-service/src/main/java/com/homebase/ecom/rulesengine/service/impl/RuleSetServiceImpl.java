package com.homebase.ecom.rulesengine.service.impl;

import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import com.homebase.ecom.rulesengine.api.dto.RuleDto;
import com.homebase.ecom.rulesengine.api.service.RuleSetService;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import com.homebase.ecom.rulesengine.api.service.FactMetadataService;
import com.homebase.ecom.rulesengine.api.dto.FactDefinitionDto;
import com.homebase.ecom.rulesengine.api.exception.RuleViolationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleSetServiceImpl implements RuleSetService {
    private final RuleSetRepository ruleSetRepository;
    private final HmStateEntityServiceImpl<RuleSet> ruleSetStateEntityService;
    private final FactMetadataService factMetadataService;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public RuleSetServiceImpl(RuleSetRepository ruleSetRepository,
                              HmStateEntityServiceImpl<RuleSet> ruleSetStateEntityService,
                              FactMetadataService factMetadataService) {
        this.ruleSetRepository = ruleSetRepository;
        this.ruleSetStateEntityService = ruleSetStateEntityService;
        this.factMetadataService = factMetadataService;
    }

    @Override
    @Transactional
    public RuleSetDto createRuleSet(RuleSetDto ruleSetDto) {
        validateRules(ruleSetDto.getRules());
        RuleSet ruleSet = new RuleSet();
        ruleSet.setId(ruleSetDto.getId());
        updateRuleSetFromDto(ruleSet, ruleSetDto);
        RuleSet saved = ruleSetRepository.save(ruleSet);
        return mapToDto(saved);
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
    @Transactional
    public RuleSetDto updateRuleSet(String id, RuleSetDto ruleSetDto) {
        validateRules(ruleSetDto.getRules());
        RuleSet ruleSet = ruleSetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RuleSet not found"));
        updateRuleSetFromDto(ruleSet, ruleSetDto);
        return mapToDto(ruleSetRepository.save(ruleSet));
    }

    @Override
    public void deleteRuleSet(String id) {
        ruleSetRepository.deleteById(id);
    }

    @Override
    public RuleSetDto submitRuleSet(String id) {
        RuleSet ruleSet = ruleSetStateEntityService.processById(id, "submit", null).getMutatedEntity();
        return mapToDto(ruleSet);
    }

    @Override
    public RuleSetDto approveRuleSet(String id) {
        RuleSet ruleSet = ruleSetStateEntityService.processById(id, "approve", null).getMutatedEntity();
        return mapToDto(ruleSet);
    }

    @Override
    public RuleSetDto activateRuleSet(String id) {
        RuleSet ruleSet = ruleSetStateEntityService.processById(id, "activate", null).getMutatedEntity();
        return mapToDto(ruleSet);
    }

    @Override
    public RuleSetDto deprecateRuleSet(String id) {
        RuleSet ruleSet = ruleSetStateEntityService.processById(id, "deprecate", null).getMutatedEntity();
        return mapToDto(ruleSet);
    }

    private void updateRuleSetFromDto(RuleSet ruleSet, RuleSetDto dto) {
        ruleSet.setName(dto.getName());
        ruleSet.setDescription(dto.getDescription());
        ruleSet.setActive(dto.isActive());
        if (dto.getRules() != null) {
            ruleSet.getRules().clear();
            ruleSet.getRules().addAll(dto.getRules().stream().map(this::mapToEntity).collect(Collectors.toList()));
        }
    }

    private Rule mapToEntity(RuleDto dto) {
        Rule rule = new Rule();
        rule.setId(dto.getId());
        rule.setName(dto.getName());
        rule.setExpression(dto.getExpression());
        rule.setEffect(dto.getEffect());
        rule.setPriority(dto.getPriority());
        return rule;
    }

    private RuleSetDto mapToDto(RuleSet entity) {
        RuleSetDto dto = new RuleSetDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setDefaultEffect(entity.getDefaultEffect());
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
        return dto;
    }

    private void validateRules(List<RuleDto> rules) {
        if (rules == null || rules.isEmpty()) return;

        Set<String> validAttributes = factMetadataService.getAllFacts().stream()
                .map(FactDefinitionDto::getAttribute)
                .map(attr -> attr.startsWith("#") ? attr.substring(1) : attr)
                .collect(Collectors.toSet());

        for (RuleDto rule : rules) {
            if (rule.getExpression() == null || rule.getExpression().trim().isEmpty()) {
                throw new RuleViolationException("Rule expression cannot be empty");
            }
            try {
                // Parse to ensure SpEL syntax is valid
                expressionParser.parseExpression(rule.getExpression());

                // Simple validation: check if words starting with # match our valid facts
                String[] tokens = rule.getExpression().split("[^a-zA-Z0-9_#\\.]+");
                for (String token : tokens) {
                    if (token.startsWith("#")) {
                        String variableName = token.substring(1);
                        if (!validAttributes.contains(variableName)) {
                            throw new RuleViolationException("Invalid fact attribute used in rule: " + token + ". Valid facts are: " + validAttributes);
                        }
                    }
                }
            } catch (Exception e) {
                if (e instanceof RuleViolationException) throw e;
                throw new RuleViolationException("Invalid SpEL expression in rule '" + rule.getName() + "': " + e.getMessage());
            }
        }
    }
}
