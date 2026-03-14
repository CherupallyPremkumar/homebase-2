package com.homebase.ecom.policy.service.impl;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;
import com.homebase.ecom.policy.api.enums.Effect;
import com.homebase.ecom.policy.api.exception.PolicyNotFoundException;
import com.homebase.ecom.policy.api.service.DecisionService;
import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.domain.repository.DecisionRepository;
import com.homebase.ecom.policy.domain.repository.PolicyRepository;
import com.homebase.ecom.policy.domain.service.RuleEngine;
import com.homebase.ecom.policy.domain.model.Rule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DecisionServiceImpl implements DecisionService {
    private final PolicyRepository policyRepository;
    private final RuleEngine ruleEngine;
    private final DecisionRepository decisionRepository;

    public DecisionServiceImpl(PolicyRepository policyRepository,
            RuleEngine ruleEngine,
            DecisionRepository decisionRepository) {
        this.policyRepository = policyRepository;
        this.ruleEngine = ruleEngine;
        this.decisionRepository = decisionRepository;
    }

    @Override
    public DecisionDto evaluate(EvaluateRequest request) {
        List<Policy> targetPolicies;
        if (request.getPolicyId() != null) {
            Policy policy = policyRepository.findById(request.getPolicyId())
                    .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + request.getPolicyId()));
            targetPolicies = List.of(policy);
        } else if (request.getTargetModule() != null) {
            targetPolicies = policyRepository.findByTargetModuleAndActiveTrue(request.getTargetModule());
            if (targetPolicies.isEmpty()) {
                throw new PolicyNotFoundException("No active policies found for module: " + request.getTargetModule());
            }
        } else {
            throw new IllegalArgumentException("Either policyId or targetModule must be provided");
        }

        Map<String, Object> enrichedContext = request.getFacts() != null ? request.getFacts() : new HashMap<>();

        Effect finalEffect = targetPolicies.get(0).getDefaultEffect();
        String matchedPolicyId = request.getPolicyId() != null ? request.getPolicyId() : targetPolicies.get(0).getId();
        StringBuilder reason = new StringBuilder();

        // Temporary placeholder for any rule matched metadata
        Map<String, String> finalMetadata = new HashMap<>();

        // 2. Evaluate across all candidate policies
        outer: for (Policy policy : targetPolicies) {
            List<Rule> activeRules = policy.getRules().stream()
                    .filter(Rule::isActive)
                    .sorted(Comparator.comparingInt(Rule::getPriority).reversed())
                    .toList();

            for (Rule rule : activeRules) {
                if (ruleEngine.execute(rule, enrichedContext)) {
                    finalEffect = rule.getEffect();
                    matchedPolicyId = policy.getId();
                    reason.append("Matched Rule '").append(rule.getName()).append("' (Priority ")
                            .append(rule.getPriority())
                            .append(") in Policy '").append(policy.getName()).append("'.");
                    // Short-Circuit: The first matching rule (highest priority) dictates the effect
                    break outer;
                }
            }
        }

        DecisionDto decisionDto = new DecisionDto();
        decisionDto.setId("dec-" + UUID.randomUUID().toString().substring(0, 8));
        decisionDto.setPolicyId(matchedPolicyId);
        decisionDto.setTargetModule(request.getTargetModule());
        decisionDto.setSubjectId(request.getSubjectId());
        decisionDto.setResource(request.getResource());
        decisionDto.setAction(request.getAction());
        decisionDto.setEffect(finalEffect);
        decisionDto.setReasons(reason.length() == 0 ? "Default effect applied: " + finalEffect : reason.toString());
        decisionDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z");

        decisionDto.setMetadata(finalMetadata);

        // Save to Audit Log
        com.homebase.ecom.policy.domain.model.Decision decisionDomain = new com.homebase.ecom.policy.domain.model.Decision();
        decisionDomain.setId(decisionDto.getId());
        decisionDomain.setPolicyId(decisionDto.getPolicyId());
        decisionDomain.setSubjectId(decisionDto.getSubjectId());
        decisionDomain.setResource(decisionDto.getResource());
        decisionDomain.setAction(decisionDto.getAction());
        decisionDomain.setEffect(decisionDto.getEffect());
        decisionDomain.setReasons(decisionDto.getReasons());
        decisionDomain.setTargetModule(decisionDto.getTargetModule());
        decisionDomain.setTimestamp(LocalDateTime.now());
        decisionDomain.setMetadata(decisionDto.getMetadata());

        decisionRepository.save(decisionDomain);

        return decisionDto;
    }

    @Override
    public List<DecisionDto> getDecisions() {
        return decisionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<DecisionDto> getDecisionsByPolicyId(String policyId) {
        return decisionRepository.findByPolicyId(policyId).stream().map(this::toDto).toList();
    }

    private DecisionDto toDto(com.homebase.ecom.policy.domain.model.Decision decision) {
        DecisionDto dto = new DecisionDto();
        dto.setId(decision.getId());
        dto.setPolicyId(decision.getPolicyId());
        dto.setSubjectId(decision.getSubjectId());
        dto.setResource(decision.getResource());
        dto.setAction(decision.getAction());
        dto.setEffect(decision.getEffect());
        dto.setReasons(decision.getReasons());
        dto.setTargetModule(decision.getTargetModule());
        dto.setMetadata(decision.getMetadata());
        dto.setTimestamp(
                decision.getTimestamp() != null ? decision.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME) + "Z"
                        : null);
        return dto;
    }
}
