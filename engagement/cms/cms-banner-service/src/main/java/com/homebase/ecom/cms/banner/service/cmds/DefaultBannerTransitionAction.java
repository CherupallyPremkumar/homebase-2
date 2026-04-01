package com.homebase.ecom.cms.banner.service.cmds;

import com.homebase.ecom.cms.model.Banner;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Default no-op transition action for Banner workflow.
 * Used for transitions that don't require special processing
 * (e.g., schedule, expire).
 */
public class DefaultBannerTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<Banner, PayloadType> {

    @Override
    public void transitionTo(Banner banner, PayloadType payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) {
        // No-op: state transition is handled by the STM framework
    }
}
