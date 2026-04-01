package com.homebase.ecom.cms.announcement.service.cmds;

import com.homebase.ecom.cms.model.Announcement;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Default no-op transition action for Announcement workflow.
 * Used for transitions that don't require special processing
 * (e.g., deactivate).
 */
public class DefaultAnnouncementTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<Announcement, PayloadType> {

    @Override
    public void transitionTo(Announcement announcement, PayloadType payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) {
        // No-op: state transition is handled by the STM framework
    }
}
