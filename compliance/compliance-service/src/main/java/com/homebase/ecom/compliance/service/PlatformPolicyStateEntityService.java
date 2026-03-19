package com.homebase.ecom.compliance.service;

import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import com.homebase.ecom.compliance.model.PlatformPolicy;

public class PlatformPolicyStateEntityService extends HmStateEntityServiceImpl<PlatformPolicy> {

    private final ComplianceValidator validator;

    public PlatformPolicyStateEntityService(STM<PlatformPolicy> stm, STMActionsInfoProvider stmActionsInfoProvider,
                                            EntityStore<PlatformPolicy> entityStore, ComplianceValidator validator) {
        super(stm, stmActionsInfoProvider, entityStore);
        this.validator = validator;
    }

    @Override
    public StateEntityServiceResponse<PlatformPolicy> create(PlatformPolicy entity) {
        validator.validatePolicyForCreate(entity);
        return super.create(entity);
    }
}
