package com.homebase.ecom.compliance.service;

import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import com.homebase.ecom.compliance.model.Agreement;

public class AgreementStateEntityService extends HmStateEntityServiceImpl<Agreement> {

    private final ComplianceValidator validator;

    public AgreementStateEntityService(STM<Agreement> stm, STMActionsInfoProvider stmActionsInfoProvider,
                                       EntityStore<Agreement> entityStore, ComplianceValidator validator) {
        super(stm, stmActionsInfoProvider, entityStore);
        this.validator = validator;
    }

    @Override
    public StateEntityServiceResponse<Agreement> create(Agreement entity) {
        validator.validateAgreementForCreate(entity);
        return super.create(entity);
    }
}
