package com.homebase.ecom.policy.service.configuration.controller;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;
import com.homebase.ecom.policy.api.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for policy evaluation.
 * Exposes the DecisionService to other bounded contexts.
 */
@RestController
public class DecisionController {

    @Autowired
    private DecisionService decisionService;

    @PostMapping("/policy/evaluate")
    public DecisionDto evaluate(@RequestBody EvaluateRequest request) {
        return decisionService.evaluate(request);
    }

    @org.springframework.web.bind.annotation.GetMapping("/api/decisions")
    public java.util.List<DecisionDto> getAllDecisions() {
        return decisionService.getDecisions();
    }

    @org.springframework.web.bind.annotation.GetMapping("/api/policies/{policyId}/decisions")
    public java.util.List<DecisionDto> getDecisionsByPolicyId(@org.springframework.web.bind.annotation.PathVariable String policyId) {
        return decisionService.getDecisionsByPolicyId(policyId);
    }
}
