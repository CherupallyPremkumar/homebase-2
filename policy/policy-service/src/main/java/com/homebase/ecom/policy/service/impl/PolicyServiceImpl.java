package com.homebase.ecom.policy.service.impl;

import com.homebase.ecom.policy.api.dto.PolicyDto;
import com.homebase.ecom.policy.api.dto.RuleDto;
import com.homebase.ecom.policy.api.service.PolicyService;
import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.model.Rule;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import com.homebase.ecom.policy.api.service.FactMetadataService;
import com.homebase.ecom.policy.api.dto.FactDefinitionDto;
import com.homebase.ecom.policy.api.exception.PolicyRuleViolationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PolicyServiceImpl implements PolicyService {
    private final PolicyRepository policyRepository;
    private final StateEntityServiceImpl<Policy> policyStateEntityService;
    private final FactMetadataService factMetadataService;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public PolicyServiceImpl(PolicyRepository policyRepository, 
                             StateEntityServiceImpl<Policy> policyStateEntityService,
                             FactMetadataService factMetadataService) {
        this.policyRepository = policyRepository;
        this.policyStateEntityService = policyStateEntityService;
        this.factMetadataService = factMetadataService;
    }

    @Override
    @Transactional
    public PolicyDto createPolicy(PolicyDto policyDto) {
        validateRules(policyDto.getRules());
        Policy policy = new Policy();
        policy.setId(policyDto.getId());
        updatePolicyFromDto(policy, policyDto);
        Policy saved = policyRepository.save(policy);
        return mapToDto(saved);
    }

    @Override
    public PolicyDto getPolicy(String id) {
        return policyRepository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public List<PolicyDto> listPolicies() {
        return policyRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PolicyDto updatePolicy(String id, PolicyDto policyDto) {
        validateRules(policyDto.getRules());
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        updatePolicyFromDto(policy, policyDto);
        return mapToDto(policyRepository.save(policy));
    }

    @Override
    public void deletePolicy(String id) {
        policyRepository.deleteById(id);
    }

    @Override
    public PolicyDto submitPolicy(String id) {
        Policy policy = policyStateEntityService.processById(id, "submit", null).getMutatedEntity();
        return mapToDto(policy);
    }

    @Override
    public PolicyDto approvePolicy(String id) {
        Policy policy = policyStateEntityService.processById(id, "approve", null).getMutatedEntity();
        return mapToDto(policy);
    }

    @Override
    public PolicyDto activatePolicy(String id) {
        Policy policy = policyStateEntityService.processById(id, "activate", null).getMutatedEntity();
        return mapToDto(policy);
    }

    @Override
    public PolicyDto deprecatePolicy(String id) {
        Policy policy = policyStateEntityService.processById(id, "deprecate", null).getMutatedEntity();
        return mapToDto(policy);
    }

    private void updatePolicyFromDto(Policy policy, PolicyDto dto) {
        policy.setName(dto.getName());
        policy.setDescription(dto.getDescription());
        policy.setActive(dto.isActive());
        if (dto.getRules() != null) {
            policy.getRules().clear();
            policy.getRules().addAll(dto.getRules().stream().map(this::mapToEntity).collect(Collectors.toList()));
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

    private PolicyDto mapToDto(Policy entity) {
        PolicyDto dto = new PolicyDto();
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
                throw new PolicyRuleViolationException("Rule expression cannot be empty");
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
                            throw new PolicyRuleViolationException("Invalid fact attribute used in rule: " + token + ". Valid facts are: " + validAttributes);
                        }
                    }
                }
            } catch (Exception e) {
                if (e instanceof PolicyRuleViolationException) throw e;
                throw new PolicyRuleViolationException("Invalid SpEL expression in rule '" + rule.getName() + "': " + e.getMessage());
            }
        }
    }
}
