package com.homebase.ecom.rulesengine.infrastructure.persistence.adapter;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that bridges the Domain Repository with Spring Data JPA.
 */
public class RuleSetRepositoryAdapter {
    private final RuleSetRepository ruleSetRepository;

    public RuleSetRepositoryAdapter(RuleSetRepository ruleSetRepository) {
        this.ruleSetRepository = ruleSetRepository;
    }

    public RuleSet save(RuleSet ruleSet) {
        return ruleSetRepository.save(ruleSet);
    }

    public Optional<RuleSet> findById(String id) {
        return ruleSetRepository.findById(id);
    }

    public List<RuleSet> findAll() {
        return ruleSetRepository.findAll();
    }

    public void deleteById(String id) {
        ruleSetRepository.deleteById(id);
    }
}
