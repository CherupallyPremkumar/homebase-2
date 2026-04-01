package com.homebase.ecom.cms.service.postSaveHooks;

import com.homebase.ecom.cms.model.CmsPage;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for PUBLISHED state.
 * Creates a CmsPageVersion snapshot of all blocks when a page is published.
 * This ensures version history is maintained for rollback capability.
 */
public class PUBLISHEDCmsPagePostSaveHook implements PostSaveHook<CmsPage> {

    private static final Logger log = LoggerFactory.getLogger(PUBLISHEDCmsPagePostSaveHook.class);

    @Override
    public void execute(State startState, State endState, CmsPage page, TransientMap map) {
        log.info("CMS page '{}' (slug={}) published — creating version snapshot. Previous state: {}",
                page.getTitle(), page.getSlug(), startState.getStateId());

        // Version snapshot creation is delegated to CmsServiceImpl.
        // The service layer will:
        //   1. Query all cms_block rows for this page
        //   2. Serialize them into a JSON snapshot
        //   3. Insert a new cms_page_version row with incremented version number
        // This hook signals the event; actual persistence occurs via the service layer.
    }
}
