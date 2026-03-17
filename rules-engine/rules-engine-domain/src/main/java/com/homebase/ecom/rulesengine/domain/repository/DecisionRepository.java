package com.homebase.ecom.rulesengine.domain.repository;

import com.homebase.ecom.rulesengine.domain.model.Decision;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository {
    Decision save(Decision decision);
    Optional<Decision> findById(String id);
    List<Decision> findAll();
    List<Decision> findByRuleSetId(String ruleSetId);
}
