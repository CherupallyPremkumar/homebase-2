package com.homebase.ecom.compliance.action.agreement;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.dto.AgreementPayload;
import com.homebase.ecom.compliance.service.ComplianceValidator;

public class AgreementSupersedeAction extends AbstractSTMTransitionAction<Agreement, AgreementPayload> {

    private final ComplianceValidator validator;

    public AgreementSupersedeAction(ComplianceValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(Agreement entity, AgreementPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getSupersededById() != null) {
            entity.setSupersededById(payload.getSupersededById());
        }
        validator.validateAgreementForSupersede(entity);
        entity.addActivity("supersede", "Superseded by newer version");
    }
}
