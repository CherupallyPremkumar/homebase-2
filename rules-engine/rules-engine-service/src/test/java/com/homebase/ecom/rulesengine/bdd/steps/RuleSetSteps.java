package com.homebase.ecom.rulesengine.bdd.steps;

import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import com.homebase.ecom.rulesengine.api.dto.RuleDto;
import com.homebase.ecom.rulesengine.api.enums.Effect;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.rulesengine.api.service.RuleSetService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.chenile.security.KeycloakConnectionDetails;
import org.chenile.security.test.BaseSecurityTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.homebase.ecom.rulesengine.SpringTestConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class RuleSetSteps {

    @Autowired
    private RuleSetService ruleSetService;

    @Autowired
    private DecisionService decisionService;

    @Autowired
    private KeycloakConnectionDetails connectionDetails;

    private DecisionDto lastDecision;
    private List<DecisionDto> lastDecisions;

    @DynamicPropertySource
    static void keycloakProps(DynamicPropertyRegistry registry) {
        registry.add("KEYCLOAK_HOST", BaseSecurityTest::getUrl);
        registry.add("KEYCLOAK_PORT", BaseSecurityTest::getHttpPort);
        registry.add("KEYCLOAK_REALM", () -> "tenant0");
        BaseSecurityTest.keycloakProperties(registry);
    }

    @Before
    public void beforeScenario() {
        connectionDetails.host = BaseSecurityTest.getUrl();
        connectionDetails.httpPort = BaseSecurityTest.getHttpPort();
    }

    // ═══════════════════════════════════════════════════════════════
    // GIVEN: Create inline policies (for unit-level tests)
    // ═══════════════════════════════════════════════════════════════

    @Given("a policy {string} exists with default effect {string}")
    public void aPolicyExistsWithDefaultEffect(String id, String effect) {
        RuleSetDto dto = new RuleSetDto();
        dto.setId(id);
        dto.setName(id);
        dto.setActive(true);
        dto.setDefaultEffect(Effect.valueOf(effect));
        dto.setRules(new ArrayList<>());
        ruleSetService.createRuleSet(dto);
    }

    @Given("rule {string} with expression {string} and effect {string} is added to {string}")
    public void ruleWithExpressionAndEffectIsAddedTo(String ruleId, String expression, String effect, String ruleSetId) {
        RuleSetDto ruleSet = ruleSetService.getRuleSet(ruleSetId);
        RuleDto rule = new RuleDto();
        rule.setId(ruleId);
        rule.setName(ruleId);
        rule.setExpression(expression);
        rule.setEffect(Effect.valueOf(effect));
        rule.setPriority(100 - ruleSet.getRules().size());
        rule.setActive(true);
        ruleSet.getRules().add(rule);
        ruleSetService.updateRuleSet(ruleSetId, ruleSet);
    }

    // ═══════════════════════════════════════════════════════════════
    // WHEN: Evaluate by ruleSetId (old style)
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
    // WHEN: Evaluate by targetModule (real rules from policy-rules.json)
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

    // Old-style assertions
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
    // HELPER: Parse fact values to proper types
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
