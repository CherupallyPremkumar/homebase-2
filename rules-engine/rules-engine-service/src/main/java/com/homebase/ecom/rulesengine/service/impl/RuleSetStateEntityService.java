package com.homebase.ecom.rulesengine.service.impl;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;

/**
 * Custom state entity service for RuleSet.
 * Overrides create() to validate required fields before STM processes the entity.
 */
public class RuleSetStateEntityService extends HmStateEntityServiceImpl<RuleSet> {

    private final RulesEngineValidator validator;

    public RuleSetStateEntityService(STM<RuleSet> stm,
                                     STMActionsInfoProvider infoProvider,
                                     EntityStore<RuleSet> entityStore,
                                     RulesEngineValidator validator) {
        super(stm, infoProvider, entityStore);
        this.validator = validator;
    }

    @Override
    public StateEntityServiceResponse<RuleSet> create(RuleSet entity) {
        validator.validateForCreate(entity);
        return super.create(entity);
    }
}
