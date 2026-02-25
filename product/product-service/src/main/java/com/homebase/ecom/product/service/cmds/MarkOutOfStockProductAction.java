package com.homebase.ecom.product.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.model.Product;
import com.homebase.ecom.product.dto.MarkOutOfStockProductPayload;

/**
 Contains customized logic for the transition. Common logic resides at {@link DefaultSTMTransitionAction}
 <p>Use this class if you want to augment the common logic for this specific transition</p>
 <p>Use a customized payload if required instead of MinimalPayload</p>
*/
public class MarkOutOfStockProductAction extends AbstractSTMTransitionAction<Product,

    MarkOutOfStockProductPayload>{


	@Override
	public void transitionTo(Product product,
            MarkOutOfStockProductPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            product.transientMap.previousPayload = payload;
	}

}
