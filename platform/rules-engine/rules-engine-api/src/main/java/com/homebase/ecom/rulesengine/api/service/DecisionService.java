package com.homebase.ecom.rulesengine.api.service;

import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.dto.DecisionDto;

import java.util.List;

public interface DecisionService {
    DecisionDto evaluate(EvaluateRequest request);
    /**
     * Evaluates ALL matching rules across policies for the given facts.
     * Unlike evaluate() which short-circuits on first match, this returns
     * every rule that matches — used by pricing for discount accumulation.
     */
    List<DecisionDto> evaluateAll(EvaluateRequest request);
    List<DecisionDto> getDecisions();
    List<DecisionDto> getDecisionsByRuleSetId(String ruleSetId);
}
