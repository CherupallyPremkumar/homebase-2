package com.homebase.ecom.policy.bdd.steps;

import com.homebase.ecom.policy.api.dto.DecisionDto;
import com.homebase.ecom.policy.api.dto.PolicyDto;
import com.homebase.ecom.policy.api.dto.RuleDto;
import com.homebase.ecom.policy.api.service.DecisionService;
import com.homebase.ecom.policy.api.service.PolicyService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import com.homebase.ecom.policy.configuration.PolicyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@CucumberContextConfiguration
@SpringBootTest(classes = PolicyConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PolicySteps {

    @Autowired
    private PolicyService policyService;
    
    @Autowired
    private DecisionService decisionService;

    private DecisionDto lastDecision;

    @Given("a policy {string} exists with default effect {string}")
    public void aPolicyExistsWithDefaultEffect(String id, String effect) {
        PolicyDto dto = new PolicyDto();
        dto.setId(id);
        dto.setName(id);
        dto.setActive(true);
        dto.setRules(new ArrayList<>());
        policyService.createPolicy(dto);
    }

    @Given("rule {string} with expression {string} and effect {string} is added to {string}")
    public void ruleWithExpressionAndEffectIsAddedTo(String ruleId, String expression, String effect, String policyId) {
        PolicyDto policy = policyService.getPolicy(policyId);
        RuleDto rule = new RuleDto();
        rule.setId(ruleId);
        rule.setName(ruleId);
        rule.setExpression(expression);
        // rule.setEffect(Effect.valueOf(effect)); // Mapping logic needed in DTO if used
        policy.getRules().add(rule);
        policyService.updatePolicy(policyId, policy);
    }

    @When("I evaluate policy {string} with context:")
    public void iEvaluatePolicyWithContext(String policyId, Map<String, String> contextData) {
        Map<String, Object> data = new HashMap<>(contextData);
        com.homebase.ecom.policy.api.dto.EvaluateRequest request = new com.homebase.ecom.policy.api.dto.EvaluateRequest();
        request.setPolicyId(policyId);
        request.setSubjectId(contextData.get("userId"));
        request.setFacts(data);
        lastDecision = decisionService.evaluate(request);
    }

    @When("I evaluate policy {string} with context: \\(userId: {string}\\)")
    public void iEvaluatePolicyWithContextUserId(String policyId, String userId) {
        com.homebase.ecom.policy.api.dto.EvaluateRequest request = new com.homebase.ecom.policy.api.dto.EvaluateRequest();
        request.setPolicyId(policyId);
        request.setSubjectId(userId);
        request.setFacts(new HashMap<>());
        lastDecision = decisionService.evaluate(request);
    }

    @Then("the decision should be {string}")
    public void theDecisionShouldBe(String expected) {
        boolean expectedAllowed = expected.equalsIgnoreCase("ALLOWED");
        String expectedEffect = expectedAllowed ? "ALLOW" : "DENY";
        assertEquals(expectedEffect, lastDecision.getEffect());
    }

    @Then("the reason should contain {string}")
    public void theReasonShouldContain(String expectedPart) {
        assertTrue(lastDecision.getReasons() != null && lastDecision.getReasons().contains(expectedPart));
    }
}
