package com.homebase.ecom.product.service.cmds;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.NeedsReworkProductPayload;

public class NeedsReworkProductAction extends AbstractSTMTransitionAction<Product, NeedsReworkProductPayload> {
    @Override
    public void transitionTo(Product product, NeedsReworkProductPayload payload, State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        product.getTransientMap().put("previousPayload", payload);
    }
}
