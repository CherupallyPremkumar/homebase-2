package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.SuspendSupplierSupplierPayload;

/**
 Contains customized logic for the transition. Common logic resides at {@link DefaultSTMTransitionAction}
 <p>Use this class if you want to augment the common logic for this specific transition</p>
 <p>Use a customized payload if required instead of MinimalPayload</p>
*/
public class SuspendSupplierSupplierAction extends AbstractSTMTransitionAction<Supplier,

    SuspendSupplierSupplierPayload>{


	@Override
	public void transitionTo(Supplier supplier,
            SuspendSupplierSupplierPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            supplier.transientMap.previousPayload = payload;
	}

}
