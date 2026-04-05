package com.homebase.ecom.cms.banner.service.cmds;

import com.homebase.ecom.cms.model.Banner;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the activate transition (DRAFT/SCHEDULED/EXPIRED -> ACTIVE).
 * Sets active=true.
 */
public class ActivateBannerAction extends AbstractSTMTransitionAction<Banner, Banner> {

    private static final Logger log = LoggerFactory.getLogger(ActivateBannerAction.class);

    @Override
    public void transitionTo(Banner banner, Banner payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        banner.setActive(true);

        log.info("Banner '{}' activated, transitioning from {} to {}",
                banner.getName(), startState.getStateId(), endState.getStateId());
    }
}
