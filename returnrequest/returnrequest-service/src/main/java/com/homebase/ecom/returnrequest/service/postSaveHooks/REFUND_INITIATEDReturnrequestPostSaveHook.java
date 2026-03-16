package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.domain.port.NotificationPort;
import com.homebase.ecom.returnrequest.domain.port.RefundPort;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for REFUND_INITIATED state.
 * Triggers payment refund via RefundPort and notifies customer.
 */
public class REFUND_INITIATEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(REFUND_INITIATEDReturnrequestPostSaveHook.class);

    @Autowired(required = false)
    private RefundPort refundPort;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} refund initiated, triggering payment refund", returnrequest.getId());

        // Trigger payment refund
        if (refundPort != null) {
            refundPort.initiateRefund(
                    returnrequest.getId(),
                    returnrequest.orderId,
                    returnrequest.customerId,
                    returnrequest.totalRefundAmount,
                    returnrequest.returnType);
        }

        // Notify customer
        if (notificationPort != null) {
            notificationPort.notifyRefundInitiated(
                    returnrequest.getId(),
                    returnrequest.customerId,
                    returnrequest.orderId,
                    returnrequest.totalRefundAmount != null ? returnrequest.totalRefundAmount.toString() : "0");
        }
    }
}
