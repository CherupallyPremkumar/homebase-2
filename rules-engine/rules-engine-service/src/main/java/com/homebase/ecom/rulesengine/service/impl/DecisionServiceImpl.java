package com.homebase.ecom.rulesengine.service.impl;

import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import com.homebase.ecom.rulesengine.api.enums.Effect;
import com.homebase.ecom.rulesengine.api.exception.RuleSetNotFoundException;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.repository.DecisionRepository;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import com.homebase.ecom.rulesengine.domain.service.RuleEngine;
import com.homebase.ecom.rulesengine.domain.model.Rule;

import org.chenile.core.context.ContextContainer;
import org.chenile.core.context.HeaderUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DecisionServiceImpl implements DecisionService {
    private final RuleSetRepository ruleSetRepository;
    private final RuleEngine ruleEngine;
    private final DecisionRepository decisionRepository;
    private final ContextContainer contextContainer;

    public DecisionServiceImpl(RuleSetRepository ruleSetRepository,
            RuleEngine ruleEngine,
            DecisionRepository decisionRepository,
            ContextContainer contextContainer) {
        this.ruleSetRepository = ruleSetRepository;
        this.ruleEngine = ruleEngine;
        this.decisionRepository = decisionRepository;
        this.contextContainer = contextContainer;
    }

    @Override
    public DecisionDto evaluate(EvaluateRequest request) {
        List<RuleSet> targetRuleSets = loadRuleSets(request);
        if (targetRuleSets.isEmpty()) {
            throw new RuleSetNotFoundException("No active rule sets found for module: " + request.getTargetModule());
        }

        Map<String, Object> enrichedContext = request.getFacts() != null ? request.getFacts() : new HashMap<>();

        Effect finalEffect = targetRuleSets.get(0).getDefaultEffect();
        String matchedRuleSetId = request.getRuleSetId() != null ? request.getRuleSetId() : targetRuleSets.get(0).getId();
        StringBuilder reason = new StringBuilder();

        Map<String, String> finalMetadata = new HashMap<>();

        outer: for (RuleSet ruleSet : targetRuleSets) {
            List<Rule> activeRules = ruleSet.getRules().stream()
                    .filter(Rule::isActive)
                    .sorted(Comparator.comparingInt(Rule::getPriority).reversed())
                    .toList();

            for (Rule rule : activeRules) {
                if (ruleEngine.execute(rule, enrichedContext)) {
                    finalEffect = rule.getEffect();
                    matchedRuleSetId = ruleSet.getId();
                    finalMetadata = rule.getMetadata();
                    reason.append("Matched Rule '").append(rule.getName()).append("' (Priority ")
                            .append(rule.getPriority())
                            .append(") in RuleSet '").append(ruleSet.getName()).append("'.");
                    break outer;
                }
            }
        }

        DecisionDto decisionDto = new DecisionDto();
        decisionDto.setId("dec-" + UUID.randomUUID().toString().substring(0, 8));
        decisionDto.setRuleSetId(matchedRuleSetId);
        decisionDto.setTargetModule(request.getTargetModule());
        decisionDto.setSubjectId(request.getSubjectId());
        decisionDto.setResource(request.getResource());
        decisionDto.setAction(request.getAction());
        decisionDto.setEffect(finalEffect);
        decisionDto.setReasons(reason.length() == 0 ? "Default effect applied: " + finalEffect : reason.toString());
        decisionDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z");

        decisionDto.setMetadata(finalMetadata);

        // Save to Audit Log
        com.homebase.ecom.rulesengine.domain.model.Decision decisionDomain = new com.homebase.ecom.rulesengine.domain.model.Decision();
        decisionDomain.setId(decisionDto.getId());
        decisionDomain.setRuleSetId(decisionDto.getRuleSetId());
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
    public List<DecisionDto> evaluateAll(EvaluateRequest request) {
        List<RuleSet> targetRuleSets = loadRuleSets(request);
        Map<String, Object> enrichedContext = request.getFacts() != null ? request.getFacts() : new HashMap<>();
        List<DecisionDto> decisions = new ArrayList<>();

        for (RuleSet ruleSet : targetRuleSets) {
            List<Rule> activeRules = ruleSet.getRules().stream()
                    .filter(Rule::isActive)
                    .sorted(Comparator.comparingInt(Rule::getPriority).reversed())
                    .toList();

            for (Rule rule : activeRules) {
                if (ruleEngine.execute(rule, enrichedContext)) {
                    DecisionDto decision = new DecisionDto();
                    decision.setId("dec-" + UUID.randomUUID().toString().substring(0, 8));
                    decision.setRuleSetId(ruleSet.getId());
                    decision.setTargetModule(request.getTargetModule());
                    decision.setSubjectId(request.getSubjectId());
                    decision.setResource(request.getResource());
                    decision.setAction(request.getAction());
                    decision.setEffect(rule.getEffect());
                    decision.setReasons("Matched Rule '" + rule.getName() + "' (Priority "
                            + rule.getPriority() + ") in RuleSet '" + ruleSet.getName() + "'.");
                    decision.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z");
                    decision.setMetadata(rule.getMetadata());
                    decisions.add(decision);

                    // Save audit log
                    com.homebase.ecom.rulesengine.domain.model.Decision decisionDomain = new com.homebase.ecom.rulesengine.domain.model.Decision();
                    decisionDomain.setId(decision.getId());
                    decisionDomain.setRuleSetId(decision.getRuleSetId());
                    decisionDomain.setSubjectId(decision.getSubjectId());
                    decisionDomain.setResource(decision.getResource());
                    decisionDomain.setAction(decision.getAction());
                    decisionDomain.setEffect(decision.getEffect());
                    decisionDomain.setReasons(decision.getReasons());
                    decisionDomain.setTargetModule(decision.getTargetModule());
                    decisionDomain.setTimestamp(LocalDateTime.now());
                    decisionDomain.setMetadata(decision.getMetadata());
                    decisionRepository.save(decisionDomain);
                }
            }
        }

        return decisions;
    }

    private List<RuleSet> loadRuleSets(EvaluateRequest request) {
        String tenant = contextContainer.getTenant();
        if (request.getRuleSetId() != null) {
            RuleSet ruleSet = ruleSetRepository.findById(request.getRuleSetId())
                    .orElseThrow(() -> new RuleSetNotFoundException("RuleSet not found: " + request.getRuleSetId()));
            return List.of(ruleSet);
        } else if (request.getTargetModule() != null) {
            List<RuleSet> ruleSets;
            if (tenant != null && !tenant.isBlank()) {
                ruleSets = ruleSetRepository.findByTargetModuleAndActiveTrueAndTenant(request.getTargetModule(), tenant);
            } else {
                ruleSets = ruleSetRepository.findByTargetModuleAndActiveTrue(request.getTargetModule());
            }
            if (ruleSets.isEmpty()) {
                return List.of();
            }
            return ruleSets;
        }
        throw new IllegalArgumentException("Either ruleSetId or targetModule must be provided");
    }

    @Override
    public List<DecisionDto> getDecisions() {
        return decisionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<DecisionDto> getDecisionsByRuleSetId(String ruleSetId) {
        return decisionRepository.findByRuleSetId(ruleSetId).stream().map(this::toDto).toList();
    }

    private DecisionDto toDto(com.homebase.ecom.rulesengine.domain.model.Decision decision) {
        DecisionDto dto = new DecisionDto();
        dto.setId(decision.getId());
        dto.setRuleSetId(decision.getRuleSetId());
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
