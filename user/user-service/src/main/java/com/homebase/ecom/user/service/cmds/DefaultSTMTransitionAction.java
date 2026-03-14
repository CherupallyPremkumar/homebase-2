package com.homebase.ecom.user.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.user.domain.model.User;

public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload> extends AbstractSTMTransitionAction<User, PayloadType> {
    @Override
    public void transitionTo(User user, PayloadType payload, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) {

    }
}
