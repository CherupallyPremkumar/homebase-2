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
 * Handles the publish transition (DRAFT -> PUBLISHED or IN_REVIEW -> PUBLISHED).
 * Sets published=true and publishedAt=now.
 */
public class PublishCmsPageAction extends AbstractSTMTransitionAction<CmsPage, CmsPage> {

    private static final Logger log = LoggerFactory.getLogger(PublishCmsPageAction.class);

    @Override
    public void transitionTo(CmsPage page, CmsPage payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        page.setPublished(true);
        page.setPublishedAt(LocalDateTime.now());

        log.info("CMS page '{}' (slug={}) published, transitioning from {} to {}",
                page.getTitle(), page.getSlug(), startState.getStateId(), endState.getStateId());
    }
}
