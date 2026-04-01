package com.homebase.ecom.cms.announcement.service.postSaveHooks;

import com.homebase.ecom.cms.model.Announcement;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for ACTIVE state.
 * Executes after an announcement transitions to ACTIVE state.
 * Can be used to trigger notifications, push to storefront, etc.
 */
public class ACTIVEAnnouncementPostSaveHook implements PostSaveHook<Announcement> {

    private static final Logger log = LoggerFactory.getLogger(ACTIVEAnnouncementPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Announcement announcement, TransientMap map) {
        log.info("Announcement '{}' is now ACTIVE. Previous state: {}",
                announcement.getTitle(), startState.getStateId());
    }
}
