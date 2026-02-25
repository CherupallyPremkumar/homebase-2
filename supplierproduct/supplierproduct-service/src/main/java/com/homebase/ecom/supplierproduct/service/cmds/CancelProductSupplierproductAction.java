package com.homebase.ecom.supplierproduct.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplierproduct.model.Supplierproduct;
import com.homebase.ecom.supplierproduct.dto.CancelProductSupplierproductPayload;

/**
 Contains customized logic for the transition. Common logic resides at {@link DefaultSTMTransitionAction}
 <p>Use this class if you want to augment the common logic for this specific transition</p>
 <p>Use a customized payload if required instead of MinimalPayload</p>
*/
public class CancelProductSupplierproductAction extends AbstractSTMTransitionAction<Supplierproduct,

    CancelProductSupplierproductPayload>{


	@Override
	public void transitionTo(Supplierproduct supplierproduct,
            CancelProductSupplierproductPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            supplierproduct.transientMap.previousPayload = payload;
	}

}
