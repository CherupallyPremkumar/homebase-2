package com.homebase.ecom.rulesengine.api.service;

import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import java.util.List;

/**
 * Read-only service interface for RuleSet.
 * All mutations (create, submit, approve, activate, deprecate, deactivate)
 * go through the controller → STM processById() path.
 * This interface is what other BCs consume via rules-engine-client.
 */
public interface RuleSetService {
    RuleSetDto getRuleSet(String id);
    List<RuleSetDto> listRuleSets();
    List<RuleSetDto> findByTargetModule(String targetModule);
}
