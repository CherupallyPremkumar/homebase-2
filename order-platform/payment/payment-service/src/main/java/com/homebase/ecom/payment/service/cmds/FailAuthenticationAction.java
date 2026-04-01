package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.FailAuthenticationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for recording authentication failure.
 */
public class FailAuthenticationAction extends AbstractSTMTransitionAction<Payment, FailAuthenticationPayload> {

    private static final Logger log = LoggerFactory.getLogger(FailAuthenticationAction.class);

    @Override
    public void transitionTo(Payment payment, FailAuthenticationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload != null && payload.getFailureReason() != null) {
            payment.recordFailure(payload.getFailureReason(), null);
        }
        log.warn("Authentication failed for paymentId={}, orderId={}, reason={}",
                payment.getId(), payment.getOrderId(),
                payload != null ? payload.getFailureReason() : "N/A");
    }
}
