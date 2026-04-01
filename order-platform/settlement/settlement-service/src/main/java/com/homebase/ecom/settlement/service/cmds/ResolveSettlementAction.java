package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.ResolveSettlementPayload;
import com.homebase.ecom.settlement.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the resolve event (dispute resolution without amount adjustment).
 * Transitions settlement back to APPROVED.
 */
public class ResolveSettlementAction extends AbstractSTMTransitionAction<Settlement, ResolveSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResolveSettlementAction.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void transitionTo(Settlement settlement, ResolveSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String resolution = (payload != null && payload.getResolution() != null)
                ? payload.getResolution()
                : "Dispute resolved";

        log.info("Settlement {} dispute resolved for supplier {}. Resolution: {}",
                settlement.getId(), settlement.getSupplierId(), resolution);

        if (notificationPort != null) {
            notificationPort.notifyDisputeResolved(settlement, resolution);
        }

        settlement.getTransientMap().previousPayload = payload;
    }
}
