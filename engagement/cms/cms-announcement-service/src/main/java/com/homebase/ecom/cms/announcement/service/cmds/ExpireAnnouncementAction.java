package com.homebase.ecom.cms.announcement.service.cmds;

import com.homebase.ecom.cms.model.Announcement;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the expire transition (ACTIVE -> EXPIRED).
 * Sets status to EXPIRED.
 */
public class ExpireAnnouncementAction extends AbstractSTMTransitionAction<Announcement, Announcement> {

    private static final Logger log = LoggerFactory.getLogger(ExpireAnnouncementAction.class);

    @Override
    public void transitionTo(Announcement announcement, Announcement payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        announcement.setStatus("EXPIRED");

        log.info("Announcement '{}' expired, transitioning from {} to {}",
                announcement.getTitle(), startState.getStateId(), endState.getStateId());
    }
}
