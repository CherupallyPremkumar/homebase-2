package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.SettlePaymentPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for settling a payment (funds transferred to merchant).
 */
public class SettlePaymentAction extends AbstractSTMTransitionAction<Payment, SettlePaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(SettlePaymentAction.class);

    @Override
    public void transitionTo(Payment payment, SettlePaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Payment settled for paymentId={}, orderId={}, amount={}",
                payment.getId(), payment.getOrderId(), payment.getAmount());
    }
}
