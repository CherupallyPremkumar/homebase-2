package com.homebase.ecom.compliance.action.policy;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.dto.PlatformPolicyPayload;
import com.homebase.ecom.compliance.service.ComplianceValidator;

public class PolicyAmendAction extends AbstractSTMTransitionAction<PlatformPolicy, PlatformPolicyPayload> {

    private final ComplianceValidator validator;

    public PolicyAmendAction(ComplianceValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(PlatformPolicy entity, PlatformPolicyPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = (payload != null) ? payload.getReason() : null;
        validator.validatePolicyAmendReason(reason);
        if (payload != null) {
            if (payload.getContentUrl() != null) entity.setContentUrl(payload.getContentUrl());
            if (payload.getSummaryText() != null) entity.setSummaryText(payload.getSummaryText());
        }
        entity.addActivity("amend", "Policy amended: " + reason);
    }
}
