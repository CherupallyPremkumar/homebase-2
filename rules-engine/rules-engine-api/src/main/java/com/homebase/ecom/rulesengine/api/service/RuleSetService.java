package com.homebase.ecom.rulesengine.api.service;

import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import java.util.List;

public interface RuleSetService {
    RuleSetDto createRuleSet(RuleSetDto ruleSetDto);
    RuleSetDto getRuleSet(String id);
    List<RuleSetDto> listRuleSets();
    RuleSetDto updateRuleSet(String id, RuleSetDto ruleSetDto);
    void deleteRuleSet(String id);

    // STM Transitions
    RuleSetDto submitRuleSet(String id);
    RuleSetDto approveRuleSet(String id);
    RuleSetDto activateRuleSet(String id);
    RuleSetDto deprecateRuleSet(String id);

}
