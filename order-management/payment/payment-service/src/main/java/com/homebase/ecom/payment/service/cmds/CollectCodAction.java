package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.CollectCodPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for confirming cash-on-delivery collection by carrier.
 */
public class CollectCodAction extends AbstractSTMTransitionAction<Payment, CollectCodPayload> {

    private static final Logger log = LoggerFactory.getLogger(CollectCodAction.class);

    @Override
    public void transitionTo(Payment payment, CollectCodPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload != null && payload.getCollectedAmount() != null) {
            payment.recordSuccess(null, "COD collected: " + payload.getCollectedAmount());
        }

        log.info("COD collected for paymentId={}, orderId={}, collectedBy={}",
                payment.getId(), payment.getOrderId(),
                payload != null ? payload.getCollectedBy() : "N/A");
    }
}
