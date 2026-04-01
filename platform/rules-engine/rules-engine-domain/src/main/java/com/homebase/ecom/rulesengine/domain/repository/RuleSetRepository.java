package com.homebase.ecom.rulesengine.domain.repository;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import java.util.List;
import java.util.Optional;

public interface RuleSetRepository {
    RuleSet save(RuleSet ruleSet);
    Optional<RuleSet> findById(String id);
    List<RuleSet> findAll();
    List<RuleSet> findByTargetModuleAndActiveTrue(String targetModule);
    List<RuleSet> findByTargetModuleAndActiveTrueAndTenant(String targetModule, String tenant);
    List<RuleSet> findAllByTenant(String tenant);
    void deleteById(String id);
}
