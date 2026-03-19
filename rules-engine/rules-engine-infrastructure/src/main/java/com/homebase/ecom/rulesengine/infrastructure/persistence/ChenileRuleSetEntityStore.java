package com.homebase.ecom.rulesengine.infrastructure.persistence;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;
import org.chenile.base.exception.NotFoundException;
import org.chenile.utils.entity.service.EntityStore;

import java.util.Optional;

public class ChenileRuleSetEntityStore implements EntityStore<RuleSet> {

    private RuleSetRepository ruleSetRepository;

    public ChenileRuleSetEntityStore(RuleSetRepository ruleSetRepository) {
        this.ruleSetRepository = ruleSetRepository;
    }

    @Override
    public void store(RuleSet entity) {
        RuleSet saved = ruleSetRepository.save(entity);
        // Copy back generated fields onto the original domain object
        entity.setId(saved.getId());
        entity.setVersion(saved.getVersion());
    }

    @Override
    public RuleSet retrieve(String id) {
        Optional<RuleSet> entity = ruleSetRepository.findById(id);
        if (entity.isPresent())
            return entity.get();
        throw new NotFoundException(1600, "Unable to find RuleSet with ID " + id);
    }
}
