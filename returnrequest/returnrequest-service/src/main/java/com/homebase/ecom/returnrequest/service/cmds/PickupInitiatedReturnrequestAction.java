package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.PickupInitiatedReturnrequestPayload;

/**
 Contains customized logic for the transition. Common logic resides at {@link DefaultSTMTransitionAction}
 <p>Use this class if you want to augment the common logic for this specific transition</p>
 <p>Use a customized payload if required instead of MinimalPayload</p>
*/
public class PickupInitiatedReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,

    PickupInitiatedReturnrequestPayload>{


	@Override
	public void transitionTo(Returnrequest returnrequest,
            PickupInitiatedReturnrequestPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            returnrequest.transientMap.previousPayload = payload;
	}

}
