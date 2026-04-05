package com.homebase.ecom.cms.service.cmds;

import com.homebase.ecom.cms.dto.RejectCmsPagePayload;
import com.homebase.ecom.cms.model.CmsPage;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the reject transition (IN_REVIEW -> DRAFT).
 * Records the rejection reason from the payload.
 */
public class RejectCmsPageAction extends AbstractSTMTransitionAction<CmsPage,
        RejectCmsPagePayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectCmsPageAction.class);

    @Override
    public void transitionTo(CmsPage page,
                             RejectCmsPagePayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        page.setPublished(false);

        if (payload != null && payload.getRejectionReason() != null) {
            log.info("CMS page '{}' (slug={}) rejected — reason: {}",
                    page.getTitle(), page.getSlug(), payload.getRejectionReason());
        } else {
            log.info("CMS page '{}' (slug={}) rejected, moving back to DRAFT",
                    page.getTitle(), page.getSlug());
        }
    }
}
