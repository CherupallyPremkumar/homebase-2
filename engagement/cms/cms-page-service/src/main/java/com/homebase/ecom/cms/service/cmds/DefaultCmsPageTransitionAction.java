package com.homebase.ecom.cms.service.cmds;

import com.homebase.ecom.cms.model.CmsPage;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Default no-op transition action for CmsPage workflow.
 * Used for transitions that don't require special processing
 * (e.g., submitForReview, reject, requestChanges, restore).
 */
public class DefaultCmsPageTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<CmsPage, PayloadType> {

    @Override
    public void transitionTo(CmsPage page, PayloadType payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) {
        // No-op: state transition is handled by the STM framework
    }
}
