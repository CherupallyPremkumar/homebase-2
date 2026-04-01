package com.homebase.ecom.rulesengine.service.postSaveHooks;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.service.cache.RuleSetCacheManager;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook when a RuleSet transitions to REVIEW state.
 */
public class REVIEWRuleSetPostSaveHook implements PostSaveHook<RuleSet> {
    private static final Logger log = LoggerFactory.getLogger(REVIEWRuleSetPostSaveHook.class);
    private final RuleSetCacheManager cacheManager;

    public REVIEWRuleSetPostSaveHook(RuleSetCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void execute(State startState, State endState, RuleSet ruleSet, TransientMap map) {
        log.info("RuleSet '{}' moved to REVIEW ({}→{}).",
                ruleSet.getName(), startState, endState);
        // No cache eviction needed for REVIEW — rules aren't served until ACTIVE
    }
}
