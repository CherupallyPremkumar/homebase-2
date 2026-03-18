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
import com.homebase.ecom.rulesengine.service.cache.RuleSetCacheManager;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import org.chenile.core.context.ContextContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class DecisionServiceImpl implements DecisionService {
    private static final Logger log = LoggerFactory.getLogger(DecisionServiceImpl.class);

    private final RuleSetRepository ruleSetRepository;
    private final RuleEngine ruleEngine;
    private final DecisionRepository decisionRepository;
    private final ContextContainer contextContainer;
    private final RuleSetCacheManager cacheManager;
    private final CircuitBreaker circuitBreaker;

    public DecisionServiceImpl(RuleSetRepository ruleSetRepository,
            RuleEngine ruleEngine,
            DecisionRepository decisionRepository,
            ContextContainer contextContainer,
            RuleSetCacheManager cacheManager) {
        this.ruleSetRepository = ruleSetRepository;
        this.ruleEngine = ruleEngine;
        this.decisionRepository = decisionRepository;
        this.contextContainer = contextContainer;
        this.cacheManager = cacheManager;

        // Programmatic circuit breaker — opens after 5 failures in 10 calls, half-open after 30s
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .minimumNumberOfCalls(10)
                .slidingWindowSize(10)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        this.circuitBreaker = registry.circuitBreaker("rulesEngine");
    }

    @Override
    public DecisionDto evaluate(EvaluateRequest request) {
        Supplier<DecisionDto> decorated = CircuitBreaker.decorateSupplier(circuitBreaker, () -> doEvaluate(request));
        try {
            return decorated.get();
        } catch (Exception e) {
            log.warn("Circuit breaker triggered for evaluate, returning default effect", e);
            return buildDefaultDecision(request);
        }
    }

    @Override
    public List<DecisionDto> evaluateAll(EvaluateRequest request) {
        Supplier<List<DecisionDto>> decorated = CircuitBreaker.decorateSupplier(circuitBreaker, () -> doEvaluateAll(request));
        try {
            return decorated.get();
        } catch (Exception e) {
            log.warn("Circuit breaker triggered for evaluateAll, returning empty list", e);
            return List.of();
        }
    }

    private DecisionDto doEvaluate(EvaluateRequest request) {
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

        DecisionDto decisionDto = buildDecisionDto(request, matchedRuleSetId, finalEffect, finalMetadata, reason.toString());

        saveAuditLog(decisionDto);

        return decisionDto;
    }

    private List<DecisionDto> doEvaluateAll(EvaluateRequest request) {
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
                    String reasons = "Matched Rule '" + rule.getName() + "' (Priority "
                            + rule.getPriority() + ") in RuleSet '" + ruleSet.getName() + "'.";
                    DecisionDto decision = buildDecisionDto(request, ruleSet.getId(), rule.getEffect(), rule.getMetadata(), reasons);
                    decisions.add(decision);
                    saveAuditLog(decision);
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
            // Check cache first
            String cacheKey = RuleSetCacheManager.cacheKey(request.getTargetModule(), tenant);
            List<RuleSet> cached = cacheManager.get(cacheKey);
            if (cached != null) {
                return cached;
            }

            List<RuleSet> ruleSets;
            if (tenant != null && !tenant.isBlank()) {
                ruleSets = ruleSetRepository.findByTargetModuleAndActiveTrueAndTenant(request.getTargetModule(), tenant);
            } else {
                ruleSets = ruleSetRepository.findByTargetModuleAndActiveTrue(request.getTargetModule());
            }

            // Cache the result (even if empty)
            cacheManager.put(cacheKey, ruleSets);

            if (ruleSets.isEmpty()) {
                return List.of();
            }
            return ruleSets;
        }
        throw new IllegalArgumentException("Either ruleSetId or targetModule must be provided");
    }

    private DecisionDto buildDecisionDto(EvaluateRequest request, String ruleSetId, Effect effect,
            Map<String, String> metadata, String reasons) {
        DecisionDto dto = new DecisionDto();
        dto.setId("dec-" + UUID.randomUUID().toString().substring(0, 8));
        dto.setRuleSetId(ruleSetId);
        dto.setTargetModule(request.getTargetModule());
        dto.setSubjectId(request.getSubjectId());
        dto.setResource(request.getResource());
        dto.setAction(request.getAction());
        dto.setEffect(effect);
        dto.setReasons(reasons.isEmpty() ? "Default effect applied: " + effect : reasons);
        dto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z");
        dto.setMetadata(metadata);
        return dto;
    }

    private DecisionDto buildDefaultDecision(EvaluateRequest request) {
        DecisionDto dto = new DecisionDto();
        dto.setId("dec-" + UUID.randomUUID().toString().substring(0, 8));
        dto.setRuleSetId("fallback");
        dto.setTargetModule(request.getTargetModule());
        dto.setSubjectId(request.getSubjectId());
        dto.setResource(request.getResource());
        dto.setAction(request.getAction());
        dto.setEffect(Effect.DENY);
        dto.setReasons("Circuit breaker open — default DENY applied");
        dto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z");
        dto.setMetadata(new HashMap<>());
        return dto;
    }

    private void saveAuditLog(DecisionDto decisionDto) {
        try {
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
        } catch (Exception e) {
            log.error("Failed to save audit log for decision: {}", decisionDto.getId(), e);
        }
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
