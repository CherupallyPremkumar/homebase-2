package com.homebase.ecom.user.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.user.model.User;
import com.homebase.ecom.user.dto.RejectSupplierUserPayload;

/**
 Contains customized logic for the transition. Common logic resides at {@link DefaultSTMTransitionAction}
 <p>Use this class if you want to augment the common logic for this specific transition</p>
 <p>Use a customized payload if required instead of MinimalPayload</p>
*/
public class RejectSupplierUserAction extends AbstractSTMTransitionAction<User,

    RejectSupplierUserPayload>{


	@Override
	public void transitionTo(User user,
            RejectSupplierUserPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            user.transientMap.previousPayload = payload;
	}

}
