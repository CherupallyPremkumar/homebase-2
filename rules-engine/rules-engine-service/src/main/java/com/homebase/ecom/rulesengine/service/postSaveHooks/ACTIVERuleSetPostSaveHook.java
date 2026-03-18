package com.homebase.ecom.rulesengine.service.postSaveHooks;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.service.cache.RuleSetCacheManager;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook when a RuleSet transitions to ACTIVE state.
 * Evicts cache so new active rules are picked up immediately.
 */
public class ACTIVERuleSetPostSaveHook implements PostSaveHook<RuleSet> {
    private static final Logger log = LoggerFactory.getLogger(ACTIVERuleSetPostSaveHook.class);
    private final RuleSetCacheManager cacheManager;

    public ACTIVERuleSetPostSaveHook(RuleSetCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void execute(State startState, State endState, RuleSet ruleSet, TransientMap map) {
        log.info("RuleSet '{}' activated ({}→{}). Evicting cache for module: {}",
                ruleSet.getName(), startState, endState, ruleSet.getTargetModule());
        cacheManager.evictAll();
    }
}
