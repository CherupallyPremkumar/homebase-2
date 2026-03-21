package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.ResolveChargebackPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for resolving a chargeback dispute.
 * Sets chargebackWon on the transient map for the CHECK_CHARGEBACK_OUTCOME auto-state.
 */
public class ResolveChargebackAction extends AbstractSTMTransitionAction<Payment, ResolveChargebackPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResolveChargebackAction.class);

    @Override
    public void transitionTo(Payment payment, ResolveChargebackPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set chargebackWon on the entity for the OGNL auto-state to evaluate
        if (payload != null) {
            payment.setChargebackWon(payload.isChargebackWon());
        }

        log.info("Chargeback resolved for paymentId={}, orderId={}, merchantWon={}",
                payment.getId(), payment.getOrderId(),
                payload != null ? payload.isChargebackWon() : "N/A");
    }
}
