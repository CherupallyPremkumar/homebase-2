package com.homebase.ecom.cms.service.cmds;

import com.homebase.ecom.cms.model.CmsPage;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the unpublish transition (PUBLISHED -> DRAFT).
 * Sets published=false.
 */
public class UnpublishCmsPageAction extends AbstractSTMTransitionAction<CmsPage, CmsPage> {

    private static final Logger log = LoggerFactory.getLogger(UnpublishCmsPageAction.class);

    @Override
    public void transitionTo(CmsPage page, CmsPage payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        page.setPublished(false);

        log.info("CMS page '{}' (slug={}) unpublished, moving back to DRAFT",
                page.getTitle(), page.getSlug());
    }
}
