package com.homebase.ecom.compliance.action.policy;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.dto.PlatformPolicyPayload;
import com.homebase.ecom.compliance.service.ComplianceValidator;

public class PolicyPublishAction extends AbstractSTMTransitionAction<PlatformPolicy, PlatformPolicyPayload> {

    private final ComplianceValidator validator;

    public PolicyPublishAction(ComplianceValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(PlatformPolicy entity, PlatformPolicyPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null) {
            if (payload.getContentUrl() != null) entity.setContentUrl(payload.getContentUrl());
            if (payload.getContentHash() != null) entity.setContentHash(payload.getContentHash());
            if (payload.getEffectiveDate() != null) entity.setEffectiveDate(payload.getEffectiveDate());
            if (payload.getSummaryText() != null) entity.setSummaryText(payload.getSummaryText());
            if (payload.getVersionLabel() != null) entity.setVersionLabel(payload.getVersionLabel());
        }
        validator.validatePolicyForPublish(entity);
        entity.addActivity("publish", "Policy published and effective");
    }
}
