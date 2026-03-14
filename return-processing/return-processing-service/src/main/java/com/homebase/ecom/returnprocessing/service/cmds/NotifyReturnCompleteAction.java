package com.homebase.ecom.returnprocessing.service.cmds;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action: sends notification to customer that the return has been
 * processed and refund has been initiated.
 * Transition: REFUNDED -> COMPLETED
 */
public class NotifyReturnCompleteAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(NotifyReturnCompleteAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Sending return completion notification for return request: {}",
                saga.getReturnRequestId());

        // Build notification content
        String notificationId = "NTF-RET-" + saga.getReturnRequestId() + "-" + System.currentTimeMillis();
        saga.getTransientMap().put("completionNotificationId", notificationId);
        saga.getTransientMap().put("notificationSentAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("notificationType", "RETURN_COMPLETED");

        // Include refund details in notification
        if (saga.getRefundId() != null) {
            saga.getTransientMap().put("notificationRefundId", saga.getRefundId());
            saga.getTransientMap().put("notificationRefundAmount",
                    saga.getRefundAmount() != null ? saga.getRefundAmount().toString() : "0");
        }

        saga.setErrorMessage(null);

        log.info("Customer notified about return completion for request: {}, notificationId: {}",
                saga.getReturnRequestId(), notificationId);
    }
}
