package com.homebase.ecom.cms.service.cmds;

import com.homebase.ecom.cms.model.CmsPage;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handles the approve transition (IN_REVIEW -> PUBLISHED).
 * Sets published=true and publishedAt=now.
 */
public class ApproveCmsPageAction extends AbstractSTMTransitionAction<CmsPage, CmsPage> {

    private static final Logger log = LoggerFactory.getLogger(ApproveCmsPageAction.class);

    @Override
    public void transitionTo(CmsPage page, CmsPage payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        page.setPublished(true);
        page.setPublishedAt(LocalDateTime.now());

        log.info("CMS page '{}' (slug={}) approved and published, transitioning from {} to {}",
                page.getTitle(), page.getSlug(), startState.getStateId(), endState.getStateId());
    }
}
