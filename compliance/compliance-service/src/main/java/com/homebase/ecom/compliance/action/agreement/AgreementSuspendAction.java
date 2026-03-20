package com.homebase.ecom.compliance.action.agreement;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.service.ComplianceValidator;

public class AgreementSuspendAction extends AbstractSTMTransitionAction<Agreement, MinimalPayload> {

    private final ComplianceValidator validator;

    public AgreementSuspendAction(ComplianceValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(Agreement entity, MinimalPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String comment = (payload != null) ? payload.getComment() : null;
        validator.validateCommentRequired(comment, "suspend");
        entity.addActivity("suspend", comment);
    }
}
