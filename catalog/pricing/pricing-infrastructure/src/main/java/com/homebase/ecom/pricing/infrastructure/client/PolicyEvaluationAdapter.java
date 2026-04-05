package com.homebase.ecom.pricing.infrastructure.client;

import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.pricing.domain.port.PolicyEvaluationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Infrastructure adapter: calls Rules Engine's DecisionService.evaluateAll()
 * via ProxyBuilder proxy. Maps DecisionDto → PolicyDecision for the domain port.
 *
 * Circuit breaker: if rules-engine is down, gracefully returns empty list
 * (dynamic discounts are skipped, but pricing still works).
 */
public class PolicyEvaluationAdapter implements PolicyEvaluationPort {

    private static final Logger log = LoggerFactory.getLogger(PolicyEvaluationAdapter.class);
    private static final String TARGET_MODULE = "PRICING";

    private final DecisionService decisionService;

    public PolicyEvaluationAdapter(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @Override
    @CircuitBreaker(name = "rulesEngine", fallbackMethod = "fallbackEvaluate")
    public List<PolicyDecision> evaluateDiscountRules(Map<String, Object> facts) {
        EvaluateRequest request = new EvaluateRequest();
        request.setTargetModule(TARGET_MODULE);
        request.setResource("pricing:discount");
        request.setAction("CALCULATE");
        request.setFacts(facts);

        List<DecisionDto> decisions = decisionService.evaluateAll(request);

        return decisions.stream()
                .map(this::toPolicyDecision)
                .toList();
    }

    /**
     * Fallback: rules-engine is unreachable — skip dynamic discounts gracefully.
     */
    @SuppressWarnings("unused")
    private List<PolicyDecision> fallbackEvaluate(Map<String, Object> facts, Throwable t) {
        log.warn("Rules engine unavailable, skipping dynamic discounts: {}", t.getMessage());
        return List.of();
    }

    private PolicyDecision toPolicyDecision(DecisionDto dto) {
        PolicyDecision pd = new PolicyDecision();
        pd.setRuleId(dto.getId());
        pd.setRuleName(dto.getMetadata() != null
                ? dto.getMetadata().getOrDefault("ruleName", dto.getId()) : dto.getId());
        pd.setEffect(dto.getEffect() != null ? dto.getEffect().name() : "DENY");
        pd.setReasons(dto.getReasons());
        pd.setMetadata(dto.getMetadata());
        return pd;
    }
}
