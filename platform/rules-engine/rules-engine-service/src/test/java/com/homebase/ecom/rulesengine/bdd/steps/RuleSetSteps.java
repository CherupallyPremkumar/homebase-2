package com.homebase.ecom.rulesengine.bdd.steps;

import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.enums.Effect;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RuleSetSteps {

    @Autowired
    private RuleSetRepository ruleSetRepository;

    @Autowired
    private DecisionService decisionService;

    private void ensureTenantContext() {
        org.chenile.core.context.ContextContainer ctx = org.chenile.core.context.ContextContainer.CONTEXT_CONTAINER;
        if (ctx.get(org.chenile.core.context.HeaderUtils.TENANT_ID_KEY) == null) {
            ctx.put(org.chenile.core.context.HeaderUtils.TENANT_ID_KEY, "homebase");
        }
    }

    private DecisionDto lastDecision;
    private List<DecisionDto> lastDecisions;

    // ═══════════════════════════════════════════════════════════════
    // GIVEN: Create inline policies (for evaluation tests — bypasses STM)
    // ═══════════════════════════════════════════════════════════════

    @Given("a policy {string} exists with default effect {string}")
    public void aPolicyExistsWithDefaultEffect(String id, String effect) {
        ensureTenantContext();
        RuleSet ruleSet = new RuleSet();
        ruleSet.setId(id);
        ruleSet.setName(id);
        ruleSet.setActive(true);
        ruleSet.setDefaultEffect(Effect.valueOf(effect));
        ruleSet.setTargetModule("TEST");
        ruleSetRepository.save(ruleSet);
    }

    @Given("rule {string} with expression {string} and effect {string} is added to {string}")
    public void ruleWithExpressionAndEffectIsAddedTo(String ruleId, String expression, String effect, String ruleSetId) {
        ensureTenantContext();
        RuleSet ruleSet = ruleSetRepository.findById(ruleSetId)
                .orElseThrow(() -> new RuntimeException("RuleSet not found: " + ruleSetId));
        Rule rule = new Rule();
        rule.setId(ruleId);
        rule.setName(ruleId);
        rule.setExpression(expression);
        rule.setEffect(Effect.valueOf(effect));
        rule.setPriority(100 - ruleSet.getRules().size());
        rule.setActive(true);
        ruleSet.getRules().add(rule);
        ruleSetRepository.save(ruleSet);
    }

    // ═══════════════════════════════════════════════════════════════
    // WHEN: Evaluate by ruleSetId
    // ═══════════════════════════════════════════════════════════════

    @When("I evaluate policy {string} with context:")
    public void iEvaluatePolicyWithContext(String ruleSetId, Map<String, String> contextData) {
        Map<String, Object> data = new HashMap<>(contextData);
        EvaluateRequest request = new EvaluateRequest();
        request.setRuleSetId(ruleSetId);
        request.setSubjectId(contextData.get("userId"));
        request.setFacts(data);
        lastDecision = decisionService.evaluate(request);
    }

    // ═══════════════════════════════════════════════════════════════
    // WHEN: Evaluate by targetModule
    // ═══════════════════════════════════════════════════════════════

    @When("I evaluate rules for module {string} with facts:")
    public void iEvaluateRulesForModuleWithFacts(String targetModule, Map<String, String> rawFacts) {
        Map<String, Object> facts = parseTypedFacts(rawFacts);
        EvaluateRequest request = new EvaluateRequest();
        request.setTargetModule(targetModule);
        request.setFacts(facts);
        lastDecision = decisionService.evaluate(request);
        lastDecisions = null;
    }

    @When("I evaluate all rules for module {string} with facts:")
    public void iEvaluateAllRulesForModuleWithFacts(String targetModule, Map<String, String> rawFacts) {
        Map<String, Object> facts = parseTypedFacts(rawFacts);
        EvaluateRequest request = new EvaluateRequest();
        request.setTargetModule(targetModule);
        request.setFacts(facts);
        lastDecisions = decisionService.evaluateAll(request);
        lastDecision = lastDecisions.isEmpty() ? null : lastDecisions.get(0);
    }

    // ═══════════════════════════════════════════════════════════════
    // THEN: Assert on decision
    // ═══════════════════════════════════════════════════════════════

    @Then("the decision effect is {string}")
    public void theDecisionEffectIs(String expected) {
        assertNotNull("Decision was null", lastDecision);
        assertEquals(Effect.valueOf(expected), lastDecision.getEffect());
    }

    @Then("the decision metadata {string} is {string}")
    public void theDecisionMetadataIs(String key, String expectedValue) {
        assertNotNull("Decision was null", lastDecision);
        assertNotNull("Metadata was null", lastDecision.getMetadata());
        assertEquals(expectedValue, lastDecision.getMetadata().get(key));
    }

    @Then("at least one decision has effect {string}")
    public void atLeastOneDecisionHasEffect(String expected) {
        if (lastDecisions != null && !lastDecisions.isEmpty()) {
            boolean found = lastDecisions.stream()
                    .anyMatch(d -> d.getEffect() == Effect.valueOf(expected));
            assertTrue("No decision with effect " + expected, found);
        } else {
            assertNotNull("Decision was null", lastDecision);
            assertEquals(Effect.valueOf(expected), lastDecision.getEffect());
        }
    }

    @Then("at least one decision has metadata {string} equal to {string}")
    public void atLeastOneDecisionHasMetadata(String key, String expectedValue) {
        assertNotNull("Decisions list was null", lastDecisions);
        assertFalse("Decisions list was empty", lastDecisions.isEmpty());
        boolean found = lastDecisions.stream()
                .anyMatch(d -> d.getMetadata() != null
                        && expectedValue.equals(d.getMetadata().get(key)));
        assertTrue("No decision with metadata " + key + "=" + expectedValue, found);
    }

    @Then("the decision should be {string}")
    public void theDecisionShouldBe(String expected) {
        assertNotNull("Decision was null", lastDecision);
        Effect expectedEffect = expected.equalsIgnoreCase("ALLOWED") ? Effect.ALLOW : Effect.DENY;
        assertEquals(expectedEffect, lastDecision.getEffect());
    }

    @Then("the reason should contain {string}")
    public void theReasonShouldContain(String expectedPart) {
        assertTrue(lastDecision.getReasons() != null && lastDecision.getReasons().contains(expectedPart));
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER
    // ═══════════════════════════════════════════════════════════════

    private Map<String, Object> parseTypedFacts(Map<String, String> rawFacts) {
        Map<String, Object> typed = new HashMap<>();
        for (Map.Entry<String, String> entry : rawFacts.entrySet()) {
            typed.put(entry.getKey(), parseValue(entry.getValue()));
        }
        return typed;
    }

    private Object parseValue(String value) {
        if (value == null) return null;
        if ("true".equalsIgnoreCase(value)) return Boolean.TRUE;
        if ("false".equalsIgnoreCase(value)) return Boolean.FALSE;
        try {
            if (value.contains(".")) return Double.parseDouble(value);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
