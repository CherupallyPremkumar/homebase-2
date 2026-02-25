package com.homebase.ecom.inventory.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.model.Inventory;
import com.homebase.ecom.inventory.dto.ReserveStockInventoryPayload;

/**
 Contains customized logic for the transition. Common logic resides at {@link DefaultSTMTransitionAction}
 <p>Use this class if you want to augment the common logic for this specific transition</p>
 <p>Use a customized payload if required instead of MinimalPayload</p>
*/
public class ReserveStockInventoryAction extends AbstractSTMTransitionAction<Inventory,

    ReserveStockInventoryPayload>{


	@Override
	public void transitionTo(Inventory inventory,
            ReserveStockInventoryPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            inventory.transientMap.previousPayload = payload;
	}

}
