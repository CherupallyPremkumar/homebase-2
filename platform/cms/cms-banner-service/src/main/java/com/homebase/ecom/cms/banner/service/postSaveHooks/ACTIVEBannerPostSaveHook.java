package com.homebase.ecom.cms.banner.service.postSaveHooks;

import com.homebase.ecom.cms.model.Banner;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for ACTIVE state.
 * Executes after a banner transitions to ACTIVE state.
 * Can be used to trigger CDN cache invalidation, analytics events, etc.
 */
public class ACTIVEBannerPostSaveHook implements PostSaveHook<Banner> {

    private static final Logger log = LoggerFactory.getLogger(ACTIVEBannerPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Banner banner, TransientMap map) {
        log.info("Banner '{}' is now ACTIVE. Previous state: {}",
                banner.getName(), startState.getStateId());
    }
}
