package com.homebase.ecom.compliance.action.policy;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.service.ComplianceValidator;

public class PolicyRetireAction extends AbstractSTMTransitionAction<PlatformPolicy, MinimalPayload> {

    private final ComplianceValidator validator;

    public PolicyRetireAction(ComplianceValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(PlatformPolicy entity, MinimalPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String comment = (payload != null) ? payload.getComment() : null;
        validator.validateCommentRequired(comment, "retire");
        entity.addActivity("retire", comment);
    }
}
