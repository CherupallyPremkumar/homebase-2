package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.ConfirmCancellationOrderPayload;

/**
 * STM Action: Confirm cancellation — transitions from CANCEL_REQUESTED to CANCELLED.
 * Only ADMIN or SYSTEM can confirm.
 */
public class ConfirmCancellationOrderAction extends AbstractSTMTransitionAction<Order, ConfirmCancellationOrderPayload> {

    @Override
    public void transitionTo(Order order, ConfirmCancellationOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getAdminNote() != null) {
            order.getTransientMap().put("adminNote", payload.getAdminNote());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
