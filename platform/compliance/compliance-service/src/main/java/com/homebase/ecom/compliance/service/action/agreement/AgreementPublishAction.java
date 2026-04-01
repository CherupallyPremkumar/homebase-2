package com.homebase.ecom.compliance.service.action.agreement;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.dto.AgreementPayload;
import com.homebase.ecom.compliance.service.ComplianceValidator;

public class AgreementPublishAction extends AbstractSTMTransitionAction<Agreement, AgreementPayload> {

    private final ComplianceValidator validator;

    public AgreementPublishAction(ComplianceValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(Agreement entity, AgreementPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null) {
            if (payload.getEffectiveDate() != null) entity.setEffectiveDate(payload.getEffectiveDate());
            if (payload.getExpiryDate() != null) entity.setExpiryDate(payload.getExpiryDate());
            if (payload.getContentUrl() != null) entity.setContentUrl(payload.getContentUrl());
            if (payload.getContentHash() != null) entity.setContentHash(payload.getContentHash());
            if (payload.getVersionLabel() != null) entity.setVersionLabel(payload.getVersionLabel());
        }
        validator.validateAgreementForPublish(entity);
        entity.addActivity("publish", "Agreement published for acceptance");
    }
}
