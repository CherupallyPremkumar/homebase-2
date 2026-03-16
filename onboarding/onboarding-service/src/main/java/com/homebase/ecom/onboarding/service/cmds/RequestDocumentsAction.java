package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.RequestDocumentsPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.NotificationPort;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin requests additional documents from the seller.
 * Transitions from DOCUMENT_VERIFICATION to DOCUMENTS_REQUESTED.
 */
public class RequestDocumentsAction extends AbstractSTMTransitionAction<OnboardingSaga, RequestDocumentsPayload> {

    private static final Logger log = LoggerFactory.getLogger(RequestDocumentsAction.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void transitionTo(OnboardingSaga saga, RequestDocumentsPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        String notes = payload.getNotes();
        saga.setVerificationNotes(notes);

        if (notificationPort != null) {
            notificationPort.notifyDocumentsRequested(saga.getId(), saga.getBusinessName(), notes);
        }

        log.info("Additional documents requested for onboarding {}, business: {}, notes: {}",
                saga.getId(), saga.getBusinessName(), notes);
    }
}
